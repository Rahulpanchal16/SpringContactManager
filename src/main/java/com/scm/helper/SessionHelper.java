package com.scm.helper;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {
	@SuppressWarnings("null")
	public void messageRemoveHelper() {
		try {

			// System.out.println("Removing session attribute");
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
					.getSession();
			session.removeAttribute("message");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
