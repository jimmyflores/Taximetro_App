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
import android.os.Bundle;
import android.app.Activity;
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
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_carrera);
		
		try {
			Intent intent = this.getIntent();
			car = Integer.parseInt(intent.getStringExtra("carrera"));
					} catch (Exception e) {
			// TODO: handle exception
		}
		
		TextView textviewOrigen = (TextView) findViewById(R.id.textViewO1);
		TextView textviewNombre = (TextView) findViewById(R.id.textViewNombresD);
		TextView textviewApellido = (TextView) findViewById(R.id.textViewApellidosD);
		TextView textviewDestino = (TextView) findViewById(R.id.textViewD2);
		TextView textviewKm = (TextView) findViewById(R.id.textViewKm2);
		TextView textviewCosto = (TextView) findViewById(R.id.textViewCosto2);
		
		
		Intent intent = this.getIntent();
		
		DBTaximetro dbTaximetro = new DBTaximetro();
		ItemCarrera cl= dbTaximetro.Buscar(this, car);
		
		textviewApellido.setText(cl.getApellido());
		textviewNombre.setText(cl.getNombre());
		textviewOrigen.setText(cl.getOrigen());
		textviewDestino.setText(cl.getDestino());
		textviewKm.setText(cl.getKm().toString());
		textviewCosto.setText(cl.getCosto().toString());
		
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
								"Mi marca", "Esta es mi direccion");
					}
		//agregarMarca(cl.getLatitud(), cl.getLongitud(), "ubica", 
			//	 "Ubicacion actual");*/
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
				R.drawable.ic_launcher));
		
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
