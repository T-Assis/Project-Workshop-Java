package br.com.projetcworkshop.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.projetcworkshop.domain.Cliente;
import br.com.projetcworkshop.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage simpleMailMessage);

	void sendNewPasswordEmail(Cliente cliente, String newPassword);
}
