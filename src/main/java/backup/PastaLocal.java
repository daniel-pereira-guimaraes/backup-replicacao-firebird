package backup;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import geral.Constante;

public class PastaLocal extends PastaBackup {
	
	private String endereco;
	
	public PastaLocal(String endereco) {
		this.endereco = endereco;
		if (!this.endereco.endsWith(File.separator))
			this.endereco = this.endereco + File.separator;
	}

	@Override
	public void gravaArquivo(String nome, InputStream conteudo) throws Exception {
		try {
			Files.copy(conteudo, new File(endereco + nome).toPath(), 
			 StandardCopyOption.REPLACE_EXISTING);
		} catch(NoSuchFileException e) {
			throw new Exception(String.format(Constante.STR_PASTA_NAO_ENCONTRADA, endereco));
		} catch(FileSystemException e) {
			throw new Exception(e.getReason() + endereco);
		}
	}

	@Override
	public void excluiArquivo(String nome) throws Exception {
		Files.delete(new File(endereco + nome).toPath());
	}

	@Override
	public List<Arquivo> listaArquivos(String filtro) throws Exception {
		List<Arquivo> arquivos = new ArrayList<Arquivo>();
		File[] files = new File(endereco).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String nome = file.getName();
				if (filtro == null || nome.matches(filtro)) {
					arquivos.add(new Arquivo(nome,
						Instant.ofEpochMilli(file.lastModified())));
				}
			}
		}
		return arquivos;
	}

}
