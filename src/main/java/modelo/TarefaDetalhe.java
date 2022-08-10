package modelo;

public abstract class TarefaDetalhe extends Modelo {
	
	private int tarefaId;

	public TarefaDetalhe() {
		super();
	}
	
	public TarefaDetalhe(TarefaDetalhe tarefaDetalhe) {
		super(tarefaDetalhe);
		this.tarefaId = tarefaDetalhe.getTarefaId();
	}
	
	public int getTarefaId() {
		return tarefaId;
	}

	public void setTarefaId(int tarefaId) {
		this.tarefaId = tarefaId;
	}

}
