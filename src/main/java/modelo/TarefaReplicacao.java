package modelo;

public class TarefaReplicacao extends TarefaDestino {
	
	private Cadastro bancoDados = new Cadastro();

	public TarefaReplicacao() {
		super();
	}
	
	public TarefaReplicacao(TarefaReplicacao tarefaReplicacao) {
		super(tarefaReplicacao);
		this.bancoDados.setId(tarefaReplicacao.getBancoDados().getId());
		this.bancoDados.setNome(tarefaReplicacao.getBancoDados().getNome());
	}
	
	public Cadastro getBancoDados() {
		return bancoDados;
	}
	
}
