package com.scm.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.dao.ContactsRepository;
import com.scm.dao.SUserRepository;
import com.scm.helper.Message;
import com.scm.models.Contacts;
import com.scm.models.SUser;

@Controller
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	private SUserRepository userRepo;

	@Autowired
	private ContactsRepository conRepo;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String loggedInUsername = principal.getName();
		// get the user by username
		SUser userByLoggedInUsername = userRepo.getUserByUsername(loggedInUsername);
		model.addAttribute("user", userByLoggedInUsername);
	}

	@GetMapping(path = "/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("title", "dashboard");
		return "normal/user_dashboard";
	}

	@GetMapping(path = "/add_contact")
	public String addContactForm(Model model) {
		model.addAttribute("title", "add contact");
		model.addAttribute("contact", new Contacts());
		return "normal/add_contact";
	}

	@PostMapping(path = "/process-contact")
	public String addContactProcessing(@ModelAttribute("contact") Contacts contact, Principal principal,
			@RequestParam("contactImage") MultipartFile file, HttpSession session) {

		try {
			String userByUsername = principal.getName();
			SUser userByLoggedInUsername = this.userRepo.getUserByUsername(userByUsername);

			// processing profile image
			if (file.isEmpty()) {
				contact.setConImgUrl("pngwing.com.png");

			} else {
				contact.setConImgUrl(file.getOriginalFilename());

				File saveToLocation = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveToLocation.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("profile image set");
			}

			contact.setSuser(userByLoggedInUsername);
			userByLoggedInUsername.getContacts().add(contact);

			this.userRepo.save(userByLoggedInUsername);
			System.out.println("Added to database");

			// sending alerts
			session.setAttribute("message", new Message("Contact added successfully, you can add more", "alert-info"));

			return "normal/add_contact";

		} catch (Exception e) {
			System.out.println("Error occured");
			e.printStackTrace();
			session.setAttribute("message", new Message("contact not added, please try again", "alert-danger"));
		}

		return "normal/add_contact";
	}

	@GetMapping(path = "/view-contacts/{pageno}")
	public String viewContacts(@PathVariable int pageno, Model model, Principal principal) {
//	 	Method : 1 to do it
//		String username = principal.getName();
//		SUser userByUsername = this.userRepo.getUserByUsername(username);
//		List<Contacts> contacts = userByUsername.getContacts();

// 		Method: 2
		String username = principal.getName();
		int userId = this.userRepo.getUserByUsername(username).getId();

		Pageable pageable = PageRequest.of(pageno, 3);
		Page<Contacts> contactList = this.conRepo.findByUsername(userId, pageable);

		model.addAttribute("contacts", contactList);
		model.addAttribute("currentPage", pageno);
		int totalPages = contactList.getTotalPages();
		System.out.println(totalPages);
		model.addAttribute("totalPages", totalPages);

		return "normal/view_contacts";
	}

	@GetMapping("/view-contact/{conId}")
	public String viewSingleContact(@PathVariable int conId, Model model, Principal principal) {

		Contacts contact = this.conRepo.findById(conId).get();

		String user = principal.getName();
		SUser userByUsername = this.userRepo.getUserByUsername(user);

		if (userByUsername.getId() == contact.getSuser().getId()) {
			model.addAttribute("contact", contact);
		}

		return "normal/view-single-contact";
	}

	@GetMapping(path = "/edit-contact/{conId}")
	public String editContact(@PathVariable int conId, Model model) {
		Contacts contactById = this.conRepo.findById(conId).get();
		model.addAttribute("contact", contactById);
		return "normal/edit_contact";
	}

	@PostMapping(path = "edit-contact-process")
	public String editContactProcess(@ModelAttribute Contacts contact) {
		Contacts contactById = this.conRepo.findById(contact.getConId()).get();
		contactById.setConName(contact.getConName());
		contactById.setConNickname(contact.getConNickname());
		contactById.setConPhone(contact.getConPhone());
		contactById.setConEmail(contact.getConEmail());
		contactById.setConDescription(contact.getConDescription());
		this.conRepo.save(contactById);
		return "normal/user_dashboard";
	}

	@GetMapping(path = "/delete-contact/{conId}")
	public String deleteContact(@PathVariable int conId, Model model) {
		this.conRepo.deleteById(conId);
		model.addAttribute("currentPage", 0);
		return "redirect:user/view-contacts/0";
	}

	@GetMapping(path = "/profile")
	public String userProfile(Model model) {
		model.addAttribute("title", "Profile");
		return "normal/profile";
	}

	@GetMapping(path = "/settings")
	public String settings(Model model) {
		model.addAttribute("title", "Settings");
		return "normal/settings";
	}

	@PostMapping(path = "/change-password")
	public String changePassword(@RequestParam String oldPass, @RequestParam String newPass, Principal principal,
			HttpSession session) {
		System.out.println("old password entered by user: " + oldPass);

//		System.out.println("new password entered by user: " + newPass);
//		System.out.println("new password entered by user and encrypted: " + encoder.encode(newPass));
		String userName = principal.getName();
		SUser userByUsername = this.userRepo.getUserByUsername(userName);
		String oldPasswordFromDb = userByUsername.getPassword();
		System.out.println("Old password from the database: " + oldPasswordFromDb);

		if (encoder.matches(oldPass, oldPasswordFromDb)) {
			userByUsername.setPassword(encoder.encode(newPass));
			this.userRepo.save(userByUsername);
			session.setAttribute("message", new Message("Password changed successfully", "alert-success"));
		} else {
			session.setAttribute("message", new Message("Invalid Password, please try again", "alert-danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/settings";
	}

}