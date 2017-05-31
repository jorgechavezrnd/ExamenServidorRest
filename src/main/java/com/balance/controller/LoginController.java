package com.balance.controller;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.balance.Mail.SmtpMailSender;
import com.balance.model.Terminal;
import com.balance.model.Token;
import com.balance.service.TerminalService;
import com.balance.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.balance.model.User;
import com.balance.service.UserService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Set;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	private TerminalService terminalService;
	private TokenService tokenService;

	@Autowired
	public void setTokenService(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Autowired
	public void setTerminalService(TerminalService terminalService){
		this.terminalService=terminalService;
	}

	@Autowired
	private SmtpMailSender smtpMailSender;

	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public String login(){
		return "login";
	}

	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public String registration(Model model){
		model.addAttribute("user", new User());
		Iterable<Terminal> listaterminals=terminalService.listAllTerminals();
		ArrayList<Terminal> resp=new ArrayList<>();
		for (Terminal t: listaterminals){
			if(!t.isActive()){
				resp.add(t);
			}
		}
		model.addAttribute("terminals",resp);
		return "registration";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "There is already a user registered with the email provided");
		}
		if (!bindingResult.hasErrors()) {
			userService.saveUser(user);
			model.addAttribute("successMessage", "El usuario se registro correctamente");
			model.addAttribute("user", new User());
		}
		return "registration";
	}

	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public String home(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		if (user == null) {
			return "redirect:/";
		}
		model.addAttribute("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		model.addAttribute("adminMessage","Content Available Only for Users with Admin Role");
		model.addAttribute("userList", userService.listAllUsers());
		model.addAttribute("user", user);
		return "admin/home";
	}

	@RequestMapping(value="/user/home", method = RequestMethod.GET)
	public String homeExclusive(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		model.addAttribute("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		model.addAttribute("userMessage","Content Available Only for Users with Limited Role");
		model.addAttribute("user", user);
		return "user/home";
	}

	@RequestMapping(value="/default", method = RequestMethod.GET)
	public String defaultAfterLogin()
	{
		Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		if (roles.contains("ADMIN")) {
			return "redirect:/admin/home";
		}
		return "redirect:/user/home";
	}

	@RequestMapping(value = "/access-denied", method = RequestMethod.GET)
	public String accesoDenegado() {
		return "access-denied"; //Solo devuelve un mensaje
	}

	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public String listUsers(Model model) {
		model.addAttribute("Users",userService.listAllUsers());
		return "users";
	}

	//Email recover password
	@RequestMapping(value="/forgot", method = RequestMethod.GET)
	public String forgotpassword(){
		return "forgot";
	}

	@RequestMapping(value="/send-mail", method = RequestMethod.GET)
	public String sendMail(HttpServletRequest request) throws MessagingException,ServletException {
		String text1= request.getParameter("email");
		if(userService.findUserByEmail(request.getParameter("email"))!=null){
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[20];
			random.nextBytes(bytes);
			String token = bytes.toString();
			User user=userService.findUserByEmail(request.getParameter("email"));
			Token t=new Token(token);
			t.setUser(userService.findUserByEmail(request.getParameter("email")));
			tokenService.saveToken(t);
			smtpMailSender.send(text1, "Balance Fitness Tracker: Recover your password", "Token password: "+token);
			return "redirect:changepassword";
		}

		return "redirect:/forgot";
	}
	@RequestMapping(value="/changepassword", method = RequestMethod.GET)
	public String changepassword() {
		return "changepassword";
	}
	@RequestMapping(value="/changepasswordyes", method = RequestMethod.GET)
	public String changepassword1(String email,String password,String token) {
		User userExists = userService.findUserByEmail(email);
		if (userExists != null) {
			userExists.setPassword(password);
			if(tokenService.findByToken(token).getActive().equals(true) && tokenService.findByToken(token).getUser().equals(userExists)){
				tokenService.findByToken(token).setActive(false);
				tokenService.saveToken(tokenService.findByToken(token));
				return "redirect:/";
			}

		}
		return "redirect:changepassword";
	}
}