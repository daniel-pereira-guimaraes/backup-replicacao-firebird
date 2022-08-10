package modelo;

import java.util.ArrayList;
import java.util.List;

public class TarefaPendente extends Cadastro {
	
	private static class TarefaDestino extends Modelo {
		private String mensagem;
		
		public String getMensagem() {
			return mensagem;
		}
		
		public void setMensagem(String mensagem) {
			this.mensagem = mensagem;
		}
	}
	
	public static class TarefaReplicacao extends TarefaDestino {
		private BancoDados bancoDados = new BancoDados();

		public BancoDados getBancoDados() {
			return bancoDados;
		}
	}
		
	public static class TarefaBackup extends TarefaDestino {
		private Pasta pasta = new Pasta();
		private int qtdManter;

		public Pasta getPasta() {
			return pasta;
		}
		
		public int getQtdManter() {
			return qtdManter;
		}
		
		public void setQtdManter(int qtdManter) {
			this.qtdManter = qtdManter;
		}
	}
	
	private Tarefa.Tipo tipo;
	private BancoDados bancoDados = new BancoDados();
	private List<TarefaPendente.TarefaBackup> destinosBackup =
		new ArrayList<TarefaPendente.TarefaBackup>();
	private List<TarefaPendente.TarefaReplicacao> destinosReplicacao =
		new ArrayList<TarefaPendente.TarefaReplicacao>();
	
	public Tarefa.Tipo getTipo() {
		return tipo;
	}
	
	public void setTipo(Tarefa.Tipo tipo) {
		this.tipo = tipo;
	}
	
	public BancoDados getBancoDados() {
		return bancoDados;
	}

	public List<TarefaPendente.TarefaBackup> getDestinosBackup() {
		return destinosBackup;
	}

	public List<TarefaPendente.TarefaReplicacao> getDestinosReplicacao() {
		return destinosReplicacao;
	}
	
}
