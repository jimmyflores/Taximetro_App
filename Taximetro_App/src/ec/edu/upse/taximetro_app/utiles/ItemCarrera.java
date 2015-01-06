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
	private Double latitud_destino;
	private Double longitud_destino;
	public ItemCarrera() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ItemCarrera(int idCarrera, String nombre, String apellido,
			String origen, String destino, Double costo, Double km,
			String fecha, Double latitud, Double longitud,
			Double latitud_destino, Double longitud_destino) {
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
		this.latitud_destino = latitud_destino;
		this.longitud_destino = longitud_destino;
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
	public Double getLatitud_destino() {
		return latitud_destino;
	}
	public void setLatitud_destino(Double latitud_destino) {
		this.latitud_destino = latitud_destino;
	}
	public Double getLongitud_destino() {
		return longitud_destino;
	}
	public void setLongitud_destino(Double longitud_destino) {
		this.longitud_destino = longitud_destino;
	}
		
}
