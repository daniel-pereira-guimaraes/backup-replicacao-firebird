package modelo;

public class Notificacao  {
	
	private int tipo;
	private String origem;
	private String destino;
	private int estado;
	private String mensagem;
	private long dataHora = System.currentTimeMillis();
	
	public int getTipo() {
		return tipo;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public String getOrigem() {
		return origem;
	}
	
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	
	public String getDestino() {
		return destino;
	}
	
	public void setDestino(String destino) {
		this.destino = destino;
	}
	
	public int getEstado() {
		return estado;
	}
	
	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public long getDataHora() {
		return dataHora;
	}
	
	@Override
	public String toString() {
		return String.join(",", String.valueOf(tipo), 
			origem, destino, String.valueOf(estado), mensagem);
	}
		
}
