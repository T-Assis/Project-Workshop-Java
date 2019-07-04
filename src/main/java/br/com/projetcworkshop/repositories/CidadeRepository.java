package br.com.projetcworkshop.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.projetcworkshop.domain.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT cidades FROM Cidade cidades WHERE cidades.estado.id = :estadoId ORDER BY cidades.nome")
	public List<Cidade> findCidadesByEstado(@Param("estadoId") Integer estadoId);
}
