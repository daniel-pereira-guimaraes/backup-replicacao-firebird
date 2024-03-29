EXECUTE BLOCK AS
BEGIN
	RDB$SET_CONTEXT('USER_SESSION', 'R$C$VAR$REPLICADOR', TRUE);
END^

EXECUTE BLOCK AS
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = 'R$C$TB$BANCO_DADOS')) THEN
	BEGIN
		EXECUTE STATEMENT 
			'CREATE TABLE R$C$TB$BANCO_DADOS(' ||
			'	UUID VARCHAR(36) NOT NULL PRIMARY KEY,'||
			'	ORIGEM VARCHAR(36),' ||
			'	SEQUENCIA BIGINT UNIQUE)';
	END
END^

EXECUTE BLOCK AS
BEGIN
	IF (NOT EXISTS(SELECT * FROM R$C$TB$BANCO_DADOS)) THEN
		INSERT INTO R$C$TB$BANCO_DADOS(UUID) VALUES(UUID_TO_CHAR(GEN_UUID()));
END^

EXECUTE BLOCK AS 
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = 'R$O$TB$DESTINO')) THEN
	BEGIN
		EXECUTE STATEMENT 
			'CREATE TABLE R$O$TB$DESTINO(' ||
			'ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,' ||
			'UUID VARCHAR(36) NOT NULL UNIQUE)';
	END
END^


EXECUTE BLOCK AS 
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = 'R$O$TB$TRANSACAO')) THEN
	BEGIN
		EXECUTE STATEMENT 
			'CREATE TABLE R$O$TB$TRANSACAO(' ||
			'ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,' ||
			'SEQUENCIA BIGINT)';
	END
END^

EXECUTE BLOCK AS 
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = 'R$O$TB$TRANSACAO_DESTINO')) THEN
	BEGIN
		EXECUTE STATEMENT 
			'CREATE TABLE R$O$TB$TRANSACAO_DESTINO(' ||
			'ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,' ||
			'TRANSACAO_ID BIGINT NOT NULL REFERENCES R$O$TB$TRANSACAO(ID) ON DELETE CASCADE,' ||
			'DESTINO_ID BIGINT NOT NULL REFERENCES R$O$TB$DESTINO(ID) ON DELETE CASCADE)';
	END
END^


EXECUTE BLOCK AS 
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = 'R$O$TB$COMANDO')) THEN
	BEGIN
		EXECUTE STATEMENT 
			'CREATE TABLE R$O$TB$COMANDO(' ||
			'ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,' ||
			'TRANSACAO_ID BIGINT NOT NULL REFERENCES R$O$TB$TRANSACAO(ID) ON DELETE CASCADE,' ||
			'EVENTO VARCHAR(20) NOT NULL,' ||
			'OBJETO VARCHAR(31) NOT NULL,' ||
			'COMANDO BLOB SUB_TYPE TEXT)';
	END
END^

EXECUTE BLOCK AS 
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = 'R$O$TB$PARAMETRO')) THEN
	BEGIN
		EXECUTE STATEMENT 
			'CREATE TABLE R$O$TB$PARAMETRO(' ||
			'ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,' ||
			'COMANDO_ID BIGINT NOT NULL REFERENCES R$O$TB$COMANDO(ID) ON DELETE CASCADE,' ||
			'NOME VARCHAR(31) NOT NULL,' ||
			'VALOR VARCHAR(32765) CHARACTER SET NONE,' ||
			'VALOR_REAL DOUBLE PRECISION,' ||
			'VALOR_BINARIO BLOB CHARACTER SET NONE,' ||
			'FILTRO BOOLEAN DEFAULT FALSE NOT NULL)';
	END
END^
	
EXECUTE BLOCK AS 
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = 'R$O$GTT$COMANDO')) THEN
	BEGIN
		EXECUTE STATEMENT 
			'CREATE GLOBAL TEMPORARY TABLE R$O$GTT$COMANDO(' ||
			'ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,' ||
			'EVENTO VARCHAR(20) NOT NULL,' ||
			'OBJETO VARCHAR(31) NOT NULL,' ||
			'COMANDO BLOB SUB_TYPE TEXT)';
	END
END^

EXECUTE BLOCK AS 
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = 'R$O$GTT$PARAMETRO')) THEN
	BEGIN
		EXECUTE STATEMENT 
			'CREATE GLOBAL TEMPORARY TABLE R$O$GTT$PARAMETRO(' ||
			'ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,' ||
			'COMANDO_ID BIGINT NOT NULL REFERENCES R$O$GTT$COMANDO(ID),' ||
			'NOME VARCHAR(31) NOT NULL,' ||
			'VALOR VARCHAR(32765) CHARACTER SET NONE,' ||
			'VALOR_REAL DOUBLE PRECISION,' ||
			'VALOR_BINARIO BLOB CHARACTER SET NONE,' ||
			'FILTRO BOOLEAN DEFAULT FALSE NOT NULL)';
	END
END^

EXECUTE BLOCK AS 
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$GENERATORS WHERE RDB$GENERATOR_NAME = 'R$O$SQ$TRANSACAO')) THEN
		EXECUTE STATEMENT 'CREATE SEQUENCE R$O$SQ$TRANSACAO';
END^

EXECUTE BLOCK AS 
BEGIN
	IF (NOT EXISTS(SELECT * FROM RDB$GENERATORS WHERE RDB$GENERATOR_NAME = 'R$O$SQ$GATILHO')) THEN
		EXECUTE STATEMENT 'CREATE SEQUENCE R$O$SQ$GATILHO';
END^

CREATE OR ALTER EXCEPTION R$O$EX$REPLICADOR ''^


CREATE OR ALTER TRIGGER R$O$TG$DESTINO_INSERT BEFORE INSERT ON R$O$TB$DESTINO AS
BEGIN
	IF (EXISTS(SELECT * FROM R$C$TB$BANCO_DADOS WHERE UUID = NEW.UUID)) THEN
		EXCEPTION R$O$EX$REPLICADOR 'Bancos de dados de origem e destino iguais.';
END^

CREATE OR ALTER  TRIGGER R$O$TG$TRANSACAO_COMMIT ON TRANSACTION COMMIT AS
DECLARE TRANSACAO_ID BIGINT;
DECLARE COMANDO_ID BIGINT;
DECLARE GTT_CMD_ID BIGINT;
DECLARE GTT_CMD_EVENTO VARCHAR(20);
DECLARE GTT_CMD_OBJETO VARCHAR(31);
DECLARE GTT_CMD_COMANDO BLOB SUB_TYPE TEXT;
DECLARE GTT_PARAM_NOME VARCHAR(31);
DECLARE GTT_PARAM_VALOR VARCHAR(32765) CHARACTER SET NONE;
DECLARE GTT_PARAM_VALOR_REAL DOUBLE PRECISION;
DECLARE GTT_PARAM_VALOR_BINARIO BLOB CHARACTER SET NONE;
DECLARE GTT_PARAM_FILTRO BOOLEAN;
BEGIN
	/* Se existem comandos para replicar e destinos para replicação... */
	IF (EXISTS(SELECT * FROM R$O$TB$DESTINO) AND 
		EXISTS(SELECT * FROM R$O$GTT$COMANDO 
			WHERE EVENTO <> 'UPDATE' 
			  OR EXISTS(SELECT * FROM R$O$GTT$PARAMETRO 
				WHERE R$O$GTT$PARAMETRO.COMANDO_ID = R$O$GTT$COMANDO.ID
				  AND NOT R$O$GTT$PARAMETRO.FILTRO))) THEN
	BEGIN
		/* Grava o cabeçalho da transação, mas deixa para gravar o campo SEQUENCIA
		   somente após inserir os comandos para reduzir a probabilidade de ocorrer
		   erros de quebra de sequência no replicador. */
		INSERT INTO R$O$TB$TRANSACAO DEFAULT VALUES RETURNING Id INTO :TRANSACAO_ID;
		
		/* Insere destinos para replicação da transação. */
		INSERT INTO R$O$TB$TRANSACAO_DESTINO(TRANSACAO_ID, DESTINO_ID)
		SELECT :TRANSACAO_ID, ID FROM R$O$TB$DESTINO;
		
		/* Copia os comandos e parâmetros das tabelas temporárias para as tabelas persistentes,
		   ignorando os comandos UPDATE com nenhum campo alterado. */
		FOR
			SELECT ID, EVENTO, OBJETO, COMANDO FROM R$O$GTT$COMANDO 
			WHERE EVENTO <> 'UPDATE' 
			   OR EXISTS(SELECT * FROM R$O$GTT$PARAMETRO 
					WHERE R$O$GTT$PARAMETRO.COMANDO_ID = R$O$GTT$COMANDO.ID
					  AND NOT R$O$GTT$PARAMETRO.FILTRO)
			ORDER BY ID
			INTO :GTT_CMD_ID, :GTT_CMD_EVENTO, :GTT_CMD_OBJETO, :GTT_CMD_COMANDO
		DO
		BEGIN
			INSERT INTO R$O$TB$COMANDO(TRANSACAO_ID, EVENTO, OBJETO, COMANDO)
			VALUES(:TRANSACAO_ID, :GTT_CMD_EVENTO, :GTT_CMD_OBJETO, :GTT_CMD_COMANDO)
			RETURNING ID INTO :COMANDO_ID;
			FOR 
				SELECT 
					NOME, 
					VALOR, 
					VALOR_REAL, 
					VALOR_BINARIO, 
					FILTRO 
				FROM R$O$GTT$PARAMETRO
				WHERE COMANDO_ID = :GTT_CMD_ID 
				ORDER BY ID
				INTO 
					:GTT_PARAM_NOME, 
					:GTT_PARAM_VALOR,
					:GTT_PARAM_VALOR_REAL,
					:GTT_PARAM_VALOR_BINARIO, 
					:GTT_PARAM_FILTRO
			DO
			BEGIN
				INSERT INTO R$O$TB$PARAMETRO(
					COMANDO_ID, 
					NOME, 
					VALOR, 
					VALOR_REAL,
					VALOR_BINARIO, 
					FILTRO)
				VALUES(
					:COMANDO_ID,
					:GTT_PARAM_NOME,
					:GTT_PARAM_VALOR,
					:GTT_PARAM_VALOR_REAL,
					:GTT_PARAM_VALOR_BINARIO,	
					:GTT_PARAM_FILTRO);
			END
		END
		
		/* Atualiza a transação com o número de sequência. */
		UPDATE R$O$TB$TRANSACAO SET 
			SEQUENCIA = NEXT VALUE FOR R$O$SQ$TRANSACAO
		WHERE ID = :TRANSACAO_ID;
	END
