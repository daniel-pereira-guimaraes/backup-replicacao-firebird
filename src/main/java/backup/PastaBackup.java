package backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import modelo.Pasta;

public abstract class PastaBackup {
	
	public static PastaBackup getInstance(Pasta pasta) {
		if (pasta.getTipo() == Pasta.Tipo.LOCAL)
			return new PastaLocal(
				pasta.getEndereco());
		else
			return new PastaFTP(
				pasta.getMaquina(), 
				pasta.getPorta(),
				pasta.getUsuario(),
				pasta.getSenha(),
				pasta.getEndereco());
	}
	
	public void conecta() throws Exception {}
	public void desconecta() {};
	public abstract void gravaArquivo(String nome, InputStream conteudo) throws Exception;
	public abstract List<Arquivo> listaArquivos(String filtro) throws Exception;
	public abstract void excluiArquivo(String nome) throws Exception;
	
	public void gravaArquivo(File file) throws FileNotFoundException, Exception {
		try(FileInputStream fis = new FileInputStream(file)) {
			gravaArquivo(file.getName(), fis);
		}
	}	
	
	public void excluiArquivosAntigos(String filtro, int qtdManter) throws Exception {
		List<Arquivo> arquivos = listaArquivos(filtro);
		Collections.sort(arquivos);
		while (arquivos.size() > qtdManter) {
			excluiArquivo(arquivos.get(0).getNome());
			arquivos.remove(0);
		}
	}
	
}
