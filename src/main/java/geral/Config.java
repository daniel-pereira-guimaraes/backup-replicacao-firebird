package geral;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Config {

	private static final String SERVIDOR_ID = "ServidorId";
	private static final String ENDERECO_GBAK = "enderecoGBak";
	private static final String LOTE_REPLICACAO = "loteReplicacao";
	private static final String INTERVALO_NOTIFICACAO = "intervaloNotificacao";
	private static final String DATA_HORA_NOTIFICACAO = "ultimaNotificacao";
	private static final String PAUSADO = "sistemaPausado";
	private static final String CONEXAO_USUARIO = "conexaoUsuario";
	private static final String CONEXAO_SENHA = "conexaoSenha";
	
	private static final File file = new File("config.xml");
	private static Properties properties = null;

	private static synchronized void verificaCarregar() throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		if (properties == null) {
			properties = new Properties();
			if (file.exists())
				properties.loadFromXML(new FileInputStream(file));
		}
	}
	
	private static synchronized String get(String nome) throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		verificaCarregar();
		return properties.getProperty(nome);
	}
	
	private static synchronized void put(String nome, String valor) throws FileNotFoundException, IOException {
		verificaCarregar();
		properties.put(nome, valor);
		properties.storeToXML(new FileOutputStream(file), null);
	}
	
	public static synchronized String getServidorId() throws FileNotFoundException, IOException {
		String s = get(SERVIDOR_ID);
		if (Util.isNullOrEmpty(s)) {
			s = Util.getUUID();
			put(SERVIDOR_ID, s);
		}
		return s;
	}
	
	public static synchronized String getEnderecoGBak() throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		return get(ENDERECO_GBAK);
	}
	
	public static synchronized void setEnderecoGBak(String endereco) throws FileNotFoundException, IOException {
		put(ENDERECO_GBAK, endereco);
	}
	
	public static synchronized int getLoteReplicacao() {
		int lote = 0;
		try {
			lote = Integer.parseInt(get(LOTE_REPLICACAO));
		} catch(Throwable ignore) {}
		return Math.max(lote, 10000);
	}
	
	public static synchronized void setLoteReplicacao(int loteReplicacao) throws FileNotFoundException, IOException {
		put(LOTE_REPLICACAO, String.valueOf(loteReplicacao));
	}
			
	public static synchronized int getIntervaloNotificacao() {
		int tempo = 0;
		try {
			tempo = Integer.parseInt(get(INTERVALO_NOTIFICACAO));
		} catch (Throwable ignore) {}
		return Math.max(tempo, 1000 * 60); 
	}
	
	public static synchronized void setIntervaloNotificacao(int intervalo) throws FileNotFoundException, IOException {
		put(INTERVALO_NOTIFICACAO, String.valueOf(intervalo));
	}
	
	public static synchronized long getDataHoraNotificacao() {
		long dataHora = 0;
		try {
			dataHora = Long.parseLong(get(DATA_HORA_NOTIFICACAO));
		} catch (Throwable ignore) {}
		return Math.max(dataHora, 0);
	}
	
	public static synchronized void setDataHoraNotificacao(long dataHora) throws FileNotFoundException, IOException {
		put(DATA_HORA_NOTIFICACAO, String.valueOf(dataHora));
	}
	
	public static synchronized boolean getSistemaPausado() {
		boolean sistemaPausado = false;
		try {
			sistemaPausado = Boolean.parseBoolean(get(PAUSADO));
		} catch (Throwable ignore) {}
		return sistemaPausado;
	}
	
	public static synchronized void setSistemaPausado(boolean sistemaPausado) throws FileNotFoundException, IOException {
		put(PAUSADO, String.valueOf(sistemaPausado));
	}
	
	public static synchronized String getConexaoUsuario() {
		String conexaoUsuario = null;
		try {
			conexaoUsuario = get(CONEXAO_USUARIO);
		} catch (Throwable ignore) {}
		return Util.isNullOrEmpty(conexaoUsuario) ? "SYSDBA" : conexaoUsuario;
	}
	
	public static synchronized void setConexaoUsuario(String conexaoUsuario) throws FileNotFoundException, IOException {
		put(CONEXAO_USUARIO, conexaoUsuario);
	}
	
	public static synchronized String getConexaoSenha() {
		String conexaoSenha = null;
		try {
			conexaoSenha = get(CONEXAO_SENHA);
		} catch (Throwable ignore) {}
		return Util.isNullOrEmpty(conexaoSenha) ? "masterkey" : conexaoSenha;
	}
	
	public static synchronized void setConexaoSenha(String conexaoSenha) throws FileNotFoundException, IOException {
		put(CONEXAO_SENHA, conexaoSenha);
	}
	
}
