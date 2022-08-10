package modelo;

public abstract class Modelo {
	
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Modelo() {
		super();
	}
	
	public Modelo(Modelo modelo) {
		super();
		this.id = modelo.getId();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Modelo)
			return this.id.equals(((Modelo) obj).getId());
		else
			return super.equals(obj);
	}
}
