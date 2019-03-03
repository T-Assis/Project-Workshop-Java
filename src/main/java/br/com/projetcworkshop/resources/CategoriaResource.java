package br.com.projetcworkshop.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projetcworkshop.domain.Categoria;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	
	@GetMapping
	public List<Categoria> listar() {
		List<Categoria> list = new ArrayList<Categoria>();
		list.add(new Categoria(1, "Eletrônicos"));
		return list;
	}
	
	

}
