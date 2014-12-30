package ec.edu.upse.taximetro_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class FuncionesActivity extends Activity {

	String id_usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_funciones);
		Intent intent = getIntent();
		id_usuario = intent.getStringExtra("id_usuario");
		Toast.makeText(this, "id_usuario_consultas "+id_usuario, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.funciones, menu);
		return true;
	}
	
	
	public void onInicio(View boton){
		Intent intent =new Intent(this,MapaActivity.class);
		startActivity(intent);
	}
	
	public void onConsultas(View boton){
		Intent intent =new Intent(this,Menu_ConsultasActivity.class);
		intent.putExtra("id_usuario_funciones", id_usuario);
		Toast.makeText(this, "id_usuario_consultasON "+id_usuario, Toast.LENGTH_LONG).show();
		startActivity(intent);
	}
	
	

}
