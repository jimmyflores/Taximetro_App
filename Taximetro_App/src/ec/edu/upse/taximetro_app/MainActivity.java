package ec.edu.upse.taximetro_app;

import java.util.ArrayList;


import ec.edu.upse.taximetro_app.modelo.DBTaximetro;
import ec.edu.upse.taximetro_app.utiles.ItemDeUsuario;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText editTextUsuario, editTextPassword;
    private Button btn_Acceder;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void Registrarse_Evento(View boton){
		Intent intent = new Intent(this,RegistroActivity.class);
		startActivity(intent);
	} 
    public void Inicializar(){
    	editTextUsuario = (EditText) findViewById(R.id.editTextUsuario);
    	editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		
	}
    public void Acceder_Evento(View boton)
    {
    	Inicializar();
    	String Nombre = editTextUsuario.getText().toString();
    	String Clave = editTextPassword.getText().toString();
    	DBTaximetro dbTaxi = new DBTaximetro();
    	ArrayList<ItemDeUsuario> listarusuario = dbTaxi.Listalogin(this, Nombre, Clave);
    	if (listarusuario.size() == 0)
    	{
    		Toast.makeText(this, "Usuario o Clave incorrecta", Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    		Intent intent =new Intent(this,FuncionesActivity.class);
    		intent.putExtra("id_usuario", ""+listarusuario.get(0).getId_login());
    		Toast.makeText(this, "ID_USUARIO "+listarusuario.get(0).getId_login(), Toast.LENGTH_LONG).show();
    		startActivity(intent);
    		Limpiar();
    		
    	}	
    }
    
    public void Limpiar(){
		editTextUsuario.setText("");
		editTextPassword.setText("");
	} 
    
}
