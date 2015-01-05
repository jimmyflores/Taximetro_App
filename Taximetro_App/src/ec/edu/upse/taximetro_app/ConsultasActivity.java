package ec.edu.upse.taximetro_app;

import java.util.ArrayList;

import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.utiles.CustomListViewAdapter;
import ec.edu.upse.taximetro_app.utiles.CustomListViewAdapterConsulta;
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
	TextView num_carrera, total;
    ListView lista;
    DatePicker desde, hasta;
    String accion;
    Integer id_usuario;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consultas);
		Intent intent = getIntent();
		id_usuario = Integer.parseInt(intent.getStringExtra("id_usuario"));
		Inicializar();
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
		num_carrera = (TextView) findViewById(R.id.textViewNumCarreras);
		total = (TextView) findViewById(R.id.textViewVTCarreras);
		
 	}

	public void onBuscar(View Button){
		lista = (ListView)findViewById(R.id.listViewConsulta);
		String fecha_desde = desde.getDayOfMonth()+"/"+desde.getMonth()+"/"+desde.getYear();
		String fecha_hasta = hasta.getDayOfMonth()+"/"+hasta.getMonth()+"/"+hasta.getYear();
				
		DBTaximetro dbTaximetro = new DBTaximetro();
		ArrayList<ItemConsulta> listarCarrera = dbTaximetro.BuscarPorFecha(this,id_usuario, fecha_desde, fecha_hasta);
		CustomListViewAdapterConsulta customAdapter = new CustomListViewAdapterConsulta(this, R.layout.activity_item__result, listarCarrera);
		lista.setAdapter(customAdapter); 
		num_carrera.setText(" "+dbTaximetro.numero_de_carreras(this, id_usuario, fecha_desde, fecha_hasta));
		total.setText(" $ "+dbTaximetro.valor_total(this, id_usuario, fecha_desde, fecha_hasta));
	}
	
	public void onRegresar(View boton){
		Intent intent =new Intent(this,FuncionesActivity.class);
		startActivity(intent);
	}
	
}
