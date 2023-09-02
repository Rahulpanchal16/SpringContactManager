package com.scm.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.dao.SUserRepository;
import com.scm.helper.EmailSenderService;
import com.scm.helper.Message;
import com.scm.models.SUser;

@Controller
public class ForgotPasswordController {

	@Autowired
	private SUserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private EmailSenderService emailSender;

	@GetMapping(path = "/forgot-password")
	public String openEmailForm() {
		return "/forgot_password_form";
	}

	@PostMapping(path = "/forgot-password-process")
	public String sendOtp(@RequestParam String email, HttpSession session) {

		// generating a six digit random number which will be our otp
		long otp = Math.round(Math.random() * 1000000);
		String text = "Hi there,\r\n" + "\r\n" + "Your OTP for resetting your password on Spring Contact Manager is "
				+ otp + ". The code will be valid for 5 minutes only.";

		String subject = "Your password reset OTP - Spring Contact Manager";
		emailSender.sendEmail(email, subject, text);
		session.setAttribute("email", email);
		session.setAttribute("myotp", otp);

		return "/verify_otp";
	}

	@PostMapping(path = "/otp-processing")
	public String otpProcessing(@RequestParam long formotp, HttpSession session) {
		long myotp = (long) session.getAttribute("myotp");
		String mail = (String) session.getAttribute("email");
		if (formotp == myotp) {
			System.out.println("Otp has been verified successfully");
			session.setAttribute("message", new Message("Otp verified successfully", "alert-success"));
			session.setAttribute("email", mail);
			return "/reset_password";
		} else {
			session.setAttribute("message", new Message("Invalid Otp, please try again", "alert-danger"));
			return "redirect:/signin";
		}
	}

	@PostMapping(path = "/password-reset-processing")
	public String resettingPassword(@RequestParam String password, HttpSession session) {
		String email = (String) session.getAttribute("email");
		SUser userByUsername = this.userRepo.getUserByUsername(email);
		userByUsername.setPassword(encoder.encode(password));
		this.userRepo.save(userByUsername);
		session.setAttribute("message",
				new Message("Password successfully changed, please login to access your account", "alert-success"));
		return "redirect:/signin";
	}

}
