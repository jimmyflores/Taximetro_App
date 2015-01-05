package ec.edu.upse.taximetro_app;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.modelo.Tarifa;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
    Chronometer CronometroTiempo;
    
		// daclarar variable que representa al mapa
    	Integer id_usuario;
    	String nombre_usuario;
		Location locationI, locationF;
		LocationManager locationManager;
		String proveedor;
		PolylineOptions polilinea_options;
		LatLng latlng;
		Polyline polilinea;
		
		//variables de ubicacion
		double latitud_inicio, longitud_inicio, latitud_final, longitud_final, distancia_total=0;
		
		//variables para el calculo de la carrera
		int segundos_consumidos=0, total_segundos_p=0;
		Tarifa tarifa = new Tarifa();
		Double Tarifa_arranque, Tarifa_minima, costo_km, costo_min_espera,costoTotalCarrera;
		String TipoTarifa;	
		Integer total_segundos_i;
		
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
				Tarifa_arranque = tarifa.getArranque_tarifa();
				Tarifa_minima = tarifa.getCarrera_min();
				costo_km = tarifa.getKm_recorrido();
				costo_min_espera = tarifa.getMin_espera();
				TipoTarifa = tarifa.getDescripcion();
				//Toast.makeText(this,""+v_hora, Toast.LENGTH_LONG).show();
				costoTotalCarrera = 0.0;
				costoTotalCarrera = costoTotalCarrera + Tarifa_arranque;
			}	
	}
	
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
//	 	mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentMapas)).getMap();
 	}
	
	
	public void Limpiar(){
		et_Km.setText("");
		et_$.setText("");
		et_Partida.setText("");
		et_Llegada.setText("");
	} 
	
	public void ON_OFF(View v){				
		if(button_O_O.isChecked()){	// ON-----------------------------------------------------------------------------
			CronometroTiempo.setBase(SystemClock.elapsedRealtime());
			Limpiar();
			costoTotalCarrera = Tarifa_arranque;
			et_TipoTarifa.setText(TipoTarifa);
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
		if(costoTotalCarrera < Tarifa_minima ){
			costoTotalCarrera = Tarifa_minima;
			et_$.setText(""+costoTotalCarrera);
		}
		CronometroTiempo.stop();
		}
		else{
			Toast.makeText(this, "location es nula _btnOFF", Toast.LENGTH_SHORT).show();
		}
		}	
	}
	
	public String getFechaActual(){
				Calendar c1;
				 	c1 = Calendar.getInstance();	
			        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			        return sdf.format(c1.getTime());
		}
	float total_segundos=0;
	
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
	

 public void onGuardar(View boton){
		Inicializar();
		DBTaximetro dbTaxi = new DBTaximetro();
		if (locationI == null && isEmpty()){
    		crearMensaje(1, "Algun(os) Campo(s) stán vacios!!");
    	}else{
    		Double kilometrosRecorridos = Double.parseDouble(""+et_Km.getText());	
    		Double costoCarrera = Double.parseDouble(""+et_$.getText());
    		dbTaxi.nuevaCarrera(this, id_usuario , tarifa.getId(),
    		kilometrosRecorridos,costoCarrera,
    			""+et_Partida.getText(),latitud_inicio,longitud_inicio,
    			""+et_Llegada.getText(),latitud_final,longitud_final,getFechaActual(),""+CronometroTiempo.getText());
    			crearMensaje(1,"Carrera Guardada Exitosamente");
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

 // falta de terminar el registrar carrera..
 
 DecimalFormat df = new DecimalFormat("#.##");
	@Override
	public void onLocationChanged(Location location) {
		//Inicializar();
		// TODO Auto-generated method stub
				
				float velocidad = location.getSpeed();
				total_segundos =  (float) (total_segundos + 0.25);
				distancia_total = distancia_total + (velocidad * 0.25);
				Integer metros_comparacion = 0;
				Double cambio_velocidad = 3.33; //m/seg
				Toast.makeText(this, "VEL: " + velocidad +" m/s ...Seg: "+ total_segundos, Toast.LENGTH_LONG).show();	
				CronometroTiempo.start();
				if(TipoTarifa.equalsIgnoreCase("diurna")){
					metros_comparacion = 38; //metros
				}else{
					metros_comparacion = 33; //metros
				}
					// preguntar si la velocidad es mayor o igual de 12km/h = 3.333 m/seg
					if(velocidad >= cambio_velocidad){
						//el costo de la carrera se calcula por la distancia recorrida
						if(distancia_total%metros_comparacion == 0){
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
						
						if(total_segundos_i%10 == 0){
							costoTotalCarrera += 0.01;
						}
					}
				
				et_$.setText(""+df.format(costoTotalCarrera));
				velocidad =(float) 0.0;
				et_Km.setText(df.format(distancia_total/1000)+" Km");
				//PolylineOptions pol_options = new PolylineOptions().add(latlong).color(Color.RED);
				//Polyline polilinea2 = mapa.addPolyline(pol_options);
		
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
}

