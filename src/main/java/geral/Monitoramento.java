package geral;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

import modelo.Monitor;
import modelo.Notificacao;
import modelo.TarefaDestino;
import regra.MonitorRN;
import regra.TarefaRN;

public class Monitoramento {

	private static final String CREDENCIAL = Util.getAppAbsolutePath() + "firebase.json";
	private static final String BANCO_DADOS = System.getenv("GBR_FIREBASE_BANCO_DADOS");
	
	private static DatabaseReference databaseReference = null;
	
	public static final DatabaseReference getDatabaseReference() throws IOException {
		if (databaseReference == null) {
			FileInputStream fisCredentials = new FileInputStream(new File(CREDENCIAL));
			GoogleCredentials googleCredentials = GoogleCredentials.fromStream(fisCredentials);
			FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
				.setCredentials(googleCredentials)
			    .setDatabaseUrl(BANCO_DADOS)
			    .build();
			FirebaseApp.initializeApp(firebaseOptions);
			FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
			databaseReference = firebaseDatabase.getReference();
		}
		return databaseReference;
	}
	
	private static final class NotificacaoCompletionListener implements CompletionListener {
		private Monitor monitor;
		
		public NotificacaoCompletionListener(Monitor monitor) {
			this.monitor = monitor;
		}

		@Override
		public void onComplete(DatabaseError de, DatabaseReference dr) {
			if (de == null) {
				Saida.escreve(Saida.Tipo.NORMAL, String.format(
					Constante.STR_ENVIADOS_DADOS_MON, monitor.getNome()));
				try {
					MonitorRN.grava(monitor);
				} catch (Exception e) {
					Saida.escreve(Saida.Tipo.ERRO, e.getMessage());
				}
			} else {
				Saida.escreve(Saida.Tipo.ERRO, String.format(
					Constante.STR_ERRO_GRAVAR_FIREBASE, de.getMessage()));
			}
		}
		
	}
	
	public static final synchronized void enviaTarefas(
			boolean sistemaPausado, boolean forcarEnvio) {
		try {
			long dataHoraAtual = System.currentTimeMillis();
			if (!forcarEnvio) {
				long tempo = dataHoraAtual - Config.getDataHoraNotificacao();
				forcarEnvio = tempo >= Config.getIntervaloNotificacao();
			}
			DatabaseReference dbRef = getDatabaseReference();
			List<Monitor> monitores = MonitorRN.buscaTudo();
			boolean salvarDataHoraNotificacao = true;
			for (Monitor monitor : monitores) {
				List<Notificacao> notificacoes = TarefaRN.buscaNotificacoes(monitor.getId());
				if (sistemaPausado) {
					for (Notificacao notificacao : notificacoes)
						notificacao.setEstado(TarefaDestino.Estado.PAUSADO.getId());
				}
				String controle = Util.md5(notificacoes.toString());
				if (forcarEnvio || !controle.equals(monitor.getControle())) {
					Saida.escreve(Saida.Tipo.NORMAL, String.format(
						Constante.STR_ENVIANDO_DADOS_MON, monitor.getNome()));
					monitor.setControle(controle);
					dbRef.child(monitor.getChave())
						.child(Config.getServidorId())
						.setValue(notificacoes,	new NotificacaoCompletionListener(monitor));
					if (salvarDataHoraNotificacao) {
						Config.setDataHoraNotificacao(dataHoraAtual);
						salvarDataHoraNotificacao = false; 
					}
				}
			}
		} catch(Throwable e) {
			Saida.escreve(Saida.Tipo.ERRO, e.getMessage());
		}
	}
	
	public static final void removeMonitor(String monitor) throws IOException {
		getDatabaseReference().child(monitor).removeValue(null);
	}
}
