package ec.edu.upse.taximetro_app.modelo;

import java.util.ArrayList;

import ec.edu.upse.taximetro_app.R;
import ec.edu.upse.taximetro_app.utiles.ItemConsulta;
import ec.edu.upse.taximetro_app.utiles.ItemDeUsuario;
import ec.edu.upse.taximetro_app.utiles.ItemTablita;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBTaximetro {

	private static final String TABLA_NAME = "personas";
	private static final String TABLA_NAME1 = "usuarios";
	private static final String TABLA_NAME2 = "carrera";
	private static final String TABLA_NAME3 = "tarifa";
	private static final String TABLA_NAME4 = "ttarifa";
	
	
	private static final String DB_NAME = "DBTaximetro";
	
	public void nuevoUsuario(Context contexto, String nombres, String apellidos,
			String email, String usuario, String clave){
		Integer id_persona = 0;
		SqlTaximetro clienteDB= new SqlTaximetro(contexto, DB_NAME, null, 1);
		SQLiteDatabase db = clienteDB.getWritableDatabase();
		
		if (db!=null){
			db.execSQL("INSERT INTO "+TABLA_NAME +" VALUES (NULL,'" +nombres+ "', " +
					"'"+apellidos+"', '"+email+"','A')");
		}
		db.close();
		
		SqlTaximetro UserDB= new SqlTaximetro(contexto, DB_NAME, null, 1);
		SQLiteDatabase db2 = UserDB.getReadableDatabase();
		if (db2!=null){
			Cursor cursor = db2.rawQuery("SELECT MAX(id_p) FROM "+ TABLA_NAME +"",null);
			if(cursor.moveToFirst()){
				//recorrer los resultados
				do{
					id_persona = cursor.getInt(0);
				}while(cursor.moveToNext());
			}	
		}
		db2.close();

		SqlTaximetro UserBd =new SqlTaximetro(contexto, DB_NAME, null, 1);
		SQLiteDatabase dbg = UserBd.getWritableDatabase();
		if(dbg!= null){
			dbg.execSQL("INSERT INTO "+TABLA_NAME1+" VALUES" +
					"(NULL,"+id_persona+",'"+usuario+"','"+clave+"','A')");
		}
		dbg.close();
	}
	
	/*String sqlcarrera = "CREATE TABLE carrera (" +
			" id_c INTEGER PRIMARY KEY AUTOINCREMENT," +
			" id_usuario INTEGER NOT NULL," +
			" id_tarifa INTEGER NOT NULL," +
			" km DOUBLE," +
			" valor DOUBLE," +
			" origen TEXT," +
			" coordenada_origen DOUBLE," +
			" destino TEXT," +
			" coordenada_destino DOUBLE," +
			" fecha TEXT, " +
			" duracion_carrera TEXT, " +
			"FOREIGN KEY (id_usuario) REFERENCES usuarios(id_u), "+
			"FOREIGN KEY (id_tarifa) REFERENCES tarifa(id_t))";
	*/
	
	public void nuevaCarrera(Context contexto,Integer id_usuario, Integer id_tarifa, Double km, Double valor,
			String origen, Double latitud_origen,Double longitud_origen, String destino, 
			Double latitud_destino,Double longitud_destino, String fecha, String duracion_carrera ){
		SqlTaximetro carrera= new SqlTaximetro(contexto, DB_NAME, null, 1);
		SQLiteDatabase db = carrera.getWritableDatabase();
		
		if (db!=null){
			db.execSQL("INSERT INTO "+TABLA_NAME2 +" VALUES (NULL,"+id_usuario+ ","+id_tarifa+ ", " +
					""+km+ ","+valor+", '"+origen+ "', "+latitud_origen+","+longitud_origen+", " +
							"'"+destino+ "',"+latitud_destino+","+longitud_destino+",'"+fecha+ "'," +
									"'"+duracion_carrera+"')");
		}
		db.close();
	}
	
	
public ArrayList<ItemDeUsuario> Listalogin(Context contexto, String usuario, String clave){
		
		ArrayList<ItemDeUsuario> listaUsuario=null;
		// COnexion a la BD
		SqlTaximetro tarjetaDB =new SqlTaximetro(contexto,DB_NAME,null,1);
		// Referencia a la BD
		SQLiteDatabase  db = tarjetaDB.getReadableDatabase();
		listaUsuario = new ArrayList<ItemDeUsuario>();
		
		// Consulta sobre la bd
		//Cursor cursor = db.query(TABLA_NAME, new String[]{"tar_titular","tar_nombre","tar_numero"}, 
		//null,null,null,null,"tar_nombre");
		// si es que existe al menos un resultado
		String[] parametrosDeBusqueda = new String[]{usuario,clave};
		String sql = "SELECT * FROM "+TABLA_NAME1+ " WHERE usuario = ? and clave = ?";
		Cursor cursor = db.rawQuery(sql, parametrosDeBusqueda);
			
						if(cursor.moveToFirst()){
			// Recorrer los resultados
			do{
				ItemDeUsuario item=new ItemDeUsuario(R.drawable.ic_consulta,cursor.getString(1),
						cursor.getString(2));
				listaUsuario.add(item);
			}while(cursor.moveToNext());
		}
		
		return listaUsuario;
	}

public Tarifa selectAllTarifa(Context contexto,Integer hora){
	Tarifa Lista= new Tarifa();
	SqlTaximetro tarjetaDB =new SqlTaximetro(contexto,DB_NAME,null,1);
	SQLiteDatabase  db = tarjetaDB.getReadableDatabase();
	String[] parametrosDeBusqueda=null;
	String sql="";
	if(hora > 5 && hora<22){
		sql="SELECT id_t, descripcion, arranque_tarifa, km_recorrido, min_espera, carrera_min, estado FROM "+TABLA_NAME3+" WHERE estado='A' and descripcion='Diurna'";
	}
	if(hora >= 22 || (hora >=0 && hora<=5)){
		sql="SELECT id_t, descripcion, arranque_tarifa, km_recorrido, min_espera, carrera_min, estado FROM "+TABLA_NAME3+" WHERE estado='A' and descripcion='Nocturna'";
	}
	Cursor cursor=db.rawQuery(sql, parametrosDeBusqueda);
	if(cursor.moveToFirst()){
	// Recorrer los resultados
		do{
			//Tarifa tarif =new Tarifa(cursor.getInt(0),cursor.getString(1),
				//cursor.getDouble(2),cursor.getDouble(3),cursor.getDouble(4),cursor.getDouble(5),cursor.getString(6));
			Lista.setId(cursor.getInt(0));
			Lista.setDescripcion(cursor.getString(1));
			Lista.setArranque_tarifa(cursor.getDouble(2));
			Lista.setKm_recorrido(cursor.getDouble(3));
			Lista.setMin_espera(cursor.getDouble(4));
			Lista.setCarrera_min(cursor.getDouble(5));
			Lista.setEstado(cursor.getString(6));
			
		}while(cursor.moveToNext());
	}
	return Lista;
}


public ArrayList<ItemTablita> BuscarTabla(Context contexto){
	
	ArrayList<ItemTablita> listaTabla=null;
	
	SqlTaximetro tarjetaDB =new SqlTaximetro(contexto,DB_NAME,null,1);
	SQLiteDatabase  db = tarjetaDB.getReadableDatabase();
	
	listaTabla = new ArrayList<ItemTablita>();
	
	String[] parametrosDeBusqueda=null;
	String sql="SELECT id_tt, p_partida, p_final, precio FROM ttarifa";
	Cursor cursor=db.rawQuery(sql, parametrosDeBusqueda);

	if(cursor.moveToFirst()){
		
		// Recorrer los resultados
		do{
			ItemTablita item= new ItemTablita(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
			listaTabla.add(item);
		}while(cursor.moveToNext());
	}
	
	return listaTabla;
}


public ArrayList<ItemTablita> BuscarPorDestino(Context contexto,String Parametro){
	
	ArrayList<ItemTablita> listaTarjetas=null;
	
	// COnexion a la BD
	SqlTaximetro tarjetaDB =new SqlTaximetro(contexto,DB_NAME,null,1);
	SQLiteDatabase  db = tarjetaDB.getReadableDatabase();
	listaTarjetas = new ArrayList<ItemTablita>();
	
	// Consulta sobre la bd
	//Cursor cursor = db.query(TABLA_NAME, new String[]{"tar_titular","tar_nombre","tar_numero"}, 
		//	null,null,null,null,"tar_nombre");
	
	String[] parametrosDeBusqueda=new String[]{"%"+Parametro+"%","%"+Parametro+"%"};
	String sql="SELECT id_tt, p_partida, p_final, precio FROM ttarifa  WHERE p_partida like ? or p_final like ?";
	Cursor cursor=db.rawQuery(sql, parametrosDeBusqueda);

	if(cursor.moveToFirst()){
		
		// Recorrer los resultados
		do{
			ItemTablita item= new ItemTablita(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
			listaTarjetas.add(item);
		}while(cursor.moveToNext());
	}
	
	return listaTarjetas;
}




public ArrayList<ItemConsulta> listarConsulta(Context contexto){
	
	ArrayList<ItemConsulta> listaConsultas=null;
	SqlTaximetro tarjetaDB =new SqlTaximetro(contexto,DB_NAME,null,1);
	// Referencia a la BD
	SQLiteDatabase  db = tarjetaDB.getReadableDatabase();
	
	listaConsultas = new ArrayList<ItemConsulta>();
	
	// Consulta sobre la bd
	Cursor cursor = db.query(TABLA_NAME2, new String[]{"ruta","valor","distancia"}, 
			null,null,null,null,null);
	if(cursor.moveToFirst()){
		
		// Recorrer los resultados
		do{
			//falta corregir
		//	ItemConsulta item=new ItemConsulta(cursor.getString(1),
	    //				cursor.getString(2),cursor.getString(3));
		//	listaConsultas.add(item);
		}while(cursor.moveToNext());
	}
	
	return listaConsultas;
}


	public ArrayList<ItemConsulta> BuscarPorFecha(Context contexto, Integer id_u, String fecha_desde, String fecha_hasta){
		ArrayList<ItemConsulta> listarcarreras = null;
		SqlTaximetro carrerasDB = new SqlTaximetro(contexto, DB_NAME, null, 1);
		SQLiteDatabase db = carrerasDB.getReadableDatabase();
		listarcarreras = new ArrayList<ItemConsulta>();
		String[] parametrosBusqueda = new String[]{id_u.toString(), fecha_desde.toString(), fecha_hasta.toString()};
		String sql = "Select origen, destino, km, valor From " +TABLA_NAME2+
								" WHERE id_usuario=? and fecha>=? and fecha<=?";
		Cursor cursor = db.rawQuery(sql, parametrosBusqueda);
			if(cursor.moveToFirst()){
					do{
						ItemConsulta item = new ItemConsulta(cursor.getString(0), cursor.getString(1), 
																cursor.getDouble(2), cursor.getDouble(3));
						listarcarreras.add(item);
					}while(cursor.moveToNext());
			}
		db.close();
		return listarcarreras;
		}
	
	
	
	public Integer numero_de_carreras(Context contexto, Integer id_u, String fecha_desde, String fecha_hasta){
		SqlTaximetro carrerasDB = new SqlTaximetro(contexto, DB_NAME, null, 1);
		Integer numerodecarreras=0;
		SQLiteDatabase db = carrerasDB.getReadableDatabase();
		String[] parametrosBusqueda = new String[]{id_u.toString(), fecha_desde.toString(), fecha_hasta.toString()};
		String sql = "Select COUNT(id_c) From " +TABLA_NAME2+ " WHERE id_usuario=? and fecha>=? and fecha<=?";
		Cursor cursor = db.rawQuery(sql, parametrosBusqueda);
			if(cursor.moveToFirst()){
				do{
					numerodecarreras = cursor.getInt(0);
				}while(cursor.moveToNext());
			}
		db.close();
		return numerodecarreras;
	}
	
	public Double valor_total(Context contexto, Integer id_u, String fecha_desde, String fecha_hasta){
		SqlTaximetro carrerasDB = new SqlTaximetro(contexto, DB_NAME, null, 1);
		Double total=0.0;
		SQLiteDatabase db = carrerasDB.getReadableDatabase();
		String[] parametrosBusqueda = new String[]{id_u.toString(), fecha_desde.toString(), fecha_hasta.toString()};
		String sql = "Select SUM(valor) From " +TABLA_NAME2+ " WHERE id_usuario=? and fecha>=? and fecha<=?";
		Cursor cursor = db.rawQuery(sql, parametrosBusqueda);
			if(cursor.moveToFirst()){
				do{
					total = cursor.getDouble(0);
				}while(cursor.moveToNext());
			}
		db.close();
		return total;
	}



}