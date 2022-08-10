package modelo;

import java.time.LocalDateTime;
import java.time.LocalTime;

import geral.Constante;

public class Tarefa extends Cadastro {
	
	public static enum Tipo { 
		BACKUP(1), REPLICACAO(2); 
		
		private Integer id;
		
		Tipo(Integer id) {
			this.id = id;
		}
		
		public Integer getId() {
			return this.id;
		}
		
		@Override
		public String toString() {
			if (this == Tipo.BACKUP)
				return Constante.STR_BACKUP;
			else
				return Constante.STR_REPLICACAO;
		}
		
		public static Tipo fromId(Integer id) {
			for (Tipo tipo : Tipo.values()) 
				if (tipo.getId() == id)
					return tipo;
			return null;
		}
		
		public static Tipo fromString(String s) {
			for (Tipo tipo : Tipo.values()) 
				if (tipo.toString().equals(s))
					return tipo;
			return null;
		}
		
	}
		
	private String uuid;
	private Tipo tipo = Tipo.BACKUP;
	private Cadastro bancoDados = new Cadastro();
	private Integer intervalo = 60;
	private LocalTime horaInicial = LocalTime.parse("00:00:00");
	private LocalTime horaFinal = LocalTime.parse("23:59:59");
	private Boolean domingo = true;
	private Boolean segunda = true;
	private Boolean terca = true;
	private Boolean quarta = true;
	private Boolean quinta = true;
	private Boolean sexta = true;
	private Boolean sabado = true;
	private LocalDateTime executada;
	
	public Tarefa() {
		super();
	}
	
	public Tarefa(Tarefa.Tipo tipo) {
		super();
		this.tipo = tipo;
	}
	
	public Tarefa(Tarefa tarefa) {
		super(tarefa);
		this.tipo = tarefa.getTipo();
		this.bancoDados.setId(tarefa.getBancoDados().getId());
		this.bancoDados.setNome(tarefa.getBancoDados().getNome());
		this.intervalo = tarefa.getIntervalo();
		this.horaInicial = tarefa.getHoraFinal();
		this.horaFinal = tarefa.getHoraFinal();
		this.domingo = tarefa.isDomingo();
		this.segunda = tarefa.isSegunda();
		this.terca = tarefa.isTerca();
		this.quarta = tarefa.isQuarta();
		this.quinta = tarefa.isQuinta();
		this.sexta = tarefa.isSexta();
		this.sabado = tarefa.isSabado();
	}
		
	public String getUUID() {
		return uuid;
	}
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Cadastro getBancoDados() {
		return bancoDados;
	}
	
	public int getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(Integer intervalo) {
		this.intervalo = intervalo;
	}

	public LocalTime getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(LocalTime horaInicial) {
		this.horaInicial = horaInicial;
	}

	public LocalTime getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(LocalTime horaFinal) {
		this.horaFinal = horaFinal;
	}

	public boolean isDomingo() {
		return domingo;
	}

	public void setDomingo(Boolean domingo) {
		this.domingo = domingo;
	}

	public boolean isSegunda() {
		return segunda;
	}

	public void setSegunda(Boolean segunda) {
		this.segunda = segunda;
	}

	public boolean isTerca() {
		return terca;
	}

	public void setTerca(Boolean terca) {
		this.terca = terca;
	}

	public boolean isQuarta() {
		return quarta;
	}

	public void setQuarta(Boolean quarta) {
		this.quarta = quarta;
	}

	public boolean isQuinta() {
		return quinta;
	}

	public void setQuinta(Boolean quinta) {
		this.quinta = quinta;
	}

	public boolean isSexta() {
		return sexta;
	}

	public void setSexta(Boolean sexta) {
		this.sexta = sexta;
	}

	public boolean isSabado() {
		return sabado;
	}

	public void setSabado(Boolean sabado) {
		this.sabado = sabado;
	}

	public LocalDateTime getExecutada() {
		return executada;
	}

	public void setExecutada(LocalDateTime executada) {
		this.executada = executada;
	} 
	
}
