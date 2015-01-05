package ec.edu.upse.taximetro_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Menu_ConsultasActivity extends Activity {

	Integer id_usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu__consultas);
		Intent intent = getIntent();
		id_usuario = Integer.parseInt(intent.getStringExtra("id_usuario"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu__consultas, menu);
		return true;
	}

	public void onTarifas(View boton){
		Intent intent =new Intent(this,TablaANTActivity.class);
		startActivity(intent);
	}
	
	public void onFechas(View boton){
		Intent intent =new Intent(this,ConsultasActivity.class);
		intent.putExtra("id_usuario", id_usuario);
		startActivity(intent);
	}
	public void onCarreras(View boton){
		//Intent intent =new Intent(this,Consulta_carrera.class);
		//startActivity(intent);
	}
	
}
