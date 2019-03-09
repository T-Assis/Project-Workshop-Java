package br.com.projetcworkshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.projetcworkshop.domain.Categoria;
import br.com.projetcworkshop.repositories.CategoriaRepository;
import br.com.projetcworkshop.services.exception.DataIntegrityException;
import br.com.projetcworkshop.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public List<Categoria> findAll() {
		return repository.findAll();
	}
	
	public Categoria findById(Integer id) {
		Optional<Categoria> categoria = repository.findById(id);
		return categoria.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id));
	}
	
	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return repository.save(categoria);
	}

	public Categoria update(Categoria categoria) {
		findById(categoria.getId());
		return repository.save(categoria);	
	}
	
	public void delete(Integer id) {
		findById(id);
		try {
			repository.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível deletar uma Categoria que possuí Produtos!");
		}
	}
	
}
