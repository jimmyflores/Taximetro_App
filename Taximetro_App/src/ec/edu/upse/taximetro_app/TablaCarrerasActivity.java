package ec.edu.upse.taximetro_app;

import java.util.ArrayList;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.utiles.CustomListCarrera;
import ec.edu.upse.taximetro_app.utiles.ItemCarrera;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TablaCarrerasActivity extends Activity {
	Integer id_usuario;
	String nombre_usuario;
	ListView listViewCarrera;
	EditText txt_buscar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabla_carreras);
		try {
			Intent intent = this.getIntent();
			id_usuario = Integer.parseInt(intent.getStringExtra("id_usuario"));
			nombre_usuario = intent.getStringExtra("nombre_usuario");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		listViewCarrera = (ListView) findViewById(R.id.listView1);
		DBTaximetro dbTaximetro = new DBTaximetro();
		ArrayList<ItemCarrera> listarTabla = dbTaximetro.ListaCarreras(this, id_usuario);
		CustomListCarrera customAdapter = new CustomListCarrera(this, R.layout.activity_item_carrera, listarTabla);
		//ESTABLECER ADAPTADOR A listview
		listViewCarrera.setAdapter(customAdapter);
		//CONTROLAR EL EVENTO DE CLICK SOBRE CADA ITEM DE LA LISTA
		listViewCarrera.setOnItemClickListener(new ItemClickListener());
	}
	
	class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
			// TODO Auto-generated method stub
			ItemCarrera itemDeLista = (ItemCarrera)listViewCarrera.getItemAtPosition(position);
	//		Toast.makeText(parent.getContext(), itemDeLista.getTitle(), Toast.LENGTH_LONG).show(); 
					
							Intent intent;
							//intent = new Intent(parent.getContext(), DetalleClienteActivity.class);
							//startActivity(intent);
						
						
				//intent = new Intent(parent.getContext(), DetalleTarjetaActivity.class);
			//COLOCAR DATOS EN EL INTENT PARA QUE LLEGUEN A LA SIGUIENTE ACTIVIDAD
			}
			
		}
	
	public void onBusqueda(View Button){
		txt_buscar=(EditText)findViewById(R.id.editTextBusca);
		listViewCarrera = (ListView)findViewById(R.id.listView1);
		//SE TRABAJA CON EL MODELO
		DBTaximetro dbTaximetro = new DBTaximetro();
		ArrayList<ItemCarrera> listarTabla = dbTaximetro.BuscarDestino(this, txt_buscar.getText().toString());
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

}
