package ec.edu.upse.taximetro_app;

import java.util.ArrayList;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.utiles.CustomListViewAdapter;
import ec.edu.upse.taximetro_app.utiles.ItemConsulta;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ConsultasActivity extends Activity {
	EditText carrera, total;
    ListView lista;
    DatePicker desde, hasta;
    String accion;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultas);
		Inicializar();
		
		lista = (ListView)findViewById(R.id.listViewConsulta);
		DBTaximetro dbTaximetro = new DBTaximetro();
		ArrayList<ItemConsulta> listarCarrera = dbTaximetro.listarConsulta(this);
		//TRABAJAR CON LA INTERFAZ
		//SE CREA UN CustomListViewAdapter PARA DIBUJAR CADA ELEMENTO DEL listview
	     //CustomListViewAdapter customAdapter = new CustomListViewAdapter(this, R.layout.activity_item__result, listarCarrera);
		//ESTABLECER ADAPTADOR A listview
		//lista.setAdapter(customAdapter);
		//CONTROLAR EL EVENTO DE CLICK SOBRE CADA ITEM DE LA LISTA
		lista.setOnItemClickListener(new ItemClickListener());
	}
     
	class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
			// TODO Auto-generated method stub
			ItemConsulta itemDeLista = (ItemConsulta)lista.getItemAtPosition(position);
	//		Toast.makeText(parent.getContext(), itemDeLista.getTitle(), Toast.LENGTH_LONG).show(); 
					
							Intent intent;
							//intent = new Intent(parent.getContext(), DetalleClienteActivity.class);
							//startActivity(intent);
						
						
				//intent = new Intent(parent.getContext(), DetalleTarjetaActivity.class);
			//COLOCAR DATOS EN EL INTENT PARA QUE LLEGUEN A LA SIGUIENTE ACTIVIDAD
			}
			
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consultas, menu);
		return true;
	}
	
	public void Inicializar(){
		desde = (DatePicker) findViewById(R.id.datePickerDesde);
		hasta = (DatePicker) findViewById(R.id.datePickerHast);
		lista = (ListView) findViewById(R.id.listViewConsulta);
		//carrera = (EditText) findViewById(R.id.editTextCcarreras);
		//total = (EditText) findViewById(R.id.editTextVtotal);
		
 	}

	public void onBuscar(View Button){
		
		//lista = (ListView)findViewById(R.id.listViewConsulta);
		//SE TRABAJA CON EL MODELO
		DBTaximetro dbTaximetro = new DBTaximetro();
		
	//	ArrayList<ItemConsulta> listarCarrera = dbTaximetro.BuscarPorFecha(this,txt_busqueda.getText().toString());
		//TRABAJAR CON LA INTERFAZ
		//SE CREA UN CustomListViewAdapter PARA DIBUJAR CADA ELEMENTO DEL listview
	//	CustomListViewAdapter customAdapter = new CustomListViewAdapter(this, R.layout.activity_item__result, listarCarrera);
		//ESTABLECER ADAPTADOR A listview
	//	lista.setAdapter(customAdapter);
		// 'CONTROLAR EL EVENTO DE CLICK SOBRE CADA ITEM DE LA LISTA 
		lista.setOnItemClickListener(new ItemClickListener());
	}
	
	public void onRegresar(View boton){
		Intent intent =new Intent(this,FuncionesActivity.class);
		startActivity(intent);
	}
	
}
