package modelo;

import geral.Constante;

public class Pasta extends Servico {
	
	public static enum Tipo { 
		LOCAL(1), FTP(2);
		
		private Integer id;
		
		Tipo(Integer id) {
			this.id = id;
		}
		
		public Integer getId() {
			return this.id;
		}

		@Override
		public String toString() {
			if (this == Tipo.LOCAL)
				return Constante.STR_LOCAL;
			else
				return Constante.STR_FTP;
		}
		
		public static Tipo fromId(Integer id) {
			for (Tipo tipo : Tipo.values()) {
				if (tipo.getId().equals(id)) 
					return tipo;
			}
			return null;
		}
		
		public static Tipo fromString(String s) {
			for (Tipo tipo : Tipo.values()) {
				if (tipo.toString().equals(s))
					return tipo;
			}
			return null;
		}
	}
	
	private Tipo tipo;
	
	public Pasta() {
		super();
	}
	
	public Pasta(Pasta pasta) {
		super(pasta);
		this.tipo = pasta.getTipo();
	}
	
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
}
