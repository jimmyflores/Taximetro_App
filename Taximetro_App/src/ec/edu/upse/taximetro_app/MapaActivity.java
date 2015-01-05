package ec.edu.upse.taximetro_app;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MapaActivity extends Activity implements LocationListener{
	
	//declarar variable que represente al mapa
		
		GoogleMap mapa;
		//variables par ala localizacion 
		Location location;
		LocationManager locationManager;
		String proveedor;
		
		public void onUbicar (View v){
			//capturar la ubicacion actual del dispositivo
			//1. obtener el administrador de localizacion 
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		//revisar si los servicios de localizacion esta habilitados
			//tipos de servicios de localizacion > GPS, WIFI
			boolean gpsHabilitado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkHabilitado = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			// ningun provide de localizacion esta habilitado 
			
			if(!gpsHabilitado&&!networkHabilitado)
			{
				Toast.makeText(this,"no provider habilitado",Toast.LENGTH_LONG).show();
				//LANZAR LA ACTIVIDAD DE CONFIGURACION DE FUENTES DE LOCALIZACION
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
				
			}
			
			//seleccionar un provider
			if(gpsHabilitado){
				proveedor = LocationManager.GPS_PROVIDER;
			}else{ 
				if(networkHabilitado)
				  {
	               proveedor =LocationManager.NETWORK_PROVIDER;   			
			      }
			}
			if(proveedor!=null){
				//es decir que alguno de los tipos de localzacion estan activados y podremos obtener la localizacion
				
				//esta es mi ubicacion si se obtenieron resultados
				location = locationManager.getLastKnownLocation(proveedor);
				
			}else{
				Toast.makeText(this,"proveedor es nulo",Toast.LENGTH_LONG).show();
				
			}
			if(location!=null){
				double lat = location.getLatitude();
				double longi = location.getLongitude();
				agregarMarca(lat, longi, "ubica", "Ubicacion actual");
				Toast.makeText(this,"lat:" +lat+ " " + "long: " +longi,Toast.LENGTH_LONG).show();	
			}else{
				Toast.makeText(this,"location es null",Toast.LENGTH_LONG).show();
				
			}
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);
		mapa = ((MapFragment)getFragmentManager().findFragmentById(R.id.fragmentMapas)).getMap();
		//comprobacion
		if(mapa==null){
			Toast.makeText(this, "no se pudo crear el mapa", Toast.LENGTH_LONG).show();
		}else
		{
			//configuraciones iniciales de los mapas
			mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			mapa.getUiSettings().setZoomControlsEnabled(true);
			mapa.getUiSettings().setCompassEnabled(true);
			
			agregarMarca(-33.796923, 150.922433, "Mi marca", "Esta es mi direccion");
		}
	}

	public void cambiarMapa(View v){
		ToggleButton button_tipo = (ToggleButton) findViewById(R.id.toggleButtonSat);
		
		if(button_tipo.isChecked()){
			//encendido on
			mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		}else
		{
			//apagado off
			mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			
		}
	}

public void irTaximetro(View boton){
	Intent intent =new Intent(this,TaxiActivity.class);
	startActivity(intent);
}

public void agregarMarca(double latitud, double longitud, String titulo, String mensaje){
	MarkerOptions marca = new MarkerOptions();
	LatLng ubicacion = new LatLng(latitud, longitud);
	marca.position(ubicacion);
	marca.title(titulo);
	marca.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
	
	marca.snippet(mensaje);
	
	//agregar la marca al mapa
	mapa.addMarker(marca);
	//animar camara hacia marca del mapa
	mapa.animateCamera(CameraUpdateFactory.newLatLng(ubicacion));
	
	
}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
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