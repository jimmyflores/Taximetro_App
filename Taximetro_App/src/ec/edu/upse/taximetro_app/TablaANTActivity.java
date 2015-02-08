package ec.edu.upse.taximetro_app;

import java.util.ArrayList;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.utiles.CustomListViewAdapter;
import ec.edu.upse.taximetro_app.utiles.ItemConsulta;
import ec.edu.upse.taximetro_app.utiles.ItemTablita;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TablaANTActivity extends Activity {
	ListView listViewTabla;
	EditText txt_buscar;
	String accion;
	Intent intent;
	String opcion;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabla_ant);
		
		listViewTabla = (ListView) findViewById(R.id.listViewTabla);
		DBTaximetro dbTaximetro = new DBTaximetro();
		ArrayList<ItemTablita> listarTabla = dbTaximetro.BuscarTabla(this);
		CustomListViewAdapter customAdapter = new CustomListViewAdapter(this, R.layout.activity_item_tabla, listarTabla);
		//ESTABLECER ADAPTADOR A listview
		listViewTabla.setAdapter(customAdapter);
		//CONTROLAR EL EVENTO DE CLICK SOBRE CADA ITEM DE LA LISTA
		listViewTabla.setOnItemClickListener(new ItemClickListener());
	}

	class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
			// TODO Auto-generated method stub
		
			}
			
		}
	
	public void onBusqueda(View Button){
		txt_buscar=(EditText)findViewById(R.id.editTeextBuscarP);
		listViewTabla = (ListView)findViewById(R.id.listViewTabla);
		//SE TRABAJA CON EL MODELO
		DBTaximetro dbTaximetro = new DBTaximetro();
		ArrayList<ItemTablita> listarTabla = dbTaximetro.BuscarPorDestino(this, txt_buscar.getText().toString());
		//TRABAJAR CON LA INTERFAZ
		//SE CREA UN CustomListViewAdapter PARA DIBUJAR CADA ELEMENTO DEL listview
		CustomListViewAdapter customAdapter = new CustomListViewAdapter(this, R.layout.activity_item_tabla, listarTabla);
		
		listViewTabla.setAdapter(customAdapter);
		//CONTROLAR EL EVENTO DE CLICK SOBRE CADA ITEM DE LA LISTA
		listViewTabla.setOnItemClickListener(new ItemClickListener());
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.tabla_ant, menu);
		return true;
	}
}