END^

CREATE OR ALTER PROCEDURE R$O$SP$REMOVE_GATILHO_REP(TABELA VARCHAR(31)) AS
DECLARE NOME VARCHAR(31);
BEGIN
	FOR
		SELECT RDB$TRIGGER_NAME FROM RDB$TRIGGERS
		 WHERE RDB$TRIGGER_NAME LIKE 'R$O$TG$%'
		   AND RDB$RELATION_NAME = :TABELA
		   AND RDB$TRIGGER_TYPE BETWEEN 1 AND 114
		   AND RDB$SYSTEM_FLAG = 0
		INTO :NOME
	DO
		EXECUTE STATEMENT 'DROP TRIGGER ' || NOME;
END^

CREATE OR ALTER PROCEDURE R$O$SP$CRIA_GATILHO_REP_INSERT(TABELA VARCHAR(31)) AS
DECLARE COMANDO BLOB SUB_TYPE TEXT;
DECLARE CAMPO VARCHAR(31);
DECLARE TIPO SMALLINT;
DECLARE TAMANHO SMALLINT;
DECLARE CAMPO_VALOR VARCHAR(31);
DECLARE CRLF CHAR(2);
BEGIN
	CRLF = ASCII_CHAR(13) || ASCII_CHAR(10);
	COMANDO = 
		'CREATE OR ALTER TRIGGER R$O$TG$I' || NEXT VALUE FOR R$O$SQ$GATILHO || CRLF ||
		'	AFTER INSERT ON ' || TABELA || ' AS' || CRLF || 
		'DECLARE COMANDO_ID BIGINT;' || CRLF ||
		'BEGIN ' || CRLF ||
		'   IF (EXISTS(SELECT * FROM R$O$TB$DESTINO)) THEN ' || CRLF ||
		'   BEGIN' || CRLF ||
		'		INSERT INTO R$O$GTT$COMANDO(' || CRLF ||
		'			EVENTO,' || CRLF ||
		'			OBJETO)' || CRLF ||
		'		VALUES(' || CRLF ||
		'			''INSERT'',' || CRLF ||
		'			''' || TABELA || ''')' || CRLF ||
		'		RETURNING Id INTO :COMANDO_ID;' || CRLF || CRLF;
	
	/* Grava parâmetros para inserção. */
	FOR
		SELECT TRIM(RF.RDB$FIELD_NAME), F.RDB$FIELD_TYPE, F.RDB$FIELD_LENGTH
		FROM RDB$RELATION_FIELDS RF
		JOIN RDB$FIELDS F ON (F.RDB$FIELD_NAME = RF.RDB$FIELD_SOURCE)
		WHERE TRIM(RF.RDB$RELATION_NAME) = :TABELA
		  AND F.RDB$COMPUTED_BLR IS NULL
		ORDER BY RF.RDB$FIELD_POSITION
		INTO :CAMPO, :TIPO, :TAMANHO
	DO
	BEGIN
		IF (TIPO IN (10,27)) THEN 
			CAMPO_VALOR = 'VALOR_REAL';
		ELSE IF (TIPO = 14 AND TAMANHO > 32765) THEN 
			CAMPO_VALOR = 'VALOR_BINARIO';
		ELSE IF (TIPO = 261) THEN 
			CAMPO_VALOR = 'VALOR_BINARIO';
		ELSE 
			CAMPO_VALOR = 'VALOR';
		COMANDO = COMANDO ||
			'		IF (NEW.' || CAMPO || ' IS NOT NULL) THEN' || CRLF ||
			'		BEGIN' || CRLF ||
			'			INSERT INTO R$O$GTT$PARAMETRO(COMANDO_ID, NOME, ' || CAMPO_VALOR || ')' || CRLF ||
			'			VALUES(:COMANDO_ID, ''' || CAMPO || ''', NEW.' || CAMPO || ');' || CRLF ||
			'		END' ||  CRLF;
	END
	
	COMANDO = COMANDO || 'END END';
		
	EXECUTE STATEMENT COMANDO;
END^

CREATE OR ALTER PROCEDURE R$O$SP$CRIA_GATILHO_REP_UPDATE(TABELA VARCHAR(31)) AS
DECLARE COMANDO BLOB SUB_TYPE TEXT;
DECLARE CAMPO VARCHAR(31);
DECLARE TIPO SMALLINT;
DECLARE TAMANHO SMALLINT;
DECLARE CAMPO_VALOR VARCHAR(31);
DECLARE NOME_TIPO VARCHAR(20);
DECLARE OLD_VALUE VARCHAR(100);
DECLARE NEW_VALUE VARCHAR(100);
DECLARE CRLF CHAR(2);
BEGIN
	CRLF = ASCII_CHAR(13) || ASCII_CHAR(10);
	COMANDO = 
		'CREATE OR ALTER TRIGGER R$O$TG$U' || NEXT VALUE FOR R$O$SQ$GATILHO || CRLF ||
		'	AFTER UPDATE ON ' || TABELA || ' AS' || CRLF || 
		'DECLARE COMANDO_ID BIGINT;' || CRLF ||
		'BEGIN ' || CRLF ||
		'   IF (EXISTS(SELECT * FROM R$O$TB$DESTINO)) THEN ' || CRLF ||
		'   BEGIN' || CRLF ||
		'		INSERT INTO R$O$GTT$COMANDO(' || CRLF ||
		'			EVENTO,' || CRLF ||
		'			OBJETO)' || CRLF ||
		'		VALUES(' || CRLF ||
		'			''UPDATE'',' || CRLF ||
		'			''' || TABELA || ''')' || CRLF ||
		'		RETURNING Id INTO :COMANDO_ID;' || CRLF || CRLF;

	/* Grava parâmetros para os campos a serem atualizados. */
	FOR
		SELECT TRIM(RF.RDB$FIELD_NAME), F.RDB$FIELD_TYPE, F.RDB$FIELD_LENGTH
		FROM RDB$RELATION_FIELDS RF
		JOIN RDB$FIELDS F ON (F.RDB$FIELD_NAME = RF.RDB$FIELD_SOURCE)
		WHERE TRIM(RF.RDB$RELATION_NAME) = :TABELA
		  AND F.RDB$COMPUTED_BLR IS NULL
		ORDER BY RF.RDB$FIELD_POSITION
		INTO :CAMPO, :TIPO, :TAMANHO
	DO
	BEGIN
		IF (TIPO IN (10,27)) THEN 
			CAMPO_VALOR = 'VALOR_REAL';
		ELSE IF (TIPO = 14 AND TAMANHO > 32765) THEN 
			CAMPO_VALOR = 'VALOR_BINARIO';
		ELSE IF (TIPO = 261) THEN 
			CAMPO_VALOR = 'VALOR_BINARIO';
		ELSE 
			CAMPO_VALOR = 'VALOR';
		IF (TIPO IN (14, 37, 261)) THEN
		BEGIN
			NOME_TIPO = DECODE(TIPO, 14, 'CHAR', 37, 'VARCHAR', 'BLOB');
			IF (TIPO IN (14, 37)) THEN
				NOME_TIPO = NOME_TIPO || ' (' || TAMANHO || ')';
			OLD_VALUE = 'CAST(OLD.' || CAMPO || ' AS ' || NOME_TIPO || ' CHARACTER SET NONE)';
			NEW_VALUE = 'CAST(NEW.' || CAMPO || ' AS ' || NOME_TIPO || ' CHARACTER SET NONE)';
		END ELSE
		BEGIN
			OLD_VALUE = 'OLD.' || CAMPO;
			NEW_VALUE = 'NEW.' || CAMPO;
		END
		COMANDO = COMANDO ||
			'		IF (' || OLD_VALUE || ' IS DISTINCT FROM ' || NEW_VALUE || ') THEN' || CRLF ||
			'		BEGIN' || CRLF ||
			'			INSERT INTO R$O$GTT$PARAMETRO(COMANDO_ID, NOME, ' || CAMPO_VALOR || ')' || CRLF ||
			'			VALUES(:COMANDO_ID, ''' || CAMPO || ''', NEW.' || CAMPO || ');' || CRLF ||
			'		END' ||  CRLF;
	END
	COMANDO = COMANDO || CRLF;
	
	/* Grava parâmetros para montagem do filtro de atualização. */
	IF (EXISTS(SELECT * FROM RDB$RELATION_CONSTRAINTS RC
		WHERE TRIM(RC.RDB$CONSTRAINT_TYPE) = 'PRIMARY KEY' 
		  AND TRIM(RC.RDB$RELATION_NAME) = :TABELA)) THEN
	BEGIN
		FOR
			SELECT TRIM(SG.RDB$FIELD_NAME) FROM RDB$INDICES IX
			JOIN RDB$INDEX_SEGMENTS SG ON IX.RDB$INDEX_NAME = SG.RDB$INDEX_NAME
			JOIN RDB$RELATION_CONSTRAINTS RC ON RC.RDB$INDEX_NAME = IX.RDB$INDEX_NAME
			WHERE TRIM(RC.RDB$CONSTRAINT_TYPE) = 'PRIMARY KEY'
			  AND TRIM(RC.RDB$RELATION_NAME) = :TABELA
			INTO :CAMPO
		DO
		BEGIN
			COMANDO = COMANDO ||
				'		INSERT INTO R$O$GTT$PARAMETRO(COMANDO_ID, NOME, VALOR, FILTRO)' || CRLF || 
				'		VALUES(:COMANDO_ID, ''' || :CAMPO || ''', OLD.' || :CAMPO || ', TRUE);' || CRLF;
		END
	END ELSE
	BEGIN
		FOR 
			SELECT TRIM(RF.RDB$FIELD_NAME), F.RDB$FIELD_TYPE, F.RDB$FIELD_LENGTH
			FROM RDB$RELATION_FIELDS RF
			JOIN RDB$FIELDS F ON (F.RDB$FIELD_NAME = RF.RDB$FIELD_SOURCE)
			WHERE TRIM(RF.RDB$RELATION_NAME) = :TABELA
			  AND F.RDB$COMPUTED_BLR IS NULL
			ORDER BY RF.RDB$FIELD_POSITION
			INTO :CAMPO, :TIPO, :TAMANHO
		DO
		BEGIN
			IF (TIPO IN (10,27)) THEN 
				CAMPO_VALOR = 'VALOR_REAL';
			ELSE IF (TIPO = 14 AND TAMANHO > 32765) THEN 
				CAMPO_VALOR = 'VALOR_BINARIO';
			ELSE IF (TIPO = 261) THEN 
				CAMPO_VALOR = 'VALOR_BINARIO';
			ELSE 
				CAMPO_VALOR = 'VALOR';
			COMANDO = COMANDO ||
				'		INSERT INTO R$O$GTT$PARAMETRO(COMANDO_ID, NOME, ' || CAMPO_VALOR || ', FILTRO)' || CRLF ||
				'		VALUES(:COMANDO_ID, ''' || CAMPO || ''', OLD.' || CAMPO || ', TRUE);' || CRLF;
		END
	END
	
	COMANDO = COMANDO || 'END END';
		
	EXECUTE STATEMENT COMANDO;
END^

CREATE OR ALTER PROCEDURE R$O$SP$CRIA_GATILHO_REP_DELETE(TABELA VARCHAR(31)) AS
DECLARE COMANDO BLOB SUB_TYPE TEXT;
DECLARE CAMPO VARCHAR(31);
DECLARE TIPO SMALLINT;
DECLARE TAMANHO SMALLINT;
DECLARE CAMPO_VALOR VARCHAR(31);
DECLARE CRLF CHAR(2);
BEGIN
	CRLF = ASCII_CHAR(13) || ASCII_CHAR(10);
	COMANDO = 
		'CREATE OR ALTER TRIGGER R$O$TG$D' || NEXT VALUE FOR R$O$SQ$GATILHO || CRLF ||
		'	AFTER DELETE ON ' || TABELA || ' AS' || CRLF ||
		'DECLARE COMANDO_ID BIGINT;' || CRLF ||
		'BEGIN' || CRLF ||
		'   IF (EXISTS(SELECT * FROM R$O$TB$DESTINO)) THEN ' || CRLF ||
		'   BEGIN' || CRLF ||
		'		INSERT INTO R$O$GTT$COMANDO(' || CRLF ||
		'			EVENTO,' || CRLF ||
		'			OBJETO)' || CRLF ||
		'		VALUES(' || CRLF ||
		'			''DELETE'',' ||  CRLF ||
		'			''' || TABELA || ''')' || CRLF ||
		'		RETURNING Id INTO :COMANDO_ID;' || CRLF || CRLF;
		
	/* Parâmetros para filtro. */
	IF (EXISTS(SELECT * FROM RDB$RELATION_CONSTRAINTS RC
		WHERE TRIM(RC.RDB$CONSTRAINT_TYPE) = 'PRIMARY KEY' 
		  AND TRIM(RC.RDB$RELATION_NAME) = :TABELA)) THEN
	BEGIN
		FOR
			SELECT TRIM(SG.RDB$FIELD_NAME) FROM RDB$INDICES IX
			JOIN RDB$INDEX_SEGMENTS SG ON IX.RDB$INDEX_NAME = SG.RDB$INDEX_NAME
			JOIN RDB$RELATION_CONSTRAINTS RC ON RC.RDB$INDEX_NAME = IX.RDB$INDEX_NAME
			WHERE TRIM(RC.RDB$CONSTRAINT_TYPE) = 'PRIMARY KEY'
			  AND TRIM(RC.RDB$RELATION_NAME) = :TABELA
			INTO :CAMPO
		DO
		BEGIN
			COMANDO = COMANDO ||
				'		INSERT INTO R$O$GTT$PARAMETRO(COMANDO_ID, NOME, VALOR, FILTRO)' || CRLF || 
				'		VALUES(:COMANDO_ID, ''' || :CAMPO || ''', OLD.' || :CAMPO || ', TRUE);' || CRLF;
		END
	END ELSE
	BEGIN
		FOR 
			SELECT TRIM(RF.RDB$FIELD_NAME), F.RDB$FIELD_TYPE, F.RDB$FIELD_LENGTH
			FROM RDB$RELATION_FIELDS RF
			JOIN RDB$FIELDS F ON (F.RDB$FIELD_NAME = RF.RDB$FIELD_SOURCE)
			WHERE TRIM(RF.RDB$RELATION_NAME) = :TABELA
			  AND F.RDB$COMPUTED_BLR IS NULL
			ORDER BY RF.RDB$FIELD_POSITION
			INTO :CAMPO, :TIPO, :TAMANHO
		DO
		BEGIN
			IF (TIPO IN (10,27)) THEN 
				CAMPO_VALOR = 'VALOR_REAL';
			ELSE IF (TIPO = 14 AND TAMANHO > 32765) THEN 
				CAMPO_VALOR = 'VALOR_BINARIO';
			ELSE IF (TIPO = 261) THEN 
				CAMPO_VALOR = 'VALOR_BINARIO';
			ELSE 
				CAMPO_VALOR = 'VALOR';
			COMANDO = COMANDO ||
				'		INSERT INTO R$O$GTT$PARAMETRO(COMANDO_ID, NOME, ' || CAMPO_VALOR || ', FILTRO)' || CRLF ||
				'		VALUES(:COMANDO_ID, ''' || CAMPO || ''', OLD.' || CAMPO || ', TRUE);' || CRLF;
		END
	END
		
	COMANDO = COMANDO || 'END END';

	EXECUTE STATEMENT :COMANDO;
END^

EXECUTE BLOCK AS
DECLARE TABELA VARCHAR(31);
BEGIN
	/* Cria gatilhos para todas as tabelas que não sejam do replicador. */
	FOR
		SELECT TRIM(RDB$RELATION_NAME) FROM RDB$RELATIONS
		WHERE RDB$RELATION_TYPE = 0 
		  AND RDB$SYSTEM_FLAG = 0
		  AND RDB$RELATION_NAME NOT SIMILAR TO 'R$(C|O|D)$(TB|GTT)$%'
		INTO :TABELA
	DO
	BEGIN
		EXECUTE PROCEDURE R$O$SP$REMOVE_GATILHO_REP(TABELA);
		EXECUTE PROCEDURE R$O$SP$CRIA_GATILHO_REP_INSERT(TABELA);
		EXECUTE PROCEDURE R$O$SP$CRIA_GATILHO_REP_UPDATE(TABELA);
		EXECUTE PROCEDURE R$O$SP$CRIA_GATILHO_REP_DELETE(TABELA);
	END
END^

CREATE OR ALTER PROCEDURE R$O$SP$REMOVE_TRANSACAO(
	DESTINO VARCHAR(36),
	SEQUENCIA BIGINT) 
AS
BEGIN
	DELETE FROM R$O$TB$TRANSACAO_DESTINO 
	WHERE DESTINO_ID = (SELECT ID FROM R$O$TB$DESTINO WHERE UUID = :DESTINO)
	  AND TRANSACAO_ID IN (SELECT ID FROM R$O$TB$TRANSACAO WHERE SEQUENCIA <= :SEQUENCIA);
	
	DELETE FROM R$O$TB$TRANSACAO
	WHERE ID NOT IN (SELECT TRANSACAO_ID FROM R$O$TB$TRANSACAO_DESTINO);
END^

CREATE OR ALTER TRIGGER R$O$TG$DDL AFTER ANY DDL STATEMENT AS
DECLARE OBJETO VARCHAR(31);
BEGIN
	IF (RDB$GET_CONTEXT('DDL_TRIGGER', 'OBJECT_TYPE') IS DISTINCT FROM 'USER') THEN
	BEGIN
		OBJETO = RDB$GET_CONTEXT('DDL_TRIGGER', 'OBJECT_NAME');
		IF (OBJETO NOT SIMILAR TO 'R$(C|O|D)$(TB|GTT|TG|SP|EX|SQ)$%') THEN
		BEGIN
			IF (EXISTS(SELECT * FROM R$O$TB$DESTINO)) THEN
			BEGIN
				/* Salva o comando DDL. */
				INSERT INTO R$O$GTT$COMANDO(
					EVENTO,
					OBJETO,
					COMANDO)
				VALUES(
					RDB$GET_CONTEXT('DDL_TRIGGER', 'DDL_EVENT'),
					RDB$GET_CONTEXT('DDL_TRIGGER', 'OBJECT_NAME'),
					RDB$GET_CONTEXT('DDL_TRIGGER', 'SQL_TEXT'));
			END
				
			/* Se for CREATE TABLE ou ALTER TABLE, cria ou altera gatilhos. */
			IF (RDB$GET_CONTEXT('DDL_TRIGGER', 'OBJECT_TYPE') = 'TABLE' AND
				RDB$GET_CONTEXT('DDL_TRIGGER', 'EVENT_TYPE') <> 'DROP') THEN
			BEGIN
				EXECUTE PROCEDURE R$O$SP$REMOVE_GATILHO_REP(OBJETO);
				EXECUTE PROCEDURE R$O$SP$CRIA_GATILHO_REP_INSERT(OBJETO);
				EXECUTE PROCEDURE R$O$SP$CRIA_GATILHO_REP_UPDATE(OBJETO);
				EXECUTE PROCEDURE R$O$SP$CRIA_GATILHO_REP_DELETE(OBJETO);
			END
		END
	END
END^