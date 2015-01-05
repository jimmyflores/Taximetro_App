package ec.edu.upse.taximetro_app.modelo;
/*
 String sql = "CREATE TABLE personas (" +
			" id_p INTEGER PRIMARY KEY AUTOINCREMENT," +
			" nombres TEXT," +
			" apellidos TEXT," +
			" email TEXT," +
			" estado TEXT )";
	
 */

public class Persona {
	private Integer id;
	private String nombres;
	private String apellidos;
	private String email;
	private String estado;
	
	public Persona() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Persona(Integer id, String nombres, String apellidos, String email,
			String estado) {
		super();
		this.id = id;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.email = email;
		this.estado = estado;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	
}
