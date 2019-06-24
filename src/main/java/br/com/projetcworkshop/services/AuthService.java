package br.com.projetcworkshop.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.projetcworkshop.domain.Cliente;
import br.com.projetcworkshop.repositories.ClienteRepository;
import br.com.projetcworkshop.services.exception.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("E-mail não encontrado");
		}
		String newPassword = newPassword();
		cliente.setSenha(bCryptPasswordEncoder.encode(newPassword));
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPassword);
	}

	private String newPassword() {
		char[] vector = new char[15];
		for (int i = 0; i < vector.length; i++) {
			vector[i] = randomChar();
		}
		return new String(vector);
	}

	private char randomChar() {
		int option = random.nextInt(3);
		
		if (option == 0)
			return (char) (random.nextInt(10) + 48); // 0... 9
		
		else if (option == 1)
			return (char) (random.nextInt(26) + 65); // A... Z
		
		else
			return (char) (random.nextInt(26) + 97); // a... z
	}
	
}
