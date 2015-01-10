package ec.edu.upse.taximetro_app;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends Activity {

	private EditText et_nombres, et_apellidos, et_e_mail, et_usuario, et_contrasenia;
	private Button btn_Registro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		Inicializar();
		
		btn_Registro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WSRegistro registro = new WSRegistro();
				registro.execute();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro, menu);
		return true;
	}

	public void Inicializar(){
		et_nombres = (EditText) findViewById(R.id.editTextNombres);
		et_apellidos = (EditText) findViewById(R.id.editTextApellidos);
		et_e_mail = (EditText) findViewById(R.id.editTextE_mail);
		et_usuario = (EditText) findViewById(R.id.editTextUserRegistro);
		et_contrasenia = (EditText) findViewById(R.id.editTextPassRegistro);
	}
	
	
	private class WSRegistro extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			boolean resul = true;
			final String NAMESPACE = "";
			final String URL = "";
			final String METHOD_NAME = "";
			final String SOAP_ACTION = "";
			
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("nombres", et_nombres.getText().toString());
			request.addProperty("apellidos", et_apellidos.getText().toString());
			request.addProperty("e_mail", et_e_mail.getText().toString());
			request.addProperty("usuario", et_usuario.getText().toString());
			request.addProperty("contrasenia", et_contrasenia.getText().toString());
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(URL);
			
			try {
				transporte.call(SOAP_ACTION, envelope);
				SoapPrimitive resultado_xml = (SoapPrimitive)envelope.getResponse();
				String res = resultado_xml.toString();
				if(!res.equals("1"))
					resul = false;
			
			} catch (Exception e) {
				 resul = false;
			}
			return resul;
		}
		
		
		protected void onPostExecute (Boolean result){
			if (result){
				AlertDialog.Builder alerta = new AlertDialog.Builder(null);
				alerta.setTitle("Aviso de la App");
				alerta.setMessage("Usuario Registrado");
				alerta.setIcon(R.drawable.ic_launcher);
				alerta.setPositiveButton("Aceptar", null);
				alerta.show();
			}
			else{
				AlertDialog.Builder alerta = new AlertDialog.Builder(null);
				alerta.setTitle("Aviso de la App");
				alerta.setMessage("Error");
				alerta.setIcon(R.drawable.ic_launcher);
				alerta.setPositiveButton("Aceptar", null);
				alerta.show();
			}	
		}
		
	}
	
	public void Cancelar_Evento(View boton){
		Intent intent =new Intent(this,MainActivity.class);
		startActivity(intent);
	}
	/*
	 * String nombres, String apellidos,
			String email, String usuario, String clave
	 * */
	public void IngresarUsuario(View boton){
		Inicializar();
		String Nombres = et_nombres.getText().toString();
		String Apellidos = et_apellidos.getText().toString();
		String email = et_e_mail.getText().toString();
		String Usuario = et_usuario.getText().toString();
		String Clave = et_contrasenia.getText().toString();
		DBTaximetro dbTaxi = new DBTaximetro();
		
		if (isEmpty()){
			Toast.makeText(this,"Falta(n)de ingresar algun(os) Campo(s)!!", Toast.LENGTH_LONG).show();
    	}else{
		dbTaxi.nuevoUsuario(this,Nombres, Apellidos, email, Usuario, Clave); //, nombres, apellidos, email, usuario, clave), nombres, apellidos, email, usuario, clave)//nuevoCliente(this, rucCedula, nombre, apellidos,direccion , 0.0, 0.0, rutafoto);
		Toast.makeText(this, "Guardado", Toast.LENGTH_LONG).show();
		Limpiar();
		Intent intent =new Intent(this,MainActivity.class);
		startActivity(intent);
    	}
	}
	
	public boolean isEmpty(){
		if(et_nombres.getText().toString().equals("")|| et_apellidos.getText().toString().equals("") || et_e_mail.getText().toString().equals("")|| et_usuario.getText().toString().equals("") || et_contrasenia.getText().toString().equals("") ){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void Limpiar(){
		et_nombres.setText("");
		et_apellidos.setText("");
		et_e_mail.setText("");
		et_usuario.setText("");
		et_contrasenia.setText("");
	} 
}
