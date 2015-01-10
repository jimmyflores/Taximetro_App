package ec.edu.upse.taximetro_app.ws;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ManejadorWS extends DefaultHandler {

	private String echo;
	private boolean recibido;
	

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		echo = "Inicializado manejador.";
		recibido = false;
	}

	
	
	/**
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		echo = new String(ch);
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String nombreLocal, String qName)
			throws SAXException {
		
		if(nombreLocal.equals("return")){
			recibido = true;
		}
		
	}

	
	public String echo(){
		return echo;
	}

	public boolean isRecibido() {
		return recibido;
	}	
	
}
