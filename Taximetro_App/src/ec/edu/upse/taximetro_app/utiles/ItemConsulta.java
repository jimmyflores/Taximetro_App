package ec.edu.upse.taximetro_app.utiles;

public class ItemConsulta {

	private String Origen;
	private String Destino;
    private Double Distancia;
    private Double Valor;
	
    public ItemConsulta(String origen, String destino, Double valor, Double distancia) {
    	this.Origen = origen;
    	this.Destino = destino;
    	this.Distancia = distancia;
    	this.Valor = valor;
	}
    

    public ItemConsulta() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getOrigen() {
		return Origen;
	}

	public void setOrigen(String origen) {
		Origen = origen;
	}

	public String getDestino() {
		return Destino;
	}

	public void setDestino(String destino) {
		Destino = destino;
	}
	
	public Double getDistancia() {
		return Distancia;
	}

	public void setDistancia(Double distancia) {
		Distancia = distancia;
	}
	
	public Double getValor() {
		return Valor;
	}

	public void setValor(Double valor) {
		Valor = valor;
	}
    
}
