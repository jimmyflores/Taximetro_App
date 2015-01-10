package ec.edu.upse.taximetro_app;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.modelo.Usuario;
import ec.edu.upse.taximetro_app.utiles.ItemDeUsuario;
import ec.edu.upse.taximetro_app.ws.ManejadorWS;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {    
	private EditText editTextUsuario, editTextPassword;
    
	private Button btn_Acceder;
    String Salida;
    String respuesta;
    
	@Override
    
	protected void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
    
	}


    
	@Override
    
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    
	}
    
	public void Registrarse_Evento(View boton){
		
		Intent intent = new Intent(this,RegistroActivity.class);
		startActivity(intent);
	} 
    
	public void Inicializar(){
    	
		editTextUsuario = (EditText) findViewById(R.id.editTextUsuario);
    	
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		
	
	}
    
	public void Acceder_Evento(View boton)
    {
		Inicializar();
    	String Nombre = editTextUsuario.getText().toString();
    	String Clave = editTextPassword.getText().toString();
    	DBTaximetro dbTaxi = new DBTaximetro();
    	Usuario user = dbTaxi.Listalogin(this, Nombre, Clave);
    	if (user == null)
    	{
    		Toast.makeText(this, "El Usuario/Clave es incorrecta o el usuario no está registrado", Toast.LENGTH_LONG).show();
    	}
    	
    	else
    	{
    			
    		Intent intent =new Intent(this,FuncionesActivity.class);
    		intent.putExtra("id_usuario", ""+user.getId());
    		intent.putExtra("usuario", user.getNombre_usuario());
    		//Toast.makeText(this, "usuario: "+user.getId(), Toast.LENGTH_LONG).show();
    		startActivity(intent);
    		Limpiar();   	
    	}
	}
    
    public void coneccion_ws(){
    	HttpURLConnection conexion = null;
    		
    	try {
			URL url = new URL("http://direccion_ws/servicio_login");
		//
			conexion = (HttpURLConnection) url.openConnection();
			
				conexion.setRequestMethod("POST");
			
			conexion.setDoOutput(true);
			
			// Asignación de parámetros de entrada al servicio.
			OutputStreamWriter sal= new OutputStreamWriter(conexion.getOutputStream());
			sal.write("msg=");
			sal.write(URLEncoder.encode("usuario - contrasenia ","UTF-8") );
			sal.flush();
			if( conexion.getResponseCode() == HttpURLConnection.HTTP_OK){
				
				//muestra mensaje de que se conecto correctamente
				SAXParserFactory fabrica = SAXParserFactory.newInstance();
				SAXParser parser = fabrica.newSAXParser();
				XMLReader lector = parser.getXMLReader();
				
				ManejadorWS manejadorXML = new ManejadorWS();
				lector.setContentHandler( manejadorXML );
				lector.parse( new InputSource(conexion.getInputStream()) );
				
				respuesta = manejadorXML.echo();
				
				}
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    }
    
	public void Limpiar(){
		
		editTextUsuario.setText("");
		
		editTextPassword.setText("");
	
	} 
    

}

