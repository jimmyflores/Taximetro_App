package ec.edu.upse.taximetro_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class FuncionesActivity extends Activity {

	Integer id;
	String nombre_usuario;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_funciones);
		Intent intentActual = this.getIntent();
		try {
		id = Integer.parseInt(intentActual.getStringExtra("id_usuario"));
		nombre_usuario = intentActual.getStringExtra("usuario");
		//Toast.makeText(this, "usuario: "+nombre_usuario+" id: "+id, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
		e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.funciones, menu);
		return true;
	}
	
	
	public void onInicio(View boton){
		Intent intent =new Intent(this,MapaActivity.class);
		intent.putExtra("id_usuario", ""+id);
		intent.putExtra("usuario", nombre_usuario);
		startActivity(intent);
	}
	
	public void onConsultas(View boton){
		Intent intent =new Intent(this,Menu_ConsultasActivity.class);
		intent.putExtra("id_usuario", ""+id);
		startActivity(intent);
	}
}