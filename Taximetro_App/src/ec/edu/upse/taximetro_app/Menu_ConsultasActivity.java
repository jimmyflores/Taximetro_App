package ec.edu.upse.taximetro_app;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.modelo.Usuario;
import ec.edu.upse.taximetro_app.servicio.ConexionWebService;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Menu_ConsultasActivity extends Activity {

	Integer id_usuario;
	String nombre_usuario;
	String online;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_menu__consultas);
		try {
			Intent intent = this.getIntent();
			
			if(!ConexionWebService.VerificaConexion(this))
			{
				id_usuario = Integer.parseInt(intent.getStringExtra("id_usuario"));
				nombre_usuario = intent.getStringExtra("usuario");
				online = intent.getStringExtra("online");
				
				DBTaximetro dbTaxi = new DBTaximetro();
				Usuario user = dbTaxi.ListaUsuario(this, nombre_usuario);
				
				if (user == null || online.equalsIgnoreCase("si"))
				{
					crearMensaje(2, "El Usuario no se encuentra registrado localmente debe conectarse a internet, ¿Desea conectarse?");
					Intent intents = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
					startActivity(intents);
				}
			}
			else
			{
				Intent intente = this.getIntent();
				id_usuario = Integer.parseInt(intente.getStringExtra("id_usuario"));
				nombre_usuario = intente.getStringExtra("usuario");
				nombre_usuario = intente.getStringExtra("online");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public void crearMensaje(int numBotones, String Mensaje){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(Mensaje);
		dialog.setCancelable(false);
		if(numBotones == 1){
			dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				  @Override
				  public void onClick(DialogInterface dialog, int which) {
				    
				  }
				});	
			
		}
		if(numBotones == 2){
			dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
				  @Override
				  public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						startActivity(intent);
				  }
				});	
		dialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
				  @Override
				  public void onClick(DialogInterface dialog, int which) {
				    
				  }
				});	
		}
		dialog.show();
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
		intent.putExtra("id_usuario", ""+id_usuario);
		intent.putExtra("nombre_usuario", nombre_usuario);
		startActivity(intent);
	}
	
	public void onCarreras(View boton){
		Intent intent =new Intent(this,TablaCarrerasActivity.class);
		intent.putExtra("id_usuario", ""+id_usuario);
		intent.putExtra("usuario", nombre_usuario);
		startActivity(intent);
		
	}
	
}
