package ec.edu.upse.taximetro_app.modelo;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBTaximetro_service {
	private static final String TABLA_NAME_Persona = "personas";
	private static final String TABLA_NAME_User = "usuarios";
	private static final String TABLA_NAME_carrera = "carrera";
	private static final String DB_NAME = "DBTaximetro";
	
	//Usuarios no enviados al WebService al momento de registrarse
	//por que el dispositivo no tenia acceso a internet
	public ArrayList<Usuario> UsuariosNoEnviados(Context contexto){
		ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
		// COnexion a la BD
		SqlTaximetro tarjetaDB =new SqlTaximetro(contexto,DB_NAME,null,1);
		// Referencia a la BD
		SQLiteDatabase  db = tarjetaDB.getReadableDatabase();
		
		String[] parametrosDeBusqueda = new String[]{};
		String sql = "SELECT user.id_u, user.usuario, person.id_p, person.nombres , person.apellidos, person.email, user.clave " +
				"FROM usuarios as user,personas as person " +
				"WHERE user.estado_envio='NE' and user.id_persona = person.id_p and person.estado_envio='NE'";
				      Cursor cursor = db.rawQuery(sql, parametrosDeBusqueda);	
			
		    if(cursor.moveToFirst()){
			// Recorrer los resultados
			do{
				Usuario user = new Usuario();
								user.setId(cursor.getInt(0));
								user.setNombre_usuario(cursor.getString(1));
								user.setEstado("A");
								
								Persona per = new Persona();
								per.setId(cursor.getInt(2));
								per.setNombres(cursor.getString(3));
								per.setApellidos(cursor.getString(4));
								per.setEmail(cursor.getString(5));
								
								user.setClave(cursor.getString(6));
								user.setPersona(per);
								System.out.println(" -- >");
								listaUsuarios.add(user);
			}while(cursor.moveToNext());
		
		}
		db.close();
		return listaUsuarios;
	}
	
	public void ActualizarEnvios(Context contexto, Usuario listaUsuarios){

		//tabla personas
		SqlTaximetro personas= new SqlTaximetro(contexto, DB_NAME, null, 1);
		SQLiteDatabase db = personas.getWritableDatabase();
		
			if (db!=null){
				db.execSQL("UPDATE personas SET estado_envio = "+listaUsuarios.getPersona().getId()+"");
			}
		db.close();
		
		//tabla usuarios
		SqlTaximetro usuarios= new SqlTaximetro(contexto, DB_NAME, null, 1);
		SQLiteDatabase db1 = usuarios.getWritableDatabase();
			if (db1!=null){
				db1.execSQL("UPDATE usuarios SET estado_envio = "+listaUsuarios.getId()+"");
			}
		db1.close();
	}
	
}
