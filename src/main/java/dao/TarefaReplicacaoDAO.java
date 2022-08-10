package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import modelo.TarefaReplicacao;
import modelo.TarefaDestino;
import modelo.TarefaPendente;

public class TarefaReplicacaoDAO extends DAO {

	public TarefaReplicacaoDAO(Conexao conexao) {
		super(conexao);
	}
	
	public void grava(TarefaReplicacao tarefaReplicacao) throws Exception {
		PreparedStatement pstm = null;
		try {
			if (tarefaReplicacao.getId() == null) {
				pstm = getConexao().prepareStatement(
					"INSERT INTO TarefaReplicacao("
					+ "Tarefa_Id, BancoDados_Id, Estado) "
					+ "VALUES(?,?,?) RETURNING Id");
				pstm.setInt(1, tarefaReplicacao.getTarefaId());
				pstm.setInt(2, tarefaReplicacao.getBancoDados().getId());
				pstm.setInt(3, tarefaReplicacao.getEstado().getId());
				executaInsert(pstm, tarefaReplicacao);
			} else {
				pstm = getConexao().prepareStatement(
					"UPDATE TarefaReplicacao SET "
					+ "Tarefa_Id=?,"
					+ "BancoDados_Id=?,"
					+ "Estado=? "
					+ "WHERE Id=?");
				pstm.setInt(1, tarefaReplicacao.getTarefaId());
				pstm.setInt(2, tarefaReplicacao.getBancoDados().getId());
				pstm.setInt(3, tarefaReplicacao.getEstado().getId());
				pstm.setInt(4, tarefaReplicacao.getId());
				pstm.execute();
			}
		} finally {
			if (pstm != null) pstm.close();
		}
	}
	
	public void gravaMensagem(TarefaPendente.TarefaReplicacao tarefaReplicacao) throws Exception {
		PreparedStatement pstm = getConexao().prepareStatement(
			"UPDATE TarefaReplicacao SET Mensagem = ? WHERE Id = ?");
		try {
			pstm.setString(1, tarefaReplicacao.getMensagem());
			pstm.setInt(2, tarefaReplicacao.getId());
			pstm.execute();
		} finally {
			pstm.close();
		}
	}
	
	public void gravaMensagem(TarefaPendente tarefa, String mensagem) throws Exception {
		PreparedStatement pstm = getConexao().prepareStatement(
			"UPDATE TarefaReplicacao SET Mensagem = ? WHERE Tarefa_Id = ?");
		try {
			pstm.setString(1, mensagem);
			pstm.setInt(2, tarefa.getId());
			pstm.execute();
		} finally {
			pstm.close();
		}
	}
	
	public void exclui(int id) throws Exception {
		exclui(id, "TarefaReplicacao");
	}
	
	public List<TarefaReplicacao> buscaPorTarefaId(int tarefaId) throws Exception {
		List<TarefaReplicacao> lista = new ArrayList<TarefaReplicacao>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = getConexao().prepareStatement(
				"SELECT "
				+ "TarefaReplicacao.Id,"
				+ "TarefaReplicacao.Tarefa_Id,"
				+ "TarefaReplicacao.BancoDados_Id,"
				+ "BancoDados.Nome,"
				+ "TarefaReplicacao.Estado,"
				+ "TarefaReplicacao.Mensagem "
				+ "FROM TarefaReplicacao "
				+ "JOIN BancoDados ON BancoDados.Id = TarefaReplicacao.BancoDados_Id "
				+ "WHERE TarefaReplicacao.Tarefa_Id = ? "
				+ "ORDER BY BancoDados.Nome");
			pstm.setInt(1, tarefaId);
			rs = pstm.executeQuery();			
			while (rs.next()) {
				TarefaReplicacao tr = new TarefaReplicacao();
				tr.setId(rs.getInt(1));
				tr.setTarefaId(rs.getInt(2));
				tr.getBancoDados().setId(rs.getInt(3));
				tr.getBancoDados().setNome(rs.getString(4));
				tr.setEstado(TarefaDestino.Estado.fromId(rs.getInt(5)));
				tr.setMensagem(rs.getString(6));
				lista.add(tr);
			}
		} finally {
			if (rs != null) rs.close();
			if (pstm != null) pstm.close();
		}
		return lista;
	}

}
