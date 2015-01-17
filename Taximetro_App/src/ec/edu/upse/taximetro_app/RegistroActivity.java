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
		dbTaxi.nuevoUsuario(this,Nombres, Apellidos, email, Usuario, Clave, "NE"); //, nombres, apellidos, email, usuario, clave), nombres, apellidos, email, usuario, clave)//nuevoCliente(this, rucCedula, nombre, apellidos,direccion , 0.0, 0.0, rutafoto);
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
