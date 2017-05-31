package com.balance.service;

import java.util.Arrays;
import java.util.HashSet;

import com.balance.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.balance.model.Role;
import com.balance.model.User;
import com.balance.repository.RoleRepository;
import com.balance.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private TerminalRepository terminalRepository;

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(true);
		Role userRole = roleRepository.findByRole("ADMIN");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		terminalRepository.findOne(user.getTerminal().getId()).setActive(true);
		userRepository.save(user);
	}


	@Override
	public User getUserById(Integer id) {
		return userRepository.findOne(id);
	}

	@Override
	public void deleteUser(Integer id) {
		userRepository.delete(id);
	}
	@Override
	public Iterable<User> listAllUsers(){
		return userRepository.findAll();
	}

	@Override
	public void saveUserEdited(User user){
		terminalRepository.findOne(user.getTerminal().getId()).setActive(true);
		userRepository.save(user);

	}
}