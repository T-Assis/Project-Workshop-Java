package br.com.projetcworkshop.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projetcworkshop.domain.Cidade;
import br.com.projetcworkshop.domain.Estado;
import br.com.projetcworkshop.dto.CidadeDTO;
import br.com.projetcworkshop.dto.EstadoDTO;
import br.com.projetcworkshop.services.CidadeService;
import br.com.projetcworkshop.services.EstadoService;

@RestController
@RequestMapping(value="/estados")
public class EstadoResource {

	@Autowired
	private EstadoService service;
	
	@Autowired
	private CidadeService cidadeService;
	
	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAll(){
		List<Estado> listEstado = service.findAll();
		List<EstadoDTO> listEstadoDTO = listEstado.stream().map(estado -> new EstadoDTO(estado)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listEstadoDTO);
	}
	
	@GetMapping(value = "/{estadoId}/cidades")
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {
		List<Cidade> listCidades = cidadeService.findByEstado(estadoId);
		List<CidadeDTO> listCidadesDTO = listCidades.stream().map(cidade -> new CidadeDTO(cidade)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listCidadesDTO);
	}
}
