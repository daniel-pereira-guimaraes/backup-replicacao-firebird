package modelo;

public class Cadastro extends Modelo {
	
	private String nome;
		
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Cadastro() {
		super();
	}
	
	public Cadastro(Cadastro cadastro) {
		super(cadastro);
		this.nome = cadastro.getNome();
	}
		
	@Override
	public String toString() {
		return getNome();
	}
	
}
