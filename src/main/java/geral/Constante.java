package geral;

public class Constante {

	// Legendas e mensagens (todas começam com STR_).
	public static final String STR_TITULO_APLICACAO = "Gerenciador de Backup e Replicação";
	public static final String STR_SOBRE_SISTEMA = "Sobre o sistema";
	public static final String STR_DADOS_SISTEMA =
		"<html><div align=\"center\"" + 
		"<h1>" + Constante.STR_TITULO_APLICACAO + "</h1>" + 
		"<br>Aplicação desenvolvida como parte do Trabalho de Conclusão de Curso." + 
		"<br><br>Título do trabalho<br>BACKUP DINÂMICO DE BANCO DE DADOS DO SGBD FIREBIRD 3.0" + 
		"<br><br>Formando:<br>DANIEL PEREIRA GUIMARÃES" + 
		"<br><br>Orientador:<br>PROF. REINALDO LIMA PEREIRA" + 
		"<br><br>Curso:<br>ANÁLISE E DESENVOLVIMENTO DE SISTEMAS" + 
		"<br><br>Instituição:<br>INSTITUTO FEDERAL DE EDUCAÇÃO, CIÊNCIA E TECNOLOGIA DE RONDÔNIA" + 
		"<br><br><i>Ji-Paraná/RO, novembro de 2019.</i>" + 
		"</div></html>";
	
	public static final String STR_CADASTRO = "Cadastro";
	public static final String STR_BANCOS_DE_DADOS = "Bancos de dados";
	public static final String STR_BANCO_DE_DADOS = "Banco de dados";
	public static final String STR_PASTAS_PARA_BACKUP = "Pastas para backup";
	public static final String STR_MONITORES = "Monitores";
	public static final String STR_TAREFAS = "Tarefas";
	public static final String STR_TAREFA = "Tarefa";
	public static final String STR_BACKUP = "Backup";
	public static final String STR_REPLICACAO = "Replicação";
	public static final String STR_DESTINOS = "Destinos";
	public static final String STR_FERRAMENTAS = "Ferramentas";
	public static final String STR_AJUDA = "Ajuda";
	public static final String STR_INFORMACAO = "Informação";
	public static final String STR_CONFIRMACAO = "Confirmação";
	public static final String STR_ERRO = "Erro";
	public static final String STR_PESQUISAR = "Pesquisar";
	public static final String STR_INSERIR = "Inserir";
	public static final String STR_ALTERAR = "Alterar";
	public static final String STR_EXCLUIR = "Excluir";
	public static final String STR_OK = "Ok";
	public static final String STR_CANCELAR = "Cancelar";
	public static final String STR_SALVAR = "Salvar";
	public static final String STR_PAUSAR = "Pausar";
	public static final String STR_CONTINUAR = "Continuar";
	public static final String STR_LIMPAR = "Limpar";
	public static final String STR_TESTAR_ACESSO = "Testar acesso";
	public static final String STR_ENDERECO_DO_GBAK = "Endereco do GBAK";
	public static final String STR_PROCURAR = "Procurar";
	public static final String STR_CONFIGURACOES = "Configurações";
	public static final String STR_DATA = "Data";
	public static final String STR_HORA = "Hora";
	
	public static final String STR_NOME = "Nome";
	public static final String STR_MAQUINA = "Máquina";
	public static final String STR_PORTA = "Porta";
	public static final String STR_ENDERECO = "Endereço";
	public static final String STR_CARACTERES = "Caracteres";
	public static final String STR_USUARIO = "Usuário";
	public static final String STR_SENHA = "Senha";
	public static final String STR_CHAVE = "Chave";
	public static final String STR_TIPO = "Tipo";
	public static final String STR_LOCAL = "Local";
	public static final String STR_FTP = "FTP";
	public static final String STR_INTERVALO_SEGUNDOS = "Intervalo (segundos)";
	public static final String STR_INTERVALO_MINUTOS = "Intervalo (minutos)";
	public static final String STR_HORA_INICIAL = "Hora inicial";
	public static final String STR_HORA_FINAL = "Hora final";
	public static final String STR_DIAS_DA_SEMANA = "Dias da semana";
	public static final String STR_DOMINGO = "Domingo";
	public static final String STR_SEGUNDA = "Segunda";
	public static final String STR_TERCA = "Terça";
	public static final String STR_QUARTA = "Quarta";
	public static final String STR_QUINTA = "Quinta";
	public static final String STR_SEXTA = "Sexta";
	public static final String STR_SABADO = "Sábado";
	public static final String STR_ESTADO = "Estado";
	public static final String STR_MENSAGEM = "Mensagem";
	public static final String STR_NORMAL = "Normal";
	public static final String STR_PAUSADA = "Pausada";
	public static final String STR_INOPERANTE = "Inoperante";
	public static final String STR_MANTER = "Manter";
	public static final String STR_USUARIO_CONEXAO_LOCAL = "Usuário do Firebird local";
	public static final String STR_SENHA_CONEXAO_LOCAL = "Senha do Firebird local";
	
