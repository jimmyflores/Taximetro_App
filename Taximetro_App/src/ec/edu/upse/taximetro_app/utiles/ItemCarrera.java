package ec.edu.upse.taximetro_app.utiles;

public class ItemCarrera {
	private int idCarrera;
	private String Nombre;
	private String Apellido;
	private String origen;
	private String destino; 
	private Double costo;
	private Double km;
	private String fecha;
	private Double latitud;
	private Double longitud;
	public ItemCarrera(int idCarrera, String nombre, String apellido,
			String origen, String destino, Double costo, Double km,
			String fecha, Double latitud, Double longitud) {
		super();
		this.idCarrera = idCarrera;
		Nombre = nombre;
		Apellido = apellido;
		this.origen = origen;
		this.destino = destino;
		this.costo = costo;
		this.km = km;
		this.fecha = fecha;
		this.latitud = latitud;
		this.longitud = longitud;
	}
	public ItemCarrera() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getIdCarrera() {
		return idCarrera;
	}
	public void setIdCarrera(int idCarrera) {
		this.idCarrera = idCarrera;
	}
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public String getApellido() {
		return Apellido;
	}
	public void setApellido(String apellido) {
		Apellido = apellido;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	public Double getKm() {
		return km;
	}
	public void setKm(Double km) {
		this.km = km;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Double getLatitud() {
		return latitud;
	}
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	public Double getLongitud() {
		return longitud;
	}
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	
	
	
}
