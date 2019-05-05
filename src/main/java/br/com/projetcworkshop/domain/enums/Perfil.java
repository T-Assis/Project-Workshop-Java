package br.com.projetcworkshop.domain.enums;

public enum Perfil {
	
	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	private int codigo;
	private String descricao;
	
	private Perfil(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static Perfil toEnum(Integer codigo) {
		if (codigo == null) {
			return null;
		}
		
		for (Perfil statusPagamento : Perfil.values()) {
			if (codigo.equals(statusPagamento.getCodigo())) {
				return statusPagamento;
			}
		}
		throw new IllegalArgumentException("ID inválido para classe enum: " + codigo);
	}

}
