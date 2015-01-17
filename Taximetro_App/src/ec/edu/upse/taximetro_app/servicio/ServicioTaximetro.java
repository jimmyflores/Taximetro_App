package ec.edu.upse.taximetro_app.servicio;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ec.edu.upse.taximetro_app.MainActivity;
import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.modelo.DBTaximetro_service;
import ec.edu.upse.taximetro_app.modelo.Usuario;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ServicioTaximetro extends Service{
	private Timer t = new Timer();
	private static final long UPDATE_INTERVAL = 10000;
	
	//variables para el web service
		HttpTransportSE transporte;
		SoapObject request;
		SoapSerializationEnvelope sobre;
		SoapPrimitive resultado;
		
	
	static String NAMESPACE = "http://funciones.servicios.com/";
	static String URL ="http://10.0.2.2:8080/WebServiceSAAP/services/RegistrarCarreras";
	String METODO;// = "NuevoClienteSimple";
	String SOAP_ACTION;// = "http://sgoliver.net/NuevoClienteSimple";
	
	@Override
	public IBinder onBind(Intent arg0){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Toast.makeText(getApplicationContext(), "---servicio iniciado....", Toast.LENGTH_LONG).show();
		EnviarDatos();
	}

	private static ServicioTaximetro instance  = null;
	
	public static boolean isRunning() { 
	      return instance != null; 
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
	
	
	public void EnviarDatos(){
		/*System.out.println("");
		t.scheduleAtFixedRate(new TimerTask() {		
			@Override
			public void run() {
				System.out.println("--- Servicio corriendo....");
				// TODO Auto-generated method stub
				if(!verificarConexion(ServicioTaximetro.this))
				{*/
					System.out.println("--- Tengo Internet");
					//conexion con el web service
					//para envio de datos guardados solo en la base local
					
					//Usuario guardados en la base local pero no enviados
					DBTaximetro_service db = new DBTaximetro_service();
					ArrayList<Usuario> listaUsuarios = db.UsuariosNoEnviados(ServicioTaximetro.this);
					Integer Cont_Usuarios = 0;
					if(listaUsuarios != null){
						//envio de Datos
						System.out.println("--- numero de usuarios = " + listaUsuarios.size() );
						for (int i= 0; i<listaUsuarios.size();i++){
							if(EnviarUsuarios(listaUsuarios.get(i))){
								Cont_Usuarios += 1;
								System.out.println("--> cont: "+Cont_Usuarios);
								ActualizarUsuario(listaUsuarios.get(i));
							}
						}
					}/*
				}
			}
		}, 0, UPDATE_INTERVAL);*/
	}
	
	String strJSON;
	
	public Boolean EnviarUsuarios(Usuario user){
		Boolean result = false;
	    try {
	    	METODO = "registrarusuario";
			SOAP_ACTION = NAMESPACE + METODO;
			String cadena = "";
			try {		
				request = new SoapObject(NAMESPACE, METODO);
				request.addProperty("nombres",user.getPersona().getNombres());
		        request.addProperty("apellidos",user.getPersona().getApellidos());
		        request.addProperty("correo",user.getPersona().getEmail());
		        request.addProperty("cedula"," ");
		        request.addProperty("user",user.getNombre_usuario());
		        request.addProperty("contrasena",user.getClave());
				sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				sobre.dotNet = true;
				sobre.setOutputSoapObject(request);
			
				//habilitar a comunicacion del webservice con la activty
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			
				transporte = new HttpTransportSE(URL);
			
				transporte.call(SOAP_ACTION, sobre);
				resultado = (SoapPrimitive) sobre.getResponse();
				cadena = resultado.toString();
				System.out.println("--> " + cadena);
				if(cadena.equals("false")){
					result = false;
				}else{
					result = true;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    //Simulando un mayor tiempo de tardanza del webservice
	    //Ya que el webservice y la app se ejecutan en la misma PC el tiempo de respuesta seria super rapido
	    try {
	      Thread.sleep(2000);
	    } catch (InterruptedException e) {      
	      e.printStackTrace();
	    }    
	    return result;
	    
	}
	
	public void ActualizarUsuario(Usuario user){
		DBTaximetro_service db = new DBTaximetro_service();
		db.ActualizarEnvios(this, user);	
	}
	
	Gson gson = null;
	
	private void crearLista(String strJson){
		//se crea el objeto que ayuda deserealizar la cadena JSON
		gson = new Gson();

		//Obtenemos el tipo de un ArrayList
		Type lstT = new TypeToken< ArrayList>(){}.getType();

		//Creamos una objeto ArrayList
		ArrayList arrListAOS = new ArrayList();

		//Deserealizamos la cadena JSON para que se convertida a un ArrayList
		arrListAOS = gson.fromJson(strJson, lstT);
		Log.i("Local Service", "Datos recividos correctamente");
		//Asignaos la ArrayList al controls ListView para mostrar
		//la lista de SO Android que se consumieron del web service
		//lista.setAdapter(new ArrayAdapter (getApplication(), android.R.layout.simple_list_item_1, arrListAOS));
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	@Override
	public ComponentName startService(Intent service) {
		// TODO Auto-generated method stub
		return super.startService(service);
	}

	@Override
	public boolean stopService(Intent name) {
		// TODO Auto-generated method stub
		return super.stopService(name);
	}
}
