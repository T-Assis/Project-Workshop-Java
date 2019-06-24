package br.com.projetcworkshop.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projetcworkshop.security.JWTUtil;
import br.com.projetcworkshop.security.UserSpringSecurity;
import br.com.projetcworkshop.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResouce {
	
	@Autowired
	JWTUtil JWTUtil;
	
	@PostMapping(value = "/refresh_token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSpringSecurity user = UserService.authenticated();
		String token = JWTUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}
	
}
