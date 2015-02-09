package ec.edu.upse.taximetro_app;

import com.google.android.gms.internal.bd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
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
	Double latitud=0.0,longitud=0.0,lat_dest=0.0,long_dest=0.0;
	
	//textview
	TextView textviewOrigen, textviewNombre, textviewApellido;
	TextView textviewDestino, textviewKm , textviewCosto ,textviewFecha;
	
	public void inicializar(){
		textviewOrigen = (TextView) findViewById(R.id.textView01);
		textviewNombre = (TextView) findViewById(R.id.textViewNombresD);
		textviewApellido = (TextView) findViewById(R.id.textViewApellidosD);
		textviewDestino = (TextView) findViewById(R.id.textViewD2);
		textviewKm = (TextView) findViewById(R.id.textViewKm2);
		textviewCosto = (TextView) findViewById(R.id.textViewCosto2);
		textviewFecha = (TextView) findViewById(R.id.textViewFe);
		mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentMapaD)).getMap();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_carrera);
		inicializar();
		if(mapa==null){
			Toast.makeText(this, "no se pudo crear mapa",
			Toast.LENGTH_LONG).show();
		}else{
			try 
			{
				Intent intent = this.getIntent();
				car = Integer.parseInt(intent.getStringExtra("carrera"));
				nombre=intent.getStringExtra("nombre");
				apellido=intent.getStringExtra("apellido");
				origen=intent.getStringExtra("origen");
				destino=intent.getStringExtra("destino");
				km=intent.getStringExtra("km");
				costo=intent.getStringExtra("costo");
				fecha=intent.getStringExtra("fecha");
				latitud=Double.parseDouble(intent.getStringExtra("latitud"));
				longitud=Double.parseDouble(intent.getStringExtra("longitud"));
				lat_dest=Double.parseDouble(intent.getStringExtra("latdest"));
				long_dest=Double.parseDouble(intent.getStringExtra("longdest"));
			} 
			catch (Exception e) {
				// TODO: handle exception
			}
		
		Intent intent = this.getIntent();
		
			DBTaximetro dbTaximetro = new DBTaximetro();
			ItemCarrera cl= dbTaximetro.Buscar(this, car);
			if(cl!=null){
			textviewApellido.setText(cl.getApellido());
			textviewNombre.setText(cl.getNombre());
			textviewOrigen.setText(cl.getOrigen());
			textviewDestino.setText(cl.getDestino());
			textviewKm.setText(cl.getKm().toString());
			textviewCosto.setText(cl.getCosto().toString());
			textviewFecha.setText(cl.getFecha());
		
					// comprobacion
					if(mapa != null){
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
	
		
		}else{
				textviewApellido.setText(apellido);
				textviewNombre.setText(nombre);
				textviewOrigen.setText(origen);
				textviewDestino.setText(destino);
				textviewKm.setText(km);
				textviewCosto.setText(costo);
				textviewFecha.setText(fecha);
				// comprobacion
						if(mapa==null){
							Toast.makeText(this, "no se pudo crear mapa",
							Toast.LENGTH_LONG).show();
						}else{
							// configuraciones iniciales del mapa
							mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
							mapa.getUiSettings().setZoomControlsEnabled(true);
							mapa.getUiSettings().setCompassEnabled(true);
							
							agregarMarca(latitud,longitud,
									"Origen", "Direccion de Origen");
							agregarMarca(lat_dest,long_dest,
									"Destino", "Direccion de Destino");			
			}
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
