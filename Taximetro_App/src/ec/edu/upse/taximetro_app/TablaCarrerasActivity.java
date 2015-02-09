package ec.edu.upse.taximetro_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ec.edu.upse.taximetro_app.TablaANTActivity.ItemClickListener;
import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.modelo.Usuario;
import ec.edu.upse.taximetro_app.servicio.ConexionWebService;
import ec.edu.upse.taximetro_app.utiles.CustomListCarrera;
import ec.edu.upse.taximetro_app.utiles.CustomListViewAdapter;
import ec.edu.upse.taximetro_app.utiles.ItemCarrera;
import ec.edu.upse.taximetro_app.utiles.ItemTablita;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.google.gson.reflect.TypeToken;

public class TablaCarrerasActivity extends Activity {
	HttpTransportSE transporte;
	SoapObject request;
	SoapSerializationEnvelope sobre;
	SoapPrimitive resultado;
	
	//atributos
	private String NAMESPACE= ConexionWebService.getNamespace();
	private String URL = ConexionWebService.getUrlConsultar();
	private String SOAP_ACTION;
	private String METODO;
	
	Integer id_usuario;
	String nombre_usuario;
	ListView listViewCarrera;
	EditText txt_buscar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabla_carreras);
		try {
			Intent intentActual = this.getIntent();
			id_usuario = Integer.parseInt(intentActual.getStringExtra("id_usuario"));
			nombre_usuario = intentActual.getStringExtra("nombre_usuario");
		
			if(!verificarConexion(this))
			{
				DBTaximetro dbTaxi = new DBTaximetro();
				Usuario user = dbTaxi.ListaUsuario(this, nombre_usuario);
				if (user == null)
				{
					Toast.makeText(this, "El Usuario no se encuentra registrado localmente active el servicio de internet", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
					startActivity(intent);
				}
				listViewCarrera = (ListView) findViewById(R.id.listView1);
				DBTaximetro dbTaximetro = new DBTaximetro();
				ArrayList<ItemCarrera> listarTabla = dbTaximetro.ListaCarreras(this, id_usuario);
				CustomListCarrera customAdapter = new CustomListCarrera(this, R.layout.activity_item_tabla, listarTabla);
				//ESTABLECER ADAPTADOR A listview
				listViewCarrera.setAdapter(customAdapter);
				//CONTROLAR EL EVENTO DE CLICK SOBRE CADA ITEM DE LA LISTA
				listViewCarrera.setOnItemClickListener(new ItemClickListener());
			}
			else
			{
				mostrar();
			}
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		
	}
	
	class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
			// TODO Auto-generated method stub
			ItemCarrera itemDeLista = (ItemCarrera)listViewCarrera.getItemAtPosition(position);
	//		Toast.makeText(parent.getContext(), itemDeLista.getTitle(), Toast.LENGTH_LONG).show();
							Intent intent;
							intent = new Intent(parent.getContext(), DetalleCarreraActivity.class);
							intent.putExtra("carrera", ""+itemDeLista.getIdCarrera());
							intent.putExtra("nombre", ""+itemDeLista.getNombre());
							intent.putExtra("apellido", ""+itemDeLista.getApellido());
							intent.putExtra("origen", ""+itemDeLista.getOrigen());
							intent.putExtra("destino", ""+itemDeLista.getDestino());
							intent.putExtra("km", ""+itemDeLista.getKm());
							intent.putExtra("costo", ""+itemDeLista.getCosto());
							intent.putExtra("fecha", ""+itemDeLista.getFecha());
							intent.putExtra("latitud", ""+itemDeLista.getLatitud());
							intent.putExtra("longitud", ""+itemDeLista.getLongitud());
							intent.putExtra("latdest", ""+itemDeLista.getLatitud_destino());
							intent.putExtra("longdest", ""+itemDeLista.getLongitud_destino());
							startActivity(intent);
			}
			
		}
	
	

	public void onBusqueda(View Button){
		txt_buscar=(EditText)findViewById(R.id.editTextBusca);
		
		listViewCarrera = (ListView)findViewById(R.id.listView1);
		//SE TRABAJA CON EL MODELO
		DBTaximetro dbTaximetro = new DBTaximetro();
		ArrayList<ItemCarrera> listarTabla = dbTaximetro.BuscarDestino(this, txt_buscar.getText().toString(), id_usuario);
		//TRABAJAR CON LA INTERFAZ
		//SE CREA UN CustomListViewAdapter PARA DIBUJAR CADA ELEMENTO DEL listview
		CustomListCarrera customAdapter = new CustomListCarrera(this, R.layout.activity_item_carrera, listarTabla);
		
		listViewCarrera.setAdapter(customAdapter);
		//CONTROLAR EL EVENTO DE CLICK SOBRE CADA ITEM DE LA LISTA
		listViewCarrera.setOnItemClickListener(new ItemClickListener());
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabla_carreras, menu);
		return true;
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
private void mostrar()
{
	System.out.println(" --- estoy en el metodo mostrar");
	METODO = "servicio_consultar_usuario";
	SOAP_ACTION = NAMESPACE + METODO;
	SoapObject request=null;
	SoapSerializationEnvelope envelope=null;
	SoapPrimitive resultrequestSoap = null;	
	request = new SoapObject(NAMESPACE,METODO);
	
	request.addProperty("usuario", ""+nombre_usuario);
	envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	envelope.dotNet=true;
	envelope.setOutputSoapObject(request);
	HttpTransportSE transporte = new HttpTransportSE(URL);
	try {
		transporte.call(SOAP_ACTION, envelope);
		resultrequestSoap=(SoapPrimitive)envelope.getResponse();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (XmlPullParserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String strJSON = resultrequestSoap.toString();
	System.out.println("--- " + strJSON);
	crearLista(strJSON);
}
	public void crearLista(String strJSON)
	{
		
		ArrayList<ItemCarrera> listarcarreraws = new ArrayList<ItemCarrera>();
		listViewCarrera = (ListView)findViewById(R.id.listView1);
		txt_buscar=(EditText)findViewById(R.id.editTextBusca);
		try {
			JSONArray jsonn =new JSONArray(strJSON);;
			for(int i=0; i< jsonn.length(); i++){
			int usid = jsonn.getJSONObject(i).getInt("idCarrera");
			int id_tarifa = jsonn.getJSONObject(i).getJSONObject("tarifa").getInt("idTarifa");
			String tipo_tarifa = jsonn.getJSONObject(i).getJSONObject("tarifa").getString("tipoTarifa");
			int valor_tarifa = jsonn.getJSONObject(i).getJSONObject("tarifa").getInt("valor");
			int id_usuario = jsonn.getJSONObject(i).getJSONObject("usuario").getInt("idUsuario");
			String nombre = jsonn.getJSONObject(i).getJSONObject("usuario").getString("nombre");
			String apellido = jsonn.getJSONObject(i).getJSONObject("usuario").getString("apellido");
			String origen = jsonn.getJSONObject(i).getString("origen");
			String destino = jsonn.getJSONObject(i).getString("destino");
			String tiempo = jsonn.getJSONObject(i).getString("tiempo");
			Double kmrecorridos = jsonn.getJSONObject(i).getDouble("kmRecorridos");
			Double precio = jsonn.getJSONObject(i).getDouble("precio");
			String fecha = jsonn.getJSONObject(i).getString("fecha");
			Double latorigen = jsonn.getJSONObject(i).getDouble("latitudOrigen");
			Double longorigen = jsonn.getJSONObject(i).getDouble("longitudOrigen");
			Double latdestino = jsonn.getJSONObject(i).getDouble("latitudDestino");
			Double longdestino = jsonn.getJSONObject(i).getDouble("longitudDestino");
			
			ItemCarrera item = new ItemCarrera();
			item.setIdCarrera(usid);
			item.setNombre(nombre);
			item.setApellido(apellido);
			item.setOrigen(origen);
			item.setDestino(destino);
			item.setCosto(precio);
			item.setKm(kmrecorridos);
			item.setFecha(fecha);
			item.setLatitud(latorigen);
			item.setLongitud(longorigen);
			item.setLatitud_destino(latdestino);
			item.setLatitud_destino(longdestino);
			listarcarreraws.add(item);
			}
			System.out.println(" -- estoy en crear lista");
			CustomListCarrera customAdapter = new CustomListCarrera(this, R.layout.activity_item_carrera, listarcarreraws);
			listViewCarrera.setAdapter(customAdapter);
			//CONTROLAR EL EVENTO DE CLICK SOBRE CADA ITEM DE LA LISTA
			listViewCarrera.setOnItemClickListener(new ItemClickListener());	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	 
}
