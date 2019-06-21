package br.com.projetcworkshop.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetcworkshop.domain.Cliente;
import br.com.projetcworkshop.domain.ItemPedido;
import br.com.projetcworkshop.domain.PagamentoBoleto;
import br.com.projetcworkshop.domain.Pedido;
import br.com.projetcworkshop.domain.enums.StatusPagamento;
import br.com.projetcworkshop.repositories.ItemPedidoRepository;
import br.com.projetcworkshop.repositories.PagamentoRepository;
import br.com.projetcworkshop.repositories.PedidoRepository;
import br.com.projetcworkshop.security.UserSpringSecurity;
import br.com.projetcworkshop.services.exception.AuthorizationException;
import br.com.projetcworkshop.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public List<Pedido> findAll() {
		return repository.findAll();
	}
	
	public Pedido findById(Integer id) {
		Optional<Pedido> pedido = repository.findById(id);
		return pedido.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.findById(pedido.getCliente().getId()));
		pedido.getPagamento().setStatusPagamento(StatusPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		
		if (pedido.getPagamento() instanceof PagamentoBoleto) {
			PagamentoBoleto pagto = (PagamentoBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoBoleto(pagto, pedido.getInstante());
		}
		pedido = repository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		
		for (ItemPedido itemPedido : pedido.getItens()) {
			itemPedido.setDesconto(0.0);
			itemPedido.setProduto(produtoService.findById(itemPedido.getProduto().getId()));
			itemPedido.setPreco(produtoService.findById(itemPedido.getProduto().getId()).getPreco());
			itemPedido.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		emailService.sendOrderConfirmationEmail(pedido);
		return pedido;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSpringSecurity userSS = UserService.authenticated();
		if (userSS == null) {
			throw new AuthorizationException("Acesso Negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.findById(userSS.getId());
		return repository.findByCliente(cliente, pageRequest);
	}
	

}
