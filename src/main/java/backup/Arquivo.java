package backup;

import java.time.Instant;

public class Arquivo implements Comparable<Arquivo> {
	
	private String nome;
	private Instant dataHora;
	
	public Arquivo(String nome, Instant dataHora) {
		super();
		this.nome = nome;
		this.dataHora = dataHora;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Instant getDataHora() {
		return dataHora;
	}
	
	public void setDataHora(Instant dataHora) {
		this.dataHora = dataHora;
	}

	public int compareTo(Arquivo outroArquivo) {
		return dataHora.compareTo(outroArquivo.getDataHora());
	}

}
