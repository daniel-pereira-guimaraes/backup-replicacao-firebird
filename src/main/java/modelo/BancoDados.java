package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BancoDados extends Servico {

	private String caracteres;
	
	public BancoDados() {
		super();
	}
	
	public BancoDados(BancoDados bancoDados) {
		super(bancoDados);
		this.caracteres = bancoDados.getCaracteres();
	}
	
	public String getCaracteres() {
		return caracteres;
	}

	public void setCaracteres(String caracteres) {
		this.caracteres = caracteres;
	}	
		
	public String getUrl() {
		return "jdbc:firebirdsql"
				+ ":" + getMaquina()
				+ "/" + getPorta()
				+ ":" + getEndereco()
				+ "?encoding=" + caracteres;
	}
	
	public String getNomeBackup() {
		return getNome() + " - " 
			+ DateTimeFormatter
				.ofPattern("yyyy-MM-dd - HH-mm-ss")
				.format(LocalDateTime.now()) 
			+ ".fbk";
	}
	
	public String getFiltroBackup() {
		return "(" + getNome() + " - )\\d{4}-\\d{2}-\\d{2} - \\d{2}-\\d{2}-\\d{2}(.fbk.zip)";
	}
}
