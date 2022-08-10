package backup;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.*;

import geral.Constante;
import geral.Util;

public class PastaFTP extends PastaBackup {
	
	private final String delimitador = "/";
	private String maquina;
	private int porta;
	private String usuario;
	private String senha;
	private String endereco;
	
	private FTPClient ftpClient = new FTPClient();

	public PastaFTP(String maquina, int porta, String usuario, String senha, String endereco) {
		super();
		this.maquina = maquina;
		this.porta = porta;
		this.usuario = usuario;
		this.senha = senha;
		this.endereco = endereco;
		if (!this.endereco.endsWith(delimitador))
			this.endereco = this.endereco + delimitador;
	}

	private void verificaRetorno() throws Exception {
		int codigo = ftpClient.getReplyCode();
		if (codigo < 200 || codigo > 299) {
			String mensagem = ftpClient.getReplyString();
			if (Util.isNullOrEmpty(mensagem))
				mensagem = Constante.STR_ERRO_DESCONHECIDO;
			throw new Exception(String.format(Constante.STR_FALHA_FTP,
				maquina, porta, endereco, mensagem));
		}
	}
	
	@Override
	public void conecta() throws  Exception {
		try {
			/*
			ftpClient.setDefaultTimeout(2000);
			ftpClient.setConnectTimeout(2000);
			ftpClient.setDataTimeout(2000);
			*/
			ftpClient.connect(maquina, porta);
			try {
				ftpClient.login(usuario, senha);
				verificaRetorno();
			} catch (Throwable e) {
				desconecta();
				throw e;
			}
		} catch(UnknownHostException e) {
			throw new Exception(String.format(Constante.STR_MAQUINA_DESCONHECIDA, maquina));
		}
	}

	@Override
	public void desconecta() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch(Throwable ignora) {}
		}
	}

	@Override
	public void gravaArquivo(String nome, InputStream conteudo) throws Exception {
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.storeFile(endereco + nome, conteudo);
        verificaRetorno();
	}

	@Override
	public void excluiArquivo(String nome) throws Exception {
		ftpClient.deleteFile(endereco + nome);
		verificaRetorno();
	}

	@Override
	public List<Arquivo> listaArquivos(String filtro) throws Exception {
		List<Arquivo> arquivos = new ArrayList<Arquivo>();
        FTPFile[] files = ftpClient.listFiles(endereco);
        if (files != null) {
        	for (FTPFile file : files) {
                if (file.isFile()) {
    				String nome = file.getName();
    				if (filtro == null || nome.matches(filtro)) {
    					arquivos.add(new Arquivo(nome,
    						Instant.ofEpochMilli(file.getTimestamp().getTimeInMillis())));
    				}
                }
            }
        }
		return arquivos;
	}

}
