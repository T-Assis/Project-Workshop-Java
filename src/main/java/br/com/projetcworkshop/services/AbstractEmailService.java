package br.com.projetcworkshop.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import br.com.projetcworkshop.domain.Cliente;
import br.com.projetcworkshop.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido pedido) {
		SimpleMailMessage simpleMailMessage = prepareSimpleMailMessageFromPedido(pedido); 
		sendEmail(simpleMailMessage);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		simpleMailMessage.setTo(pedido.getCliente().getEmail());
		simpleMailMessage.setFrom(sender);
		simpleMailMessage.setSubject("Pedido confirmado! Código: " + pedido.getId());
		simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
		simpleMailMessage.setText(pedido.toString());
		
		return simpleMailMessage;
	}
	
	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPassword) {
		SimpleMailMessage simpleMailMessage = prepareNewPasswordEmail(cliente, newPassword); 
		sendEmail(simpleMailMessage);
	}

	protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPassword) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		simpleMailMessage.setTo(cliente.getEmail());
		simpleMailMessage.setFrom(sender);
		simpleMailMessage.setSubject("Solicitação de nova senha!");
		simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
		simpleMailMessage.setText("Nova senha: " + newPassword);
		
		return simpleMailMessage;
	}
	
}
