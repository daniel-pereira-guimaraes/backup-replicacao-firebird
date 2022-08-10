package modelo;

public class Monitor extends Cadastro {
	
	private String chave;
	private String controle;
	
	public Monitor() {
		super();
	}
	
	public Monitor(Monitor monitor) {
		super(monitor);
		this.chave = monitor.getChave();
	}
	
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getControle() {
		return controle;
	}
	
	public void setControle(String controle) {
		this.controle = controle;
	}
}
