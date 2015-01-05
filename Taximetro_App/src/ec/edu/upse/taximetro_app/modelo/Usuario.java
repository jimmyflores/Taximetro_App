package ec.edu.upse.taximetro_app.modelo;

/*
 * String sql1 = "CREATE TABLE usuarios (" +
			" id_u INTEGER PRIMARY KEY AUTOINCREMENT," +
			" id_persona INTEGER NOT NULL," +
			" usuario TEXT," +
			" clave TEXT," +
			" estado TEXT, " +
			"FOREIGN KEY (id_persona) REFERENCES personas(id_p))";
	
 * */
public class Usuario {
	private Integer id;
	private Persona persona;
	private String nombre_usuario;
	private String clave;
	private String estado;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public String getNombre_usuario() {
		return nombre_usuario;
	}
	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Usuario() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Usuario(Integer id, Persona persona, String nombre_usuario,
			String clave, String estado) {
		super();
		this.id = id;
		this.persona = persona;
		this.nombre_usuario = nombre_usuario;
		this.clave = clave;
		this.estado = estado;
	}
	
}
