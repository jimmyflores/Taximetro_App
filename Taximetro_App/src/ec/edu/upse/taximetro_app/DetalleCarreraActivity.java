package ec.edu.upse.taximetro_app;

import com.google.android.gms.internal.bd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.servicio.ConexionWebService;
import ec.edu.upse.taximetro_app.utiles.ItemCarrera;


import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleCarreraActivity extends Activity implements 
LocationListener{
	GoogleMap mapa;
	Location location;
	LocationManager locationManager;
	String proveedor,ruta;
	Integer car;
	String nombre,apellido,origen,destino,km,costo,fecha;
	String latitud,longitud,lat_dest,long_dest;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_carrera);
		
		try {
			Intent intent = this.getIntent();
			car = Integer.parseInt(intent.getStringExtra("carrera"));
			nombre=intent.getStringExtra("nombre");
			apellido=intent.getStringExtra("apellido");
			origen=intent.getStringExtra("origen");
			destino=intent.getStringExtra("destino");
			km=intent.getStringExtra("km");
			costo=intent.getStringExtra("costo");
			fecha=intent.getStringExtra("fecha");
			System.out.println("!!!localizacion: Lat,long = "+latitud.toString()+","+longitud.toString());
			latitud = intent.getStringExtra("lat");
			longitud =intent.getStringExtra("long");
			lat_dest = intent.getStringExtra("latdest");
			long_dest = intent.getStringExtra("longdest");
			System.out.println("!!!localizacion: Lat,long = "+latitud.toString()+","+longitud.toString());
			
					} catch (Exception e) {
			e.printStackTrace();
		}
	
		TextView textviewOrigen = (TextView) findViewById(R.id.textView01);
		TextView textviewNombre = (TextView) findViewById(R.id.textViewNombresD);
		TextView textviewApellido = (TextView) findViewById(R.id.textViewApellidosD);
		TextView textviewDestino = (TextView) findViewById(R.id.textViewD2);
		TextView textviewKm = (TextView) findViewById(R.id.textViewKm2);
		TextView textviewCosto = (TextView) findViewById(R.id.textViewCosto2);
		TextView textviewFecha = (TextView) findViewById(R.id.textViewFe);
		
		
		Intent intent = this.getIntent();
		if(!ConexionWebService.VerificaConexion(this))
		{
			DBTaximetro dbTaximetro = new DBTaximetro();
			ItemCarrera cl= dbTaximetro.Buscar(this, car);
		
			textviewApellido.setText(cl.getApellido());
			textviewNombre.setText(cl.getNombre());
			textviewOrigen.setText(cl.getOrigen());
			textviewDestino.setText(cl.getDestino());
			textviewKm.setText(cl.getKm().toString());
			textviewCosto.setText(cl.getCosto().toString());
			textviewFecha.setText(cl.getFecha());
		
			mapa = ((MapFragment)
				getFragmentManager().findFragmentById(R.id.fragmentMapaD)).getMap();
					
					// comprobacion
					if(mapa==null){
						Toast.makeText(this, "no se pudo crear mapa",
						Toast.LENGTH_LONG).show();
					}else{
						// configuraciones iniciales del mapa
						mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
						mapa.getUiSettings().setZoomControlsEnabled(true);
						mapa.getUiSettings().setCompassEnabled(true);
						
						agregarMarca(cl.getLatitud(), cl.getLongitud(),
								"Origen", "Direccion de Origen");
						agregarMarca(cl.getLatitud_destino(), cl.getLongitud_destino(),
								"Destino", "Direccion de Destino");
						
						
					}
		//agregarMarca(cl.getLatitud(), cl.getLongitud(), "ubica", 
			//	 "Ubicacion actual");*/
		}
		else
		{
			textviewApellido.setText(apellido);
			textviewNombre.setText(nombre);
			textviewOrigen.setText(origen);
			textviewDestino.setText(destino);
			textviewKm.setText(km);
			textviewCosto.setText(costo);
			textviewFecha.setText(fecha);
			mapa = ((MapFragment)
					getFragmentManager().findFragmentById(R.id.fragmentMapaD)).getMap();
						
						// comprobacion
						if(mapa==null){
							Toast.makeText(this, "no se pudo crear mapa",
							Toast.LENGTH_LONG).show();
						}else{
							// configuraciones iniciales del mapa
							mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
							mapa.getUiSettings().setZoomControlsEnabled(true);
							mapa.getUiSettings().setCompassEnabled(true);
							Double latitud_i = Double.parseDouble(latitud);
							Double longitud_i = Double.parseDouble(longitud); 
							agregarMarca(latitud_i,longitud_i,
									"Origen", "Direccion de Origen");
							agregarMarca(Double.parseDouble(lat_dest),Double.parseDouble(long_dest),
									"Destino", "Direccion de Destino");
								
							
						}
		}
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_carrera, menu);
		return true;
	}	

	public void agregarMarca(double latitud, double longitud,
			String titulo, String mensaje){
		MarkerOptions marca = new MarkerOptions();
		LatLng ubicacion = new LatLng(latitud, longitud);
		marca.position(ubicacion);
		marca.title(titulo);
		marca.icon(BitmapDescriptorFactory.fromResource(
			R.drawable.ic_marca));
		
		marca.snippet(mensaje);
		// agregar la marca al mapa
		mapa.addMarker(marca);
		// animar camara hacia marca del mapa
		mapa.animateCamera(
				CameraUpdateFactory.newLatLng(ubicacion));
		
	}@Override
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
