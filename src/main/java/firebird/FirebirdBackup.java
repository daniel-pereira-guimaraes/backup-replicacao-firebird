package firebird;

import java.io.File;

import geral.Constante;
import geral.Processo;
import modelo.BancoDados;

public class FirebirdBackup {

	private final String aspas = "\"";
	private final String prefixoTemp = "GBR_Backup_";
	private final String sufixoTemp = ".temp";
	private final File dirTemp = new File(System.getProperty("java.io.tmpdir"));
	
	private String enderecoGbak;
	
	public FirebirdBackup(String enderecoGbak) {
		super();
		this.enderecoGbak = enderecoGbak;
	}

	public String getEnderecoGbak() {
		return enderecoGbak;
	}

	public void setEnderecoGbak(String enderecoGbak) {
		this.enderecoGbak = enderecoGbak;
	}

	private String comandoBackup(BancoDados bd, String saida) {
		return enderecoGbak
			+ " -user " + bd.getUsuario() 
			+ " -password " + bd.getSenha()
			+ " -b " + bd.getMaquina() + "/" + bd.getPorta() + ":" + bd.getEndereco()
			+ " " + saida;  
	}
	
	private String comandoRestauracao(BancoDados bd, String entrada) {
		return enderecoGbak
			+ " -user " + bd.getUsuario() 
			+ " -password " + bd.getSenha()
			+ " -r " + entrada
			+ " " + bd.getMaquina()	+ "/" + bd.getPorta() + ":" + bd.getEndereco();
	}
	
	private void executaComando(String comando, String msgErro) throws Exception {
		Processo.Retorno retorno = new Processo(comando).executaAguarda();
		if (retorno.getCodigo() != 0) {
			if (retorno.getErro() != null)
				msgErro += "\n" + retorno.getErro();
			throw new Exception(msgErro);
		}
	}
	
	public void fazBackup(BancoDados bancoDados, String enderecoBackup) throws Exception {
		String comando = comandoBackup(bancoDados, aspas + enderecoBackup + aspas);
		try {
			executaComando(comando, Constante.STR_ERRO_FAZER_BACKUP);
		} catch(Throwable e) {
			new File(enderecoBackup).delete();
			throw e;
		}
	}

	public void restauraBackup(BancoDados bancoDados, String enderecoBackup) throws Exception {
		String comando = comandoRestauracao(bancoDados, aspas + enderecoBackup + aspas);
		executaComando(comando, Constante.STR_ERRO_RESTAURAR_BACKUP);
	}
	
	public void clonaBancoDados(BancoDados origem, BancoDados destino) throws Exception {
		File temp = File.createTempFile(prefixoTemp, sufixoTemp, dirTemp);
		try {
			fazBackup(origem, temp.getAbsolutePath());
			restauraBackup(destino, temp.getAbsolutePath());
		} finally {
			temp.delete();
		}
	}
	
}