	public static final String STR_ERRO_FAZER_BACKUP = "Erro ao fazer backup.";
	public static final String STR_ERRO_RESTAURAR_BACKUP = "Erro ao restaurar backup.";
	public static final String STR_ERRO_CLONAR_BANCO_DADOS = "Erro ao copiar o banco de dados.";
	public static final String STR_PAUSAR_PARA_EXCLUIR = "É necessário pausar primeiro para depois excluir.";
	
	public static final String STR_BANCO_NAO_CONECTADO = "Banco de dados não conectado.";
	public static final String STR_BANCO_DESTINO_SEM_ID = "Banco de dados de destino sem identificação.";
	public static final String STR_BANCO_DESTINO_IGUAL_ORIGEM = "Banco de dados de origem e destino iguais.";
	public static final String STR_BANCO_DESTINO_INCOMPATIVEL = "Banco de dados de destino incompatível."; 
	public static final String STR_BANCO_DESTINO_JA_EXISTE = "O banco de dados de destino já existe. A replicação deve ser programada para um banco de dados que ainda não existe, pois será criado automaticamente.";
	public static final String STR_ERRO_DEFINIR_UUID_DESTINO = "Erro ao definir UUID do banco de dados de destino.";
	public static final String STR_INSERT_SEM_RETORNO = "O comando INSERT não retornou os dados esperados.";
	public static final String STR_CLASSES_INCOMPATIVEIS = "A classe '%s' não é compatível com '%s'.";
	public static final String STR_SEQUENCIA_REP_QUEBRADA = "Sequência de replicação quebrada.";
	public static final String STR_ERRO_GRAVAR_SEQUENCIA_REP = "Erro ao gravar sequência de replicação.";
	public static final String STR_CAMPO_REQUERIDO = "Este campo não pode ficar em branco.";
	public static final String STR_CAMPO_REQUERIDO_NOME = "O campo '%s' não pode ficar em branco.";
	public static final String STR_CONF_EXCLUIR_REG = "Excluir o registro selecionado?";
	public static final String STR_SELECIONE_LINHA = "Selecione uma linha e tente novamente.";
	public static final String STR_VALOR_INVALIDO = "Valor inválido: %s";
	public static final String STR_VALOR_INVALIDO_CAMPO = "Valor inválido no campo '%s'.";
	public static final String STR_INTERVALO_INVALIDO = "Valor inválido: %s\nO intervalo válido é de %d até %d.";
	public static final String STR_INFORMAR_CAB_TAREFA = "Informe os dados principais da tarefa.";
	public static final String STR_ACESSO_COM_SUCESSO = "Acesso estabelecido com sucesso para\n%s";
	public static final String STR_MAQUINA_DESCONHECIDA = "Máquina desconhecida:\n%s";
	public static final String STR_ERRO_DESCONHECIDO = "Erro desconhecido.";
	public static final String STR_FALHA_FTP = "Falha no acesso ao FTP.\n%s:%d:%s\n%s";
	public static final String STR_PASTA_NAO_ENCONTRADA = "Pasta não encontrada:\n%s";
	public static final String STR_ERRO_CRIAR_ARQUIVO = "Erro ao criar o arquivo:\n%s\n%s";
	public static final String STR_ERRO_ACESSAR_ARQUIVO = "Erro ao acessar o arquivo:\n%s\n%s";
	public static final String STR_PROGRAMA_JA_ABERTO = "Este programa já está aberto.";
	public static final String STR_RECURSO_NAO_ENCONTRADO = "Recurso não encontrado: %s";
	
	public static final String STR_ERRO_GRAVAR_FIREBASE = "Erro ao gravar no Firebase: %s";
	public static final String STR_ENVIANDO_DADOS_MON = "Enviando dados de monitoramento para %s.";
	public static final String STR_ENVIADOS_DADOS_MON = "Dados de monitoramento enviados para %s.";
	
	public static final String STR_BKP_PREPARANDO = "Preparando backup de %s.";
	public static final String STR_BKP_COMPACTANDO = "Compactando backup de %s.";
	public static final String STR_BKP_ENVIANDO = "Enviando backup de %s para %s.";
	public static final String STR_BKP_ENVIADO = "Backup de %s enviado para %s.";
	
	public static final String STR_REP_PREPARANDO_ORIGEM = "Preparando origem da replicação: %s";
	public static final String STR_REP_PREPARANDO_DESTINO = "Preparando destino da replicação: %s";
	public static final String STR_REP_DESPREPARANDO_ORIGEM = "Despreparando origem da replicação: %s";
	public static final String STR_REP_REPLICANDO = "Replicando de %s para %s.";
	public static final String STR_REP_COMANDOS_TRANSACOES = "Replicado(s) %d comando(s) em %d transação(ões).";
	public static final String STR_REP_NENHUM_REGISTRO = "Nenhum registro replicado.";
	
	public static final String STR_CREDENCIAL_FIREBASE = "Credencial Firebase";
	public static final String STR_URL_BANCO_FIREBASE = "URL do banco Firebase";
	public static final String STR_LOTE_REPLICACAO = "Lote de replicação";
	public static final String STR_TRANSACOES = "transações";
	public static final String STR_INTERVALO_NOTIFICACAO = "Intervalo de notificação";
	public static final String STR_MINUTOS = "minutos";
	
	public static final String STR_UN_BANCODADOS_NOME = "Nome de banco de dados já cadastrado.";
	public static final String STR_UN_BANCODADOS = "Banco de dados (máquina + porta + endereço) já cadastrado.";
	public static final String STR_UN_PASTA_NOME = "Nome de pasta já cadastrado.";
	public static final String STR_UN_PASTA = "Pasta (máquina + porta + endereço) já cadastrada.";
	public static final String STR_UN_MONITOR_CHAVE = "Chave de monitor já cadastrada.";
	public static final String STR_UN_TAREFA_NOME = "Nome de tarefa já cadastrado.";
	public static final String STR_UN_TAREFAREPLICACAO =  "Banco de dados já adicionado como destino de uma replicação.";
	public static final String STR_UN_TAREFAMONITOR = "Monitor já adicionado na tarefa.";
	public static final String STR_UN_TAREFABACKUP = "Pasta já adicionada na tarefa.";
	
	public static final String STR_FK_TAREFA_BANCODADOS = "Violação do relacionamento entre tarefas e bancos de dados.";
	public static final String STR_FK_TAREFABACKUP_PASTA = "Violação do relaciomanento entre tarefas e pastas.";
	public static final String STR_FK_TAREFAMONITOR_MONITOR = "Violação do relacionamento entre tarefas e monitores.";

	// Outras constantes
	public static final String BD_MAQUINA_PADRAO = "localhost";
	public static final int    BD_PORTA_PADRAO = 3050;
	public static final String BD_ENDERECO_PADRAO = "C:\\Sistema\\BancoDados.fdb";
	public static final String BD_CARACTERES_PADRAO = "UTF8";
	public static final String BD_USUARIO_PADRAO = "SYSDBA";
	public static final String BD_SENHA_PADRAO = "masterkey";
	
	public static final int    FTP_PORTA_PADRAO = 21;
	
	public static final String[] LISTA_CARACTERES = {
		"NONE",
		"OCTETS",
		"ASCII",
		"UNICODE_FSS",
		"UTF8",
		"SJIS_0208",
		"EUCJ_0208",
		"DOS437",
		"DOS850",
		"DOS865",
		"ISO8859_1",
		"ISO8859_2",
		"ISO8859_3",
		"ISO8859_4",
		"ISO8859_5",
		"ISO8859_6",
		"ISO8859_7",
		"ISO8859_8",
		"ISO8859_9",
		"ISO8859_13",
		"DOS852",
		"DOS857",
		"DOS860",
		"DOS861",
		"DOS863",
		"CYRL",
		"DOS737",
		"DOS775",
		"DOS858",
		"DOS862",
		"DOS864",
		"DOS866",
		"DOS869",
		"WIN1250",
		"WIN1251",
		"WIN1252",
		"WIN1253",
		"WIN1254",
		"NEXT",
		"WIN1255",
		"WIN1256",
		"WIN1257",
		"KSC_5601",
		"BIG_5",
		"GB_2312",
		"KOI8R",
		"KOI8U",
		"WIN1258",
		"TIS620",
		"GBK",
		"CP943C",
		"GB18030"};           
	
}
