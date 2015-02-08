package ec.edu.upse.taximetro_app;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import ec.edu.upse.taximetro_app.modelo.Carrera;
import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.modelo.Tarifa;
import ec.edu.upse.taximetro_app.servicio.ConexionWebService;
import ec.edu.upse.taximetro_app.utiles.MarshalDouble;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TaxiActivity extends Activity implements LocationListener{
	
	
	TextView et_Km, et_$, et_TipoTarifa;
	EditText et_Partida, et_Llegada;
	Button Guardar, Cancelar;
	ToggleButton button_O_O;
	Button btnguardar;
    Chronometer CronometroTiempo;
    
    //parametros
    Integer id_usuario;
    String nombre_usuario;
    
    // daclarar variable que representa al mapa
	Location locationI, locationF;
	LocationManager locationManager;
	String proveedor;
	PolylineOptions polilinea_options;
	LatLng latlng;
	Polyline polilinea;
		
	//variables Web Service
	private String SOAP_ACTION;
	private String METODO;
		HttpTransportSE transporte;
		SoapObject request;
		SoapSerializationEnvelope sobre;
		SoapPrimitive result;
	
	
	//variables de ubicacion
	double latitud_inicio, longitud_inicio, latitud_final, longitud_final, distancia_total=0;
		
	//variables para el calculo de la carrera
	int segundos_consumidos=0, total_segundos_p=0;
	Tarifa tarifa = new Tarifa();
	Double Costo_Tarifa_Arranque, Costo_Tarifa_minima, costo_km, costo_min_espera,costoTotalCarrera;
	String TipoTarifa;	
	Integer total_segundos_i;
	float total_segundos = 0;	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.taxi, menu);
		return true;
	}
	
	public void Inicializar(){
		et_TipoTarifa = (TextView) findViewById(R.id.textViewTipoTarifa);
		CronometroTiempo = (Chronometer) findViewById(R.id.CronometroTiempo);
		et_Km = (TextView) findViewById(R.id.textViewkilo);
		et_$ = (TextView) findViewById(R.id.textViewCosto);
		et_Partida = (EditText) findViewById(R.id.editTextPar);
		et_Llegada = (EditText) findViewById(R.id.editTextLleg);
		button_O_O = (ToggleButton) findViewById(R.id.toggleButtonSat);
		btnguardar = (Button) findViewById(R.id.buttonGuardar);
 	}
	
	public void Limpiar(){
		et_Km.setText("");
		et_$.setText("");
		et_Partida.setText("");
		et_Llegada.setText("");
	} 
	
	public String getFechaActual(){
		Calendar c1;
		 	c1 = Calendar.getInstance();	
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        return sdf.format(c1.getTime());
	}


	
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_taxi);
			Intent intentActual = this.getIntent();
					try {
						id_usuario = Integer.parseInt(intentActual.getStringExtra("id_usuario"));
						nombre_usuario = intentActual.getStringExtra("usuario");
						latitud_inicio = Double.parseDouble(intentActual.getStringExtra("latitud_inicio"));
						longitud_inicio = Double.parseDouble(intentActual.getStringExtra("longitud_inicio"));
					} catch (Exception e) {
						e.printStackTrace();
					}
			Inicializar();
			DBTaximetro db = new DBTaximetro();
			Date hora =new Date();
			Integer v_hora = hora.getHours();
				
				tarifa = db.selectAllTarifa(this, v_hora);
				if(tarifa != null){
					Costo_Tarifa_Arranque = tarifa.getArranque_tarifa();
					Costo_Tarifa_minima = tarifa.getCarrera_min();
					costo_km = tarifa.getKm_recorrido();
					costo_min_espera = tarifa.getMin_espera();
					TipoTarifa = tarifa.getDescripcion();
					costoTotalCarrera = 0.0;
					costoTotalCarrera = costoTotalCarrera + Costo_Tarifa_Arranque;
					et_TipoTarifa.setText(TipoTarifa);
				}	
		}
	 
	 public void onGuardar(View boton){
			Inicializar();
			String estado_envio  = "NE";
			DBTaximetro dbTaxi = new DBTaximetro();
			if (locationI == null && isEmpty()){
	    		crearMensaje(1, "Algun(os) Campo(s) stán vacios!!");
	    	}else{
	    		
	    		Double kilometrosRecorridos = Double.parseDouble(""+et_Km.getText());	
	    		Double costoCarrera = Double.parseDouble(""+et_$.getText());
	    		
	    		if (ConexionWebService.VerificaConexion(this)){
	    			Carrera race = new Carrera();
	   	    		race.setUsuario(nombre_usuario);
	   	    		race.setIdTarifa(tarifa.getId());
	   	    		race.setKm(kilometrosRecorridos);
	   	    		race.setValor(costoCarrera);
	   	    		race.setOrigen(et_Partida.getText().toString());
	   	    		race.setDestino(et_Llegada.getText().toString());
	   	    		race.setLatitud_origen(latitud_inicio);
	   	    		race.setLongitud_origen(longitud_inicio);
	   	    		race.setLatitud_destino(latitud_final);
	   	    		race.setLongitud_destino(longitud_final);
	   	    		race.setTiempo_carrera(CronometroTiempo.getText().toString());
	   	    		race.setFecha(getFechaActual());
	   	    		
	   	    		if (EnviarCarrera(race) == 1){
	   	    			estado_envio = "E";
	   	    			crearMensaje(1, "Carrera Registrada Con exito _WS");
	   	    		}
	   	    		
	   	    	}
	    		
	    		dbTaxi.nuevaCarrera(this, id_usuario , tarifa.getId(),
	    	    		kilometrosRecorridos,costoCarrera,
	    	    			""+et_Partida.getText(),latitud_inicio,longitud_inicio,
	    	    			""+et_Llegada.getText(),latitud_final,longitud_final,getFechaActual(),
	    	    			""+CronometroTiempo.getText(),estado_envio);
	    	    crearMensaje(1,"Carrera Guardada Exitosamente _host");
	    		Limpiar();
	    	}	
		}
	 

	public void crearMensaje(int numBotones, String Mensaje){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(Mensaje);
		dialog.setCancelable(false);
		if(numBotones == 1){
			dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				  @Override
				  public void onClick(DialogInterface dialog, int which) {
				    
				  }
				});	
			
		}
		if(numBotones == 2){
			dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
				  @Override
				  public void onClick(DialogInterface dialog, int which) {
				    Intent intent = new Intent(TaxiActivity.this,FuncionesActivity.class);
				    startActivity(intent);
				  }
				});	
			dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
				  @Override
				  public void onClick(DialogInterface dialog, int which) {
				    
				  }
				});	
		}
		dialog.show();
	}
	
	public void ON_OFF(View v){				
		if(button_O_O.isChecked()){	// ON-----------------------------------------------------------------------------
			btnguardar.setEnabled(false);
			CronometroTiempo.setBase(SystemClock.elapsedRealtime());
			Limpiar();
			
			costoTotalCarrera = Costo_Tarifa_Arranque;
			et_$.setText(""+costoTotalCarrera);
			
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			boolean gpsHabilitado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkHabilitado = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if(!gpsHabilitado && !networkHabilitado)
			{
				Toast.makeText(this,"no provider habilitado",Toast.LENGTH_LONG).show();
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);	
			}
		

		// SELECCIONAR UN PROVIDER
			if(gpsHabilitado){
				proveedor = LocationManager.GPS_PROVIDER;
			}else{
				if(networkHabilitado)
				  {
	               proveedor =LocationManager.NETWORK_PROVIDER;   			
			      }
			}
			
			if(proveedor !=null){ //SI ESTÁ ACTIVO EL GPS, SE PODRÁ OBTENER LA LOCACLIZACIÓN
				locationI = locationManager.getLastKnownLocation(proveedor); 
			}else{
				Toast.makeText(this, "proveedor es nulo _LocatonI", Toast.LENGTH_SHORT).show();
			}
		
			if(locationI !=null){	
				CronometroTiempo.start();
				latlng = new LatLng(latitud_inicio, longitud_inicio);
			
				//ACTUALIZACIÓN DE LA LOCALIZACIÓN...PROVEEDOR, MILISEGUNDOS, METROS, ACTIVIDAD
				locationManager.requestLocationUpdates(proveedor, 250, 0, this);
		}else{
			Toast.makeText(this, "location es nula _btnON", Toast.LENGTH_SHORT).show();
		}
		
	}else{
		// APAGADO OFF
		if(locationI !=null){
		locationF = locationManager.getLastKnownLocation(proveedor);
		latitud_final = locationF.getLatitude();
		longitud_final = locationF.getLongitude();
		
		et_Km.setText(df.format(distancia_total/1000)+"");
		
		locationI = null;
		locationF=null;
		//removeUpdates -> detener nuevas actualizaciones
		locationManager.removeUpdates(this);
		
		if(costoTotalCarrera < Costo_Tarifa_minima ){
			costoTotalCarrera = Costo_Tarifa_minima;
			et_$.setText(""+costoTotalCarrera);
		}
		CronometroTiempo.stop();
		}
		else{
			Toast.makeText(this, "location es nula _btnOFF", Toast.LENGTH_SHORT).show();
		}
		btnguardar.setEnabled(true);
		}	
	}
	
	
	public void onCancelar(View boton){
		crearMensaje(2, "¿Seguro que desea Cancelar la operación y salir?");
	}
	
	public boolean isEmpty(){
		if(et_Km.getText().toString().equals("")|| et_$.getText().toString().equals("") || et_Partida.getText().toString().equals("") || et_Llegada.getText().toString().equals("")){
			return true;
		}
		else{
			return false;
		}
	}

 // falta de terminar el registrar carrera..
 
 
