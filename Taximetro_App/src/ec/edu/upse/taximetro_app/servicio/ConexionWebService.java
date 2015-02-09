package ec.edu.upse.taximetro_app.servicio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConexionWebService {
	private static final String URL_Registrar = "http://192.168.0.100:8080/WebServiceSAAP/services/RegistrarCarreras";
	private static final String NAMESPACE="http://funciones.servicios.com/";
	private static final String URL_Consultar = "http://192.168.0.100:8080/WebServiceSAAP/services/ConsultarCarreras";
	
	public static String getUrlRegistrar() {
		return URL_Registrar;
	}
	public static String getNamespace() {
		return NAMESPACE;
	}
	public static String getUrlConsultar() {
		return URL_Consultar;
	}
	
	public static boolean VerificaConexion(Context ctx)
	{
		boolean bConectado =false;
			ConnectivityManager connec = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			//no solo wifi, tambien GPRS
			NetworkInfo[] redes = connec.getAllNetworkInfo();
			for(int i =0; i<2; i++)
			{
				if(redes[i].getState()==NetworkInfo.State.CONNECTED)
				{
					bConectado=true;
				}
			}
			
		return bConectado;
	}
	
}
