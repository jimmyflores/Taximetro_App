package ec.edu.upse.taximetro_app.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlTaximetro extends SQLiteOpenHelper {

	String sql = "CREATE TABLE personas (" +
			" id_p INTEGER PRIMARY KEY AUTOINCREMENT," +
			" nombres TEXT," +
			" apellidos TEXT," +
			" email TEXT," +
			" estado TEXT )";
	
	
	String sql1 = "CREATE TABLE usuarios (" +
			" id_u INTEGER PRIMARY KEY AUTOINCREMENT," +
			" id_persona INTEGER NOT NULL," +
			" usuario TEXT," +
			" clave TEXT," +
			" estado TEXT, " +
			"FOREIGN KEY (id_persona) REFERENCES personas(id_p))";
	
	String sqtabla = "CREATE TABLE ttarifa (" +
			" id_tt INTEGER PRIMARY KEY AUTOINCREMENT," +
			" p_partida TEXT," +
			" p_final TEXT," +
			" precio DOUBLE )";
	
	
	String sqlcarrera = "CREATE TABLE carrera (" +
			" id_c INTEGER PRIMARY KEY AUTOINCREMENT," +
			" id_usuario INTEGER NOT NULL," +
			" id_tarifa INTEGER NOT NULL," +
			" km DOUBLE," +
			" valor DOUBLE," +
			" origen TEXT," +
			" latitud_origen DOUBLE," +
			" longitud_origen DOUBLE," +
			" destino TEXT," +
			" latitud_destino DOUBLE," +
			" longitud_destino DOUBLE," +
			" fecha TEXT, " +
			" duracion_carrera TEXT, " +
			"FOREIGN KEY (id_usuario) REFERENCES usuarios(id_u), "+
			"FOREIGN KEY (id_tarifa) REFERENCES tarifa(id_t))";
	
	String sqltarifa = "CREATE TABLE tarifa (" +
			"id_t INTEGER PRIMARY KEY AUTOINCREMENT," +
			" descripcion TEXT," +
			" arranque_tarifa DOUBLE," +
			" km_recorrido DOUBLE," +
			" min_espera DOUBLE," +
			" carrera_min DOUBLE," +
			" estado TEXT )" ;
    
	
	public SqlTaximetro(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		db.execSQL("PRAGMA foreign_keys=ON;");
	}


	@Override
	public void onCreate(SQLiteDatabase sqldb) {
		// TODO Auto-generated method stub
		sqldb.execSQL(sql);
		sqldb.execSQL(sql1);
		sqldb.execSQL(sqtabla);
		sqldb.execSQL(sqlcarrera);
		sqldb.execSQL(sqltarifa);
		
		sqldb.execSQL("CREATE TRIGGER fk_personas" +
			    " BEFORE" +
			    " INSERT ON usuarios FOR EACH ROW BEGIN"+
			    " SELECT CASE WHEN ((SELECT id_p FROM personas"+ 
			    " WHERE id_p=new.id_persona) IS NULL)"+
			    " THEN RAISE (ABORT,'Foreign Key Violation')" +
			    " END;"+
			    " END;");
		
		
		sqldb.execSQL("CREATE TRIGGER fk_persona " +
			    " BEFORE INSERT ON carrera FOR EACH ROW BEGIN"+
			    " SELECT CASE WHEN ((SELECT p.id_u FROM usuarios p "+ 
			    " WHERE p.id_u=new.id_usuario) IS NULL)"+
			    " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
			    "  END;");
		
		sqldb.execSQL("CREATE TRIGGER fk_tarifa " +
			    " BEFORE INSERT ON carrera FOR EACH ROW BEGIN"+
			    " SELECT CASE WHEN ((SELECT t.id_t FROM tarifa t"+ 
			    " WHERE t.id_t=new.id_tarifa) IS NULL)"+
			    " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
			    "  END;");
		
		
		
		sqldb.execSQL("INSERT INTO tarifa VALUES(NULL, 'Diurna',0.35,0.26,0.06,1.00,'A')");
		sqldb.execSQL("INSERT INTO tarifa VALUES(NULL, 'Nocturna',0.40,0.30,0.06,1.10,'A')");
		
		
		
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'La Libertad', 2.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'La Libertad Zona Periferica', 3.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Santa Elena', 1.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Santa Elena Zona Periferica', 1.50)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Ballenita', 1.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Muey Centri', 3.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Muey Zona Periferica', 3.50)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Santa Rosa - San Lorenzo', 4.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Salinas - Base', 5.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Ancon', 5.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Anconcito', 6.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Atahualpa', 8.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Tambo', 7.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Chanduy', 7.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Baños de San Vicente', 7.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Colonche', 9.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'San Pablo', 5.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Monteverde', 8.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Ayangue', 15.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Olon', 25.00)");
		sqldb.execSQL("INSERT INTO ttarifa VALUES(NULL, 'Terminal Terrestre', 'Montañita', 25.00)");
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqldb, int arg1, int arg2) {
		// TODO Auto-generated method stub
		sqldb.execSQL("DROP TABLE IF EXISTS tarifa");
		sqldb.execSQL("DROP TABLE IF EXISTS ttarifa");
		sqldb.execSQL("DROP TABLE IF EXISTS personas");
		sqldb.execSQL("DROP TABLE IF EXISTS usuarios");
		sqldb.execSQL("DROP TABLE IF EXISTS carrera");
	        //Se crea la nueva versiÃ³n de la tabla
	        sqldb.execSQL(sql);
	        sqldb.execSQL(sql1);
	        sqldb.execSQL(sqtabla);
	        sqldb.execSQL(sqltarifa);
	        sqldb.execSQL(sqlcarrera);
	}

}
