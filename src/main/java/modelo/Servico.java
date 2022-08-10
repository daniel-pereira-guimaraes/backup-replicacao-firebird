package modelo;

public abstract class Servico extends Cadastro {
	
	private String maquina;
	private Integer porta;
	private String endereco;
	private String usuario;
	private String senha;
				
	public Servico() {
		super();
	}
	
	public Servico(Servico servico) {
		super(servico);
		this.maquina = servico.getMaquina();
		this.porta = servico.getPorta();
		this.endereco = servico.getEndereco();
		this.usuario = servico.getUsuario();
		this.senha = servico.getSenha();
	}
	
	public String getMaquina() {
		return maquina;
	}

	public void setMaquina(String maquina) {
		this.maquina = maquina;
	}

	public Integer getPorta() {
		return porta;
	}

	public void setPorta(Integer porta) {
		this.porta = porta;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
