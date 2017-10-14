package net.micrositios.pqrapp.encuesta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class PruebaInternet {

	private Context context;
	
	public PruebaInternet(Context con){
		context=con;
	}
	
	
	public int tipoconexion(){
		//Recogemos el servicio ConnectivityManager
		//el cual se encarga de todas las conexiones del terminal
			int estadoconexion=0;
		ConnectivityManager conMan = (ConnectivityManager) 
		      context.getSystemService(context.CONNECTIVITY_SERVICE);
		//Recogemos el estado del 3G
		
		//como vemos se recoge con el par�metro 0
		State internet_movil = conMan.getNetworkInfo(0).getState();
		//Recogemos el estado del wifi
		//En este caso se recoge con el par�metro 1
		State wifi = conMan.getNetworkInfo(1).getState();
		//Miramos si el internet 3G est� conectado o conectandose...
		    if (internet_movil == NetworkInfo.State.CONNECTED 
		     || internet_movil == NetworkInfo.State.CONNECTING) {
		     ///////////////
		    	estadoconexion=1;
		     //El movil est� conectado por 3G
		     //En este ejemplo mostrar�amos mensaje por pantalla
//		     Toast.makeText(context.getApplicationContext(), "Conectado por 3G"
//		       , Toast.LENGTH_LONG).show();
		     //Si no esta por 3G comprovamos si est� conectado o conectandose al wifi...
		    } else if (wifi == NetworkInfo.State.CONNECTED 
		      || wifi == NetworkInfo.State.CONNECTING) {
		     ///////////////
		    	estadoconexion=2;
		     //El movil est� conectado por WIFI
		     //En este ejemplo mostrar�amos mensaje por pantalla
//		     Toast.makeText(context.getApplicationContext(), "Conectado por WIFI"
//		       , Toast.LENGTH_LONG).show();
		     }
		    
		    return estadoconexion;
		    
		}
	
	
	public boolean estadoConexion() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		
		//Toast.makeText(context.getApplicationContext(), "Conectado a internet", Toast.LENGTH_LONG).show();
		return true;
		}
		//Toast.makeText(context.getApplicationContext(), "No hay conexi�n a internet", Toast.LENGTH_LONG).show();
		return false;
		}
	
	/**
	 * The method Verifies if the device has a WIFI connection
	 * @return
	 */
	public boolean estadoConexionWIFI()
	{
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected())
		{
			return true;
		}else
		{
			return false;
		}
	
		
	}
	
	
}
