package ec.edu.upse.taximetro_app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.modelo.Usuario;
import ec.edu.upse.taximetro_app.servicio.ServicioTaximetro;
import ec.edu.upse.taximetro_app.servicio.ServicioTaximetro2;
import ec.edu.upse.taximetro_app.utiles.ItemDeUsuario;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.UserHandle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.test.ServiceTestCase;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {    
	//variables para el web service
	HttpTransportSE transporte;
	SoapObject request;
	SoapSerializationEnvelope sobre;
	SoapPrimitive resultado;
	
	//atributos
	private String NAMESPACE= "http://funciones.servicios.com/";
	private String URL = "http://10.0.2.2:8080/WebServiceSAAP/services/RegistrarCarreras";
	private String SOAP_ACTION;
	private String METODO;
	
	
	private EditText editTextUsuario, editTextPassword;
	private Button btn_Acceder;
    String Salida;
    String respuesta;
    ServicioTaximetro MyService;
	@Override
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this,ServicioTaximetro.class));
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
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(getIntent());
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
    		if(!Nombre.equals("") && !Clave.equals("")){
    			//if(verificarConexion(this)){
    				if(consultaLoginWS(Nombre, Clave)){
    					Intent intent =new Intent(this,FuncionesActivity.class);
    					startActivity(intent);
    				}else{
    		    		Toast.makeText(this, "El Usuario/Clave es incorrecta o el usuario no está registrado", Toast.LENGTH_LONG).show();
    				}
    			//}
    			//	else{
    			//	DBTaximetro dbTaxi = new DBTaximetro();
    			//	Usuario user = dbTaxi.Listalogin(this, Nombre, Clave);
    			//	if (user == null)
    			//	{
    			//		Toast.makeText(this, "El Usuario/Clave es incorrecta o el usuario no está registrado", Toast.LENGTH_LONG).show();
    			//	}
    		//	}
    				
    		}else{
    			Toast.makeText(this, "No se puede iniciar con campos vacios", Toast.LENGTH_LONG).show();
    		}
    		
    	/*DBTaximetro dbTaxi = new DBTaximetro();
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
    	}*/
	}
	
	private Boolean consultaLoginWS(String usuario, String contrasenia){
		Boolean respuesta=false;
		METODO = "iniciarsesion";
		SOAP_ACTION = NAMESPACE + METODO;
		String cadena = "";
		try {		
			request = new SoapObject(NAMESPACE, METODO);
			request.addProperty("user",usuario);
			request.addProperty("contrasena",contrasenia);
			sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sobre.dotNet = true;
			sobre.setOutputSoapObject(request);
		
			//habilitar la comunicacion del webservice con el activitie
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			
			transporte = new HttpTransportSE(URL);
			transporte.call(SOAP_ACTION, sobre);
			resultado = (SoapPrimitive) sobre.getResponse();
			cadena = resultado.toString();
			System.out.println("--> " + cadena);
			if(cadena.equalsIgnoreCase("true")){
				respuesta = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return respuesta;
	}
	
	public void Limpiar(){
		editTextUsuario.setText("");
		editTextPassword.setText("");
	} 
	
public static boolean verificarConexion(Context ctx)
{
	boolean bConectado =false;
		ConnectivityManager connec = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		//no solo wifi, tambien GPRS
		NetworkInfo[] redes = connec.getAllNetworkInfo();
		for(int i =0; i<2; i++)
		{
			if(redes[i].getState()==NetworkInfo.State.CONNECTED)
			{
				bConectado=true;
			}
		}
		
	return bConectado;
}
}

