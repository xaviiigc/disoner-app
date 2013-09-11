/*	Autor: Marcos Aguayo Mora
 * 	Fecha de inicio: 09/05/13
 *  Fecha de finalización: 12/05/13
 *  Objetivo: Clase que extiende de una tarea asincrona y permite enviar notificaciones a los usuarios
 */

package asynctask.disoner;

import java.util.List;

import org.json.JSONException;

import android.os.AsyncTask;
import constantes.disoner.Info;

public class EnviarNotificaciones extends AsyncTask<String, Void, Integer> {	
	
	
	/**
	 * Metodo doInBackground
	 * Este metodo ejecuta una tarea en segundo plano
	 */
	@Override
	protected Integer doInBackground(String... p) {
				
		try {
			List<String> resultado = Info.CONEXION.enviarNotificacion(p[0], p[1], p[2], p[3]);
			if(resultado.get(0).equals("0")) return 0;
			else return 1;
			
		} catch (JSONException e) {
			return -1;
		}
	}
		
		
	/**
	 * Metodo onPostExecute
	 * Este metodo es llamado cuando la tarea asincrona termina.
	 */
	@Override
	protected void onPostExecute(Integer result) {
	
		super.onPostExecute(result);
	}
	
	
	
	/**
	 * Metodo generarNotificación
	 * Genera un mensaje para rellenar la notificación
	 * @param usuario - Nombre del usuario que realiza la notificación
	 * @param obj - Objeto sobre el que se genera la notificación
	 * @param tipo - Tipo de notificación (Comentarios, Amistad, Me gusta)
	 * @return String Mensaje de la notificación
	 */
	public static String generarNotificacion(String usuario, String obj, int tipo){
		
		if(tipo == Info.NOTIFICACION_COMENTARIO) return usuario+ " ha comentado en tu canción \""+obj+"\"";
		if(tipo == Info.NOTIFICACION_LIKE) return "A "+usuario+" le gusta tu canción \""+obj+"\"";
		if(tipo == Info.NOTIFICACION_AMISTAT) return usuario+" ahora es tu amigo";
		
		return "";
	}
}
