package ec.edu.upse.taximetro_app.modelo;

public class Carrera {
	private int idCarrera;
	private int idPersonas;
	private String Usuario;
	private int idTarifa;
	private Double km;
	private Double valor;
	private String origen;
	private Double latitud_origen;
	private Double longitud_origen;
	private String destino;
	private Double latitud_destino; 
	private Double longitud_destino;
	private String fecha;
	private String Tiempo_carrera;
	
	public Carrera() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getUsuario() {
		return Usuario;
	}

	public void setUsuario(String usuario) {
		this.Usuario = usuario;
	}
	
	public Carrera(int idCarrera, int idPersonas, int idTarifa, Double km,
			Double valor, String origen, Double latitud_origen,
			Double longitud_origen, String destino, Double latitud_destino,
			Double longitud_destino, String fecha, String tiempo_carrera) {
		super();
		this.idCarrera = idCarrera;
		this.idPersonas = idPersonas;
		this.idTarifa = idTarifa;
		this.km = km;
		this.valor = valor;
		this.origen = origen;
		this.latitud_origen = latitud_origen;
		this.longitud_origen = longitud_origen;
		this.destino = destino;
		this.latitud_destino = latitud_destino;
		this.longitud_destino = longitud_destino;
		this.fecha = fecha;
		Tiempo_carrera = tiempo_carrera;
	}
	public int getIdCarrera() {
		return idCarrera;
	}
	public void setIdCarrera(int idCarrera) {
		this.idCarrera = idCarrera;
	}
	public int getIdPersonas() {
		return idPersonas;
	}
	public void setIdPersonas(int idPersonas) {
		this.idPersonas = idPersonas;
	}
	public int getIdTarifa() {
		return idTarifa;
	}
	public void setIdTarifa(int idTarifa) {
		this.idTarifa = idTarifa;
	}
	public Double getKm() {
		return km;
	}
	public void setKm(Double km) {
		this.km = km;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public Double getLatitud_origen() {
		return latitud_origen;
	}
	public void setLatitud_origen(Double latitud_origen) {
		this.latitud_origen = latitud_origen;
	}
	public Double getLongitud_origen() {
		return longitud_origen;
	}
	public void setLongitud_origen(Double longitud_origen) {
		this.longitud_origen = longitud_origen;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
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
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getTiempo_carrera() {
		return Tiempo_carrera;
	}
	public void setTiempo_carrera(String tiempo_carrera) {
		Tiempo_carrera = tiempo_carrera;
	}
	
	}