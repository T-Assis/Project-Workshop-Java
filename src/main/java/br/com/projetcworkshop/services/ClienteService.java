package br.com.projetcworkshop.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.projetcworkshop.domain.Cidade;
import br.com.projetcworkshop.domain.Cliente;
import br.com.projetcworkshop.domain.Endereco;
import br.com.projetcworkshop.domain.enums.TipoCliente;
import br.com.projetcworkshop.dto.ClienteDTO;
import br.com.projetcworkshop.dto.ClienteNewDTO;
import br.com.projetcworkshop.repositories.ClienteRepository;
import br.com.projetcworkshop.repositories.EnderecoRepository;
import br.com.projetcworkshop.services.exception.DataIntegrityException;
import br.com.projetcworkshop.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Cliente findById(Integer id) {
		Optional<Cliente> cliente = repository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}

	@Transactional
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = repository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}
	
	public Cliente update(Cliente cliente) {
		Cliente newCliente = findById(cliente.getId());
		updateData(newCliente, cliente);
		return repository.save(newCliente);
	}

	public void delete(Integer id) {
		findById(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível deletar um Cliente que possuí Pedidos!");
		}
	}

	public Cliente fromDTO(ClienteDTO clienteDto) {
		return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO clienteNewDto) {
		Cliente cliente = new Cliente(null, clienteNewDto.getNome(), clienteNewDto.getEmail(), clienteNewDto.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDto.getTipoCliente()));
		Cidade cidade = new Cidade(clienteNewDto.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, clienteNewDto.getLogradouro(), clienteNewDto.getNumero(), clienteNewDto.getComplemento(), clienteNewDto.getBairro(), clienteNewDto.getCep(), cliente, cidade);
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteNewDto.getTelefone1());
		
		if (clienteNewDto.getTelefone2() != null) {
			cliente.getTelefones().add(clienteNewDto.getTelefone2());
		}
		
		if (clienteNewDto.getTelefone3() != null) {
			cliente.getTelefones().add(clienteNewDto.getTelefone3());
		}
		return cliente;
	}
	
	
	private void updateData(Cliente newCliente, Cliente cliente) {
		newCliente.setNome(cliente.getNome());
		newCliente.setEmail(cliente.getEmail());

	}

}
