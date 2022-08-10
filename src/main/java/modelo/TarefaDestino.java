package modelo;

import geral.Constante;
import geral.Util;

public abstract class TarefaDestino extends TarefaDetalhe {
	
	public static enum Estado {
		NORMAL(1), PAUSADO(2), INOPERANTE(3);
		
		private int id;
		
		Estado(int id) {
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}
		
		static public Estado fromId(int id) {
			for (Estado estado : Estado.values())
				if (estado.getId() == id)
					return estado;
			return null;
		}
		
		public String toString() {
			switch(this) {
				case NORMAL: return Constante.STR_NORMAL;
				case PAUSADO: return Constante.STR_PAUSADA;
				default: return null;
			}
		}
	}
	
	private Estado estado = Estado.NORMAL;
	private String mensagem;
		
	public TarefaDestino() {
		super();
	}
	
	public TarefaDestino(TarefaDestino tarefaDestino) {
		super(tarefaDestino);
		this.estado = tarefaDestino.getEstado();
		this.mensagem = tarefaDestino.getMensagem();
	}
	
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public String getDescricaoEstado() {
		if (estado == Estado.PAUSADO)
			return Constante.STR_PAUSADA;
		else if (!Util.isNullOrEmpty(mensagem))
			return Constante.STR_INOPERANTE;
		else
			return Constante.STR_NORMAL;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}
