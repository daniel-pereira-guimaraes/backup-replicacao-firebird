package modelo;

public class TarefaBackup extends TarefaDestino {
	
	private Cadastro pasta = new Pasta();
	private int qtdManter = 100;
	
	public TarefaBackup() {
		super();
	}
	
	public TarefaBackup(TarefaBackup tarefaBackup) {
		super(tarefaBackup);
		this.pasta.setId(tarefaBackup.getPasta().getId());
		this.pasta.setNome(tarefaBackup.getPasta().getNome());
		this.qtdManter = tarefaBackup.getQtdManter();
	}
	
	public Cadastro getPasta() {
		return pasta;
	}

	public int getQtdManter() {
		return qtdManter;
	}

	public void setQtdManter(int qtdManter) {
		this.qtdManter = qtdManter;
	}

}
