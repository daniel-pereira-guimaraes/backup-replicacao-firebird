package geral;

import java.io.IOException;

public class Processo {
	
	private String comando;
	
	public class Retorno {
		
		private int codigo;
		private String saida;
		private String erro;

		public Retorno(int codigo, String saida, String erro) {
			this.codigo = codigo;
			this.saida = saida;
			this.erro = erro;
		}

		public int getCodigo() {
			return codigo;
		}

		public String getSaida() {
			return saida;
		}

		public String getErro() {
			return erro;
		}		
	}

	public Processo(String comando) {
		this.comando = comando;
	}
	
	public Retorno executaAguarda() throws IOException, InterruptedException {
	      Process p = Runtime.getRuntime().exec(comando);  
	      String saida = Util.getInputStreamAsString(p.getInputStream());
	      String erro = Util.getInputStreamAsString(p.getErrorStream());
	      p.waitFor();  
	      return new Retorno(p.exitValue(), saida, erro); 
	}
	
}
