package regra;

import java.io.File;
import java.util.List;

import backup.PastaBackup;
import dao.Conexao;
import dao.TarefaDAO;
import firebird.FirebirdBackup;
import firebird.FirebirdReplicador;
import geral.Config;
import geral.Constante;
import geral.Saida;
import geral.Util;
import modelo.BancoDados;
import modelo.Notificacao;
import modelo.Pasta;
import modelo.Tarefa;
import modelo.TarefaPendente;

public class TarefaRN {
	
	public static final Tarefa grava(Tarefa tarefa) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaDAO(conexao).grava(tarefa);
		} finally {
			conexao.desconecta();
		}
		return tarefa;
	}
	
	public static final void exclui(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaDAO(conexao).exclui(id);
		} finally {
			conexao.desconecta();
		}
	}

	public static final List<Tarefa> buscaPorNome(String nome) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new TarefaDAO(conexao).buscaPorNome(nome);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static final Tarefa buscaPorId(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new TarefaDAO(conexao).buscaPorId(id);
		} finally {
			conexao.desconecta();
		}
	}
		
	public static final List<Notificacao> buscaNotificacoes(int monitorId) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new TarefaDAO(conexao).buscaNotificacoes(monitorId);
		} finally {
			conexao.desconecta();
		}
	}
	
	private static final List<TarefaPendente> buscaTarefasPendentes() throws Exception {
		List<TarefaPendente> lista = null;
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			conexao.iniciaTransacao();
			try {
				lista = new TarefaDAO(conexao).buscaTarefasPendentes();
				conexao.confirmaTransacao();
			} catch(Throwable e) {
				conexao.cancelaTransacao();
				throw e;
			}
		} finally {
			conexao.desconecta();
		}
		return lista;
	}
	
	private static final void atualizaDataHoraExecutada(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaDAO(conexao).atualizaDataHoraExecutada(id);
		} finally {
			conexao.desconecta();
		}
	}
	
	private static final void executaTarefaBackup(TarefaPendente tarefa) throws Exception {
		BancoDados bancoDados = tarefa.getBancoDados();
		File fileFbk = new File(Util.getTempDir() + bancoDados.getNomeBackup());
		File fileZip = new File(fileFbk.getAbsolutePath() + ".zip");
		try {
			Saida.escreve(Saida.Tipo.NORMAL, String.format(
				Constante.STR_BKP_PREPARANDO, bancoDados.getNome()));
			FirebirdBackup firebirdBackup = new FirebirdBackup(Config.getEnderecoGBak());
			firebirdBackup.fazBackup(bancoDados, fileFbk.getAbsolutePath());
			Saida.escreve(Saida.Tipo.NORMAL, String.format(
				Constante.STR_BKP_COMPACTANDO, bancoDados.getNome()));
			Util.zip(fileFbk, fileZip);
			for (TarefaPendente.TarefaBackup tarefaBackup : tarefa.getDestinosBackup()) {
				try {
					Pasta pasta = tarefaBackup.getPasta();
					Saida.escreve(Saida.Tipo.NORMAL, String.format(
						Constante.STR_BKP_ENVIANDO, bancoDados.getNome(), pasta.getNome()));
					PastaBackup pastaBackup = PastaBackup.getInstance(pasta);
					pastaBackup.conecta();
					try {
						pastaBackup.gravaArquivo(fileZip);
						Saida.escreve(Saida.Tipo.SUCESSO, String.format(
							Constante.STR_BKP_ENVIADO, bancoDados.getNome(), pasta.getNome()));
						pastaBackup.excluiArquivosAntigos(bancoDados.getFiltroBackup() , tarefaBackup.getQtdManter());
					} finally {
						pastaBackup.desconecta();
					}
					if (tarefaBackup.getMensagem() != null) {
						tarefaBackup.setMensagem(null);
						TarefaBackupRN.gravaMensagem(tarefaBackup);
					}
				} catch(Throwable e) {
					String mensagem = e.getMessage();
					Saida.escreve(Saida.Tipo.ERRO, mensagem);
					if (!Util.equals(tarefaBackup.getMensagem(), mensagem)) {
						tarefaBackup.setMensagem(mensagem);
						TarefaBackupRN.gravaMensagem(tarefaBackup);
					}
				}
			}
		} finally {
			fileFbk.delete();
			fileZip.delete();
		}
	}
	
	private static final void executaTarefaReplicacao(TarefaPendente tarefa) throws Exception {
		String enderecoGBak = Config.getEnderecoGBak();
		BancoDados bdOrigem = tarefa.getBancoDados();
		for (TarefaPendente.TarefaReplicacao tarefaReplicacao : tarefa.getDestinosReplicacao()) {
			try {
				BancoDados bdDestino = tarefaReplicacao.getBancoDados();
				new FirebirdReplicador(bdOrigem, bdDestino, enderecoGBak).replica(Config.getLoteReplicacao());
				if (tarefaReplicacao.getMensagem() != null) {
					tarefaReplicacao.setMensagem(null);
					TarefaReplicacaoRN.gravaMensagem(tarefaReplicacao);
				}
			} catch(Throwable e) {
				String mensagem = e.getMessage();
				Saida.escreve(Saida.Tipo.ERRO, mensagem);
				if (!Util.equals(tarefaReplicacao.getMensagem(), mensagem)) {
					tarefaReplicacao.setMensagem(mensagem);
					TarefaReplicacaoRN.gravaMensagem(tarefaReplicacao);
				}
			}
		}
	}
	
	public static final void verificaTarefasPendentes() {
		try {
			List<TarefaPendente> tarefas = buscaTarefasPendentes();
			for (TarefaPendente tarefa : tarefas) {
				try {
					if (tarefa.getTipo() == Tarefa.Tipo.BACKUP)
						executaTarefaBackup(tarefa);
					else
						executaTarefaReplicacao(tarefa);
				} catch(Throwable e) {
					Saida.escreve(Saida.Tipo.ERRO, e.getMessage());
					if (tarefa.getTipo() == Tarefa.Tipo.BACKUP)
						TarefaBackupRN.gravaMensagem(tarefa, e.getMessage());
					else
						TarefaReplicacaoRN.gravaMensagem(tarefa, e.getMessage());
				} finally {
					atualizaDataHoraExecutada(tarefa.getId());
				}
			}
		} catch(Throwable e) {
			Saida.escreve(Saida.Tipo.ERRO, e.getMessage());
		}
	}

}
