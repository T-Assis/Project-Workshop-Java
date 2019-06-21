package br.com.projetcworkshop.services;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.projetcworkshop.security.UserSpringSecurity;

public class UserService {
	
	public static UserSpringSecurity authenticated() {
		try {
			return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
