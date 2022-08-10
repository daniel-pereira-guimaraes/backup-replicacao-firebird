package geral;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.InvalidPropertiesFormatException;

import javax.swing.JOptionPane;

import dao.Conexao;
import gui.FormConfig;
import gui.FormPrincipal;

public class Principal {

	private static class Trava {
		private File dir = new File(System.getProperty("user.home"));
		private File file = new File(dir, "GBR.bloqueio");
		private RandomAccessFile randomAccessFile = null;
	    private FileChannel fileChannel = null;
	    private FileLock fileLock = null;

	    private void criaArquivo() throws Exception {
	        if (!file.exists()) {
	            try {
	                file.createNewFile();
	            } catch (Throwable e) {
	                throw new Exception(String.format(Constante.STR_ERRO_CRIAR_ARQUIVO, 
	                	file.getAbsoluteFile(), e.getMessage()));
	            }
	        }
	    }

	    private void abreArquivo() throws Exception {
	        try {
	            randomAccessFile = new RandomAccessFile(file, "rw");
				fileChannel = randomAccessFile.getChannel();
	        } catch (Throwable e) {
	            throw new Exception(String.format(Constante.STR_ERRO_ACESSAR_ARQUIVO,
	            	file.getAbsoluteFile(), e.getMessage()));
	        }
	    }

	    private void travaArquivo() {
	        try {
	        	fileLock = fileChannel.tryLock();
	        } catch (Throwable ignora) {}
	    }

	    public Trava() throws Exception {
	    	criaArquivo();
	    	abreArquivo();
	    	travaArquivo();
	    }
	    
	    public boolean travado() {
	    	return fileLock != null;
	    }
	    
	    public void finaliza() {
	    	if (fileLock != null) 
	    		try { fileLock.close();	} catch(Throwable ignore) {}
	    	if (fileChannel != null)
	    		try { fileChannel.close(); } catch(Throwable ignore) {}
	    	if (randomAccessFile != null)
	    		try { randomAccessFile.close(); } catch(Throwable ignore) {}
	    	if (file != null && file.exists())
	    		file.delete();
	    }	    
	}

	private static Trava trava;
			
	private static void shutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (trava != null) {
					if (!Config.getSistemaPausado())
						Monitoramento.enviaTarefas(true, true);
					trava.finaliza();
				}
			}
		});
	}
	
	/*
	private static void despreparaOrigem() throws java.io.FileNotFoundException, Exception {
		modelo.BancoDados bd = new modelo.BancoDados();
		
		for (int i = 0; i < 2; i++) {
			
			if (i == 0) {
				bd.setMaquina("192.168.1.7");
				bd.setEndereco("C:\\Tecnobyte\\SisTec\\Dados.fdb");
				bd.setSenha("numkavy");
			} else {
				bd.setMaquina("localhost");
				bd.setEndereco("C:\\teste_fb3\\SacPlus.fdb");
				bd.setSenha("senhaTeste");
			}
			bd.setPorta(30530);
			bd.setCaracteres("ISO8859_1");
			bd.setUsuario("SYSDBA");
			firebird.FirebirdConexao conexao = new firebird.FirebirdConexao(bd);
			conexao.conecta();
			try {
				System.out.println("Despreparando..." + bd.getUrl());
				conexao.executaScript(Util.getResourceAsString("despreparaOrigem.sql"));
			} finally {
				conexao.desconecta();
			}
			System.out.println("Pronto!");
		}
		
	}
	*/
			
	public static boolean testaConexaoLocal() throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		boolean tentarConexao = true;
		while (tentarConexao) {
			try {
				Conexao conexao = Conexao.getConexao();
				conexao.conecta();
				conexao.desconecta();
				return true;
			} catch (Throwable e) {
				Dialogo.erro(e.getMessage());
				FormConfig formConfig = new FormConfig();
				try {
					tentarConexao = formConfig.exibeModal() == JOptionPane.OK_OPTION;
				} finally {
					formConfig.dispose();
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		try {
			//despreparaOrigem();
			
			shutdownHook();
			trava = new Trava();
			if (trava.travado()) {
				if (testaConexaoLocal())
					new FormPrincipal().setVisible(true);
				else
					System.exit(0);
			} else
				throw new Exception(Constante.STR_PROGRAMA_JA_ABERTO);
			
		} catch(Throwable e) {
			Dialogo.erro(e);
			System.exit(0);
		}
	}

}