DecimalFormat df = new DecimalFormat("#.##");
	@Override
	public void onLocationChanged(Location location) {
		Inicializar();
		// TODO Auto-generated method stub
				
				float velocidad = location.getSpeed();
				total_segundos =  (float)(total_segundos + 0.25);
				distancia_total = distancia_total + (velocidad * 0.25);
				Integer metros_comparacion = 0;
				Double cambio_velocidad = 3.33; //m/seg
				CronometroTiempo.start();
				if(TipoTarifa.equalsIgnoreCase("diurna")){
					metros_comparacion = 38; //metros
				}else{
					metros_comparacion = 33; //metros
				}
					// preguntar si la velocidad es mayor o igual de 12km/h = 3.333 m/seg
					if(velocidad >= cambio_velocidad){
						//el costo de la carrera se calcula por la distancia recorrida
						if(distancia_total == metros_comparacion || distancia_total%metros_comparacion == 0){
							costoTotalCarrera += 0.01;
						}
						//costoTotalCarrera
					}
					if(velocidad <= cambio_velocidad){
						//el costo de la carrera se calcula por el tiempo transcurrido
						String tiempo_i = CronometroTiempo.getText().toString();
						int minutos_i = Integer.valueOf(tiempo_i.substring(0,1));
						int segundos_i = Integer.valueOf(tiempo_i.substring(3,4));
						total_segundos_i = (minutos_i * 60) + segundos_i;
						
						if(total_segundos == 10 || total_segundos_i%10 == 0){
							costoTotalCarrera += 0.01;
						}
					}
				
				et_$.setText(""+df.format(costoTotalCarrera));
				
				et_Km.setText(df.format(distancia_total/1000)+" Km");
	}
         
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	//registro de la carrera en el web Service
	public Integer EnviarCarrera(Carrera carrera){
		Integer resultado_metodo = 0;
		String cadena = "";
		String URL = ConexionWebService.getUrlRegistrar();
		String NAMESPACE = ConexionWebService.getNamespace();
		METODO = "registrarcarrera";
		SOAP_ACTION = NAMESPACE + METODO;
		
			request = new SoapObject(NAMESPACE, METODO);
			
	         request.addProperty("id_tarifa",carrera.getIdTarifa());
			 request.addProperty("user",carrera.getUsuario().toString());
			 request.addProperty("origen",carrera.getOrigen().toString());
			 request.addProperty("destino",carrera.getDestino().toString());
			 request.addProperty("tiempo",carrera.getTiempo_carrera().toString());
			
			 
			 PropertyInfo info = new PropertyInfo();
			 info.setName("km_recorridos");
			 info.setValue(carrera.getKm());
			 info.setType(Double.class);
			 request.addProperty(info);
			 
			 
			 info = new PropertyInfo();
			 info.setName("precio");
			 info.setValue(carrera.getValor());
			 info.setType(Double.class);
			 request.addProperty(info);
			 
		     request.addProperty("fecha",carrera.getFecha());
		     
		     info = new PropertyInfo();
			 info.setName("latitud_origen");
			 info.setValue(carrera.getLatitud_origen());
			 info.setType(Double.class);
			 request.addProperty(info);
			 
			 info = new PropertyInfo();
			 info.setName("longitud_origen");
			 info.setValue(carrera.getLongitud_origen());
			 info.setType(Double.class);
			 request.addProperty(info);
		     
		     
			 info = new PropertyInfo();
			 info.setName("latitud_destino");
			 info.setValue(carrera.getLatitud_destino());
			 info.setType(Double.class);
		     request.addProperty(info);
			 
			 info = new PropertyInfo();
			 info.setName("longitud_destino");
			 info.setValue(carrera.getLongitud_destino());
			 info.setType(Double.class);
			 request.addProperty(info);
		     
	        
		     sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				sobre.dotNet = true;
				sobre.encodingStyle = SoapSerializationEnvelope.XSD;
				sobre.setOutputSoapObject(request);
				
				MarshalDouble md = new MarshalDouble();
				md.register(sobre);
		
			//habilitar a comunicacion del webservice con la activty
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				
				transporte = new HttpTransportSE(URL);
				
				try {
					
					transporte.call(SOAP_ACTION, sobre);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					
					result =  (SoapPrimitive) sobre.getResponse();
				
				} catch (SoapFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cadena = resultado_metodo.toString();
				if(cadena.equals("0")){
					resultado_metodo = 0;
				}
				if(cadena.equals("1")){
					resultado_metodo = 1;
				}
				
				if(cadena.equals("2")){
					resultado_metodo = 2;
				}
		
		return resultado_metodo;
	}
	
}

