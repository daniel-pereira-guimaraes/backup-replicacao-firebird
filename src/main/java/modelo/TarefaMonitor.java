package modelo;

public class TarefaMonitor extends TarefaDetalhe {
	
	public class Monitor extends Cadastro { 
		private String chave;

		public String getChave() {
			return chave;
		}
		
		public void setChave(String chave) {
			this.chave = chave;
		}
	}
	
	private Monitor monitor = new Monitor();
	
	public TarefaMonitor() {
		super();
	}
	
	public TarefaMonitor(TarefaMonitor tarefaMonitor) {
		super(tarefaMonitor);
		this.monitor.setId(tarefaMonitor.getMonitor().getId());
		this.monitor.setNome(tarefaMonitor.getMonitor().getNome());
		this.monitor.setChave(tarefaMonitor.getMonitor().getChave());
	}
	
	public Monitor getMonitor() {
		return monitor;
	}
	
}
