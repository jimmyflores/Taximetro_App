package ec.edu.upse.taximetro_app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.modelo.Usuario;
import ec.edu.upse.taximetro_app.servicio.ConexionWebService;
import ec.edu.upse.taximetro_app.utiles.CustomListViewAdapter;
import ec.edu.upse.taximetro_app.utiles.CustomListViewAdapterConsulta;
import ec.edu.upse.taximetro_app.utiles.ItemConsulta;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ConsultasActivity extends Activity {
	TextView num_carrera, total;
    ListView lista;
    String accion;
    Integer id_usuario;
    String nombre_usuario;
    Button buttonDesde, buttonHasta;
    
    HttpTransportSE transporte;
	SoapObject request;
	SoapSerializationEnvelope sobre;
	SoapPrimitive resultado;
	
	int numeroCarrerasWS=0; 
	Double precioTotalCWS=0.0;
	
	//atributos
	private String NAMESPACE= ConexionWebService.getNamespace();
	private String URL = ConexionWebService.getUrlConsultar();
	private String SOAP_ACTION;
	private String METODO;
	
	private int mYear_d, mMonth_d, mDay_d, mYear_h, mMonth_h, mDay_h; 
	private String dia_d="", mes_d="", dia_h="",mes_h="";
  	static final int DATE_DIALOG_ID_DESDE = 0;
  	static final int DATE_DIALOG_ID_HASTA = 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_consultas);
		Intent intentActual = this.getIntent();
		final Calendar c_desde = Calendar.getInstance();
		mYear_d = c_desde.get(Calendar.YEAR);
		mMonth_d = c_desde.get(Calendar.MONTH);
		mDay_d = c_desde.get(Calendar.DAY_OF_MONTH);
		final Calendar c_hasta = Calendar.getInstance();
		mYear_h = c_hasta.get(Calendar.YEAR);
		mMonth_h = c_hasta.get(Calendar.MONTH);
		mDay_h = c_hasta.get(Calendar.DAY_OF_MONTH);
		
		try {
			if(!verificarConexion(this))
			{
				id_usuario = Integer.parseInt(intentActual.getStringExtra("id_usuario"));
				nombre_usuario = intentActual.getStringExtra("nombre_usuario");
				DBTaximetro dbTaxi = new DBTaximetro();
				Usuario user = dbTaxi.ListaUsuario(this, nombre_usuario);
				if (user == null)
				{
					Toast.makeText(this, "El Usuario no se encuentra registrado localmente active el servicio de internet", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
					startActivity(intent);
				}
			}
			else
			{
				id_usuario = Integer.parseInt(intentActual.getStringExtra("id_usuario"));
				nombre_usuario = intentActual.getStringExtra("nombre_usuario");
				
			}
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		Inicializar();
		updateDisplayDesde();
		updateDisplayHasta();
	}
     
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.consultas, menu);
		return true;
	}
	
	public void Inicializar(){
		lista = (ListView) findViewById(R.id.listViewConsulta);
		num_carrera = (TextView) findViewById(R.id.textViewNumCarreras);
		total = (TextView) findViewById(R.id.textViewVTCarreras);
		buttonDesde = (Button) findViewById(R.id.buttonDesde);
		buttonHasta = (Button) findViewById(R.id.buttonHasta);
		
 	}
	
	public void onFechaDesde(View v){
		LayoutInflater inf = this.getLayoutInflater();
		View vista =  inf.inflate(R.layout.activity_custom_dialog, null);
		final DatePicker fecha_inicio = (DatePicker) vista.findViewById(R.id.datePicker1);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		//alert.setView(vista);
		alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				buttonDesde.setText(fecha(fecha_inicio.getDayOfMonth(),fecha_inicio.getMonth(),fecha_inicio.getYear()));	
			}
		});
		alert.show();
	}
	
	public void onFechaHasta(View v){
		LayoutInflater inf = this.getLayoutInflater();
		View vista =  inf.inflate(R.layout.activity_custom_dialog, null);
		
		final DatePicker fecha_inicio = (DatePicker) vista.findViewById(R.id.datePicker1);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setView(vista);
		alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
		buttonHasta.setText(fecha(fecha_inicio.getDayOfMonth(),fecha_inicio.getMonth(),fecha_inicio.getYear()));	
			}
		});
		alert.show();
	}
	
	
	public String fecha(Integer p_dia, Integer p_mes, Integer p_año){
		String fecha = "";
		String dia="", mes="";
		if(p_mes+1 >=1 && p_mes+1 <=9){
			mes = "0"+(p_mes +1);
		}else{
			mes = ""+(p_mes +1);
		}
		
		if(p_dia>=1 && p_dia <=9){
			dia = "0"+(p_dia);
		}else{
			dia = ""+(p_dia);
		}
		fecha= dia+"/"+mes+"/"+p_año;

		return fecha;
	}
	
	
	
	public void onBuscar(View Button){
		lista = (ListView)findViewById(R.id.listViewConsulta);
		if(verificarConexion(this)){
			String fecha_desde = buttonDesde.getText().toString();
			String fecha_hasta = buttonHasta.getText().toString();
			System.out.println(""+fecha_desde+" "+ fecha_hasta+" "+nombre_usuario);
			ArrayList<ItemConsulta> listarCarrera = consulta_fechas(fecha_desde, fecha_hasta, nombre_usuario);
			CustomListViewAdapterConsulta customAdapter = new CustomListViewAdapterConsulta(this, R.layout.activity_item__result, listarCarrera);
			lista.setAdapter(customAdapter); 
			
			num_carrera.setText(" "+numeroCarrerasWS);
			total.setText(" $ "+precioTotalCWS);
		}else{
			String fecha_desde = buttonDesde.getText().toString();
			String fecha_hasta = buttonHasta.getText().toString();
			DBTaximetro dbTaximetro = new DBTaximetro();
			
			ArrayList<ItemConsulta> listarCarrera = dbTaximetro.BuscarPorFecha(this,id_usuario, fecha_desde, fecha_hasta);
			CustomListViewAdapterConsulta customAdapter = new CustomListViewAdapterConsulta(this, R.layout.activity_item__result, listarCarrera);
			lista.setAdapter(customAdapter); 
			
			num_carrera.setText(" "+dbTaximetro.numero_de_carreras(this, id_usuario, fecha_desde, fecha_hasta));
			total.setText(" $ "+dbTaximetro.valor_total(this, id_usuario, fecha_desde, fecha_hasta));
		}
	}
	
	public void onRegresar(View boton){
		this.finish();
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

	private ArrayList<ItemConsulta> consulta_fechas(String desde, String hasta, String usuario)
	{
	METODO = "servicio_consultar_usuario_fechas";
	SOAP_ACTION = NAMESPACE + METODO;
	SoapObject request = null;
	SoapSerializationEnvelope envelope=null;
	SoapPrimitive resultrequestSoap = null;	
	request = new SoapObject(NAMESPACE,METODO);
	request.addProperty("f_inicio", ""+desde);
	request.addProperty("f_fin", ""+hasta);
	request.addProperty("usuario", ""+usuario);
	envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	envelope.dotNet=true;
	envelope.setOutputSoapObject(request);
	
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	StrictMode.setThreadPolicy(policy);
	
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
	System.out.println("RESULTADO: "+strJSON);
		if(strJSON.equals(null)) {
			Toast.makeText(this, "No ha realizado ninguna carrera", Toast.LENGTH_LONG).show();
		}
		else{
		ArrayList<ItemConsulta> listaConsultaFecha =null;
		try {
			numeroCarrerasWS = numeroCarrerasWS+1;
			listaConsultaFecha=new ArrayList<ItemConsulta>();
			JSONArray jsonn =new JSONArray(strJSON);;
			for(int i=0; i< jsonn.length(); i++){
			int id_carrera = jsonn.getJSONObject(i).getInt("idCarrera");
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
			precioTotalCWS = precioTotalCWS + precio;
			String fecha = jsonn.getJSONObject(i).getString("fecha");
			Double latorigen = jsonn.getJSONObject(i).getDouble("latitudOrigen");
			Double longorigen = jsonn.getJSONObject(i).getDouble("longitudOrigen");
			Double latdestino = jsonn.getJSONObject(i).getDouble("latitudDestino");
			Double longdestino = jsonn.getJSONObject(i).getDouble("longitudDestino");
			
			ItemConsulta item = new ItemConsulta();
			item.setOrigen(origen);
			item.setDestino(destino);
			item.setDistancia(kmrecorridos);
			item.setValor(precio);
			listaConsultaFecha.add(item);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaConsultaFecha;
		}
		return null;
	}

	
	private void updateDisplayDesde(){
		if(mMonth_d+1 >=1 && mMonth_d+1 <=9){
			mes_d = "0"+(mMonth_d+1);
		}else{
			mes_d = ""+(mMonth_d+1);
		}
		
		if(mDay_d>=1 && mDay_d<=9){
			dia_d = "0"+(mDay_d);
		}else{
			dia_d = ""+(mDay_d);
		}
		buttonDesde.setText(new StringBuilder()
					.append(dia_d).append("/")
					.append(mes_d).append("/")
					.append(mYear_d).append(""));
	}
	
	private void updateDisplayHasta(){
		if(mMonth_h+1 >=1 && mMonth_h+1 <=9){
			mes_h = "0"+(mMonth_h+1);
		}else{
			mes_h = ""+(mMonth_h +1);
		}
		
		if(mDay_h>=1 && mDay_h<=9){
			dia_h = "0"+(mDay_h);
		}else{
			dia_h = ""+(mDay_h);
		}
		
		buttonHasta.setText(new StringBuilder()
					.append(dia_h).append("/")
					.append(mes_h).append("/")
					.append(mYear_h).append(""));
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListenerDesde = new DatePickerDialog.OnDateSetListener(){
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear_d = year;
			mMonth_d = monthOfYear;
			mDay_d = dayOfMonth;
					updateDisplayDesde();
		}};
		
		private DatePickerDialog.OnDateSetListener mDateSetListenerHasta = new DatePickerDialog.OnDateSetListener(){
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mYear_h = year;
				mMonth_h = monthOfYear;
				mDay_h = dayOfMonth;
						updateDisplayHasta();
			}};
		
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DATE_DIALOG_ID_DESDE:
			return new DatePickerDialog(this, mDateSetListenerDesde, mYear_d, mMonth_d, mDay_d);
		case DATE_DIALOG_ID_HASTA:
			return new DatePickerDialog(this, mDateSetListenerHasta, mYear_h, mMonth_h, mDay_h);
		}
		
		return null;
	}
	
	
	@SuppressWarnings("deprecation")
	public void CalendarioDesde(View v){
		showDialog(DATE_DIALOG_ID_DESDE);
	}
	
	@SuppressWarnings("deprecation")
	public void CalendarioHasta(View v){
		showDialog(DATE_DIALOG_ID_HASTA);
	}
}
