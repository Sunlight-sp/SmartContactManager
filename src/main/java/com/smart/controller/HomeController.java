package com.smart.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Signup - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	//handler for registering user
	@RequestMapping(value="/do-register", method=RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user,
			@RequestParam(name="agreement", defaultValue = "false") boolean agreement,
			Model model,
			HttpSession session) {

		try {
			
			if(!agreement) {
				System.out.println("Not agreed to terms and conditions");
				throw new Exception("Not agreed to terms and conditions");
			}
			user.setEnabled(true);
			user.setRole("ROLE_USER");
			
			System.out.println("agreement "+ agreement);
			System.out.println("user "+user.toString());
			User result = userRepository.save(user);
			model.addAttribute("user", new User());
			
			session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
			return "signup";
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong - "+e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
}
