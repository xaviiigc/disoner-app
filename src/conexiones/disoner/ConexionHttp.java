/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 30/04/13
 *  Fecha de finalización: 29/05/13
 *  Objetivo: Clase que realiza todas las interraciones de conexión con el WebService
 */

package conexiones.disoner;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import constantes.disoner.Info;

import android.util.Log;


public class ConexionHttp {
	
	//Variables
	private ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
	private JSONArray array;
	private List<String> lista;
	private JSONObject datos;

	
	
	/**
	 * Metodo convertStreamToString
	 * Convierte un InputStream pasado por parametro a String
	 * @param is (InputStream)
	 * @return String 
	 */
	private static String convertStreamToString(InputStream is){
		
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    	StringBuilder sb = new StringBuilder();
    	
    	String line = null;
    	try {
    		while ((line = reader.readLine()) != null) {
    			sb.append(line + "\n");
    		}
    	} 
    	catch (IOException e) {
    		Log.e("PHP Client","Error : "+e.getMessage());
    	} 
    	finally {
    		try {
    			is.close();
    		} catch (IOException e1) {
    			Log.e("PHP Client","Error : "+e1.getMessage());
    		}
    	}
    	return sb.toString();
    }
    

	
	/**
	 * Metodo getJsonArray
	 * Realiza una conexión HTTP con el servidor y devuleve un Array JSON con los datos obtenidos
	 * @param Parameters: parametros que le pasamos al servidor (ArrayList<NameValuePair>)
	 * @param key: palabra clave para recojer el objeto JSON 
	 * @return JSONArray
	 */
	public JSONArray getJsonArray(ArrayList<NameValuePair> Parameters, String key){
		
		try{
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Info.URL_SERVICIO);
			httppost.setEntity(new UrlEncodedFormEntity(Parameters));
			HttpResponse response = httpclient.execute(httppost);
	        String result= convertStreamToString(response.getEntity().getContent());
	        JSONObject jsonObject = new JSONObject(result); 
	        JSONArray jArray = jsonObject.getJSONArray(key);

	        return jArray;
	        
		}catch(Exception e){
			Log.e("Metodo getJsonArray", "Error en la conexion HTTP: "+e.toString());
			return null;
		}
	}
	
	
	
	/**
	 * Metodo getSesion
	 * Obtiene los datos del usuario que quiere iniciar sesion
	 * @param correo - correo electronico
	 * @param password - contraseña
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> getSesion(String correo, String password) throws JSONException{
		
		//Crea un arraylist para almacenar parametros i valores con pares de claves
		ArrayList<NameValuePair> parameters=new ArrayList<NameValuePair>();
		
		//Nombre del metodo
		parameters.add(new BasicNameValuePair("nombre","verificarLogin"));
		parameters.add(new BasicNameValuePair("correo",correo));
		parameters.add(new BasicNameValuePair("pwd",password));

		//Obtiene el resultado del servidor
		array = getJsonArray(parameters, "usuario");
		lista = new ArrayList<String>();
		
		if(array != null){	

			datos = array.getJSONObject(0);
			
			lista.add(datos.getString("id"));
			
			if(!lista.get(0).equals("0")){
				lista.add(datos.getString("nombre"));
				lista.add(datos.getString("imagen"));
				lista.add(datos.getString("biografia"));
				lista.add(datos.getString("gustos"));
				lista.add(datos.getString("cuenta"));
				lista.add(datos.getString("slug"));
			}
		}
		return lista;
	}
	
	
	
	/**
	 * Metodo getCancionesInicio
	 * Obtiene las canciones que se cargaran en la pantalla Inicio
	 * @param id - Id del ususario
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> getCancionesInicio(String id) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "cancionesInicio"));
		parametros.add(new BasicNameValuePair("id", id));
		
		array = getJsonArray(parametros, "canciones");
		lista = new ArrayList<String>();
		
		
		if(array != null){	
	
			for(int i = 0 ; i<array.length() ; i++ ){
				
				datos = array.getJSONObject(i);
				lista.add(datos.getString("id"));
				
				if(!lista.get(0).equals("0")){
					
					lista.add(datos.getString("nombreCancion"));
					lista.add(datos.getString("autor_id"));
					lista.add(datos.getString("nombre"));
					lista.add(datos.getString("cancion"));
					lista.add(datos.getString("imagen"));
				}
			}
		}
		return lista;
	}
	
	
	
	/**
	 * Metodo getCancionesPerfil
	 * Obtiene las canciones de la pantalla Perfil
	 * @param id - Id del usuario
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> getCancionesPerfil(String id) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "cancionesPerfil"));
		parametros.add(new BasicNameValuePair("id", id));
		
		array = getJsonArray(parametros, "canciones");
		lista = new ArrayList<String>();
		
		if(array != null){
		
			for(int i=0 ; i<array.length() ; i++){
				
				datos = array.getJSONObject(i);
				lista.add(datos.getString("id"));
				
				if(!lista.get(0).equals("0")){
					
					lista.add(datos.getString("nombreCancion"));
					lista.add(datos.getString("autor_id"));
					lista.add(datos.getString("nombre"));
					lista.add(datos.getString("cancion"));
					lista.add(datos.getString("imagen"));
				}
			}
		}
		return lista;
	}
	
	
	
	/**
	 * Metodo getAmigos
	 * Obtiene los Amigos de un usuario pasado por parametros
	 * @param id - ID del Usuario
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> getAmigos(String id) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "getAmigos"));
		parametros.add(new BasicNameValuePair("id", id));
		
		array = getJsonArray(parametros, "amigos");
		lista = new ArrayList<String>();
		
		if(array != null){
		
			for(int i = 0; i<array.length() ; i++){
				
				datos = array.getJSONObject(i);
				lista.add(datos.getString("id"));
				
				if(!lista.get(0).equals("0")){
				
					lista.add(datos.getString("nombre"));
					lista.add(datos.getString("imagen"));
				}
			}
		}
		return lista;
	}
	
	
	
	/**
	 * Metodo getFans
	 * Obtiene los Fans de un usuario pasado por parametros
	 * @param id - ID del Usuario
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> getFans(String id) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "getFans"));
		parametros.add(new BasicNameValuePair("id", id));
		
		array = getJsonArray(parametros, "fans");
		lista = new ArrayList<String>();
		
		if(array != null){
		
			for(int i = 0; i<array.length() ; i++){
				
				datos = array.getJSONObject(i);
				lista.add(datos.getString("id"));
				
				if(!lista.get(0).equals("0")){
					
					lista.add(datos.getString("nombre"));
					lista.add(datos.getString("imagen"));
				}
			}
		}
		return lista;
	}
	
	
	
	/**
	 * Metodo getComentario
	 * Obtiene los comentarios de una canción
	 * @param id - ID de la canción
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> getComentarios(String id) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "obtenerComentarios"));
		parametros.add(new BasicNameValuePair("id", id));
		
		array = getJsonArray(parametros, "comentarios");
		lista = new ArrayList<String>();
		
		for(int i=0 ; i<array.length() ; i++){
			
			datos = array.getJSONObject(i);
			lista.add(datos.getString("nombre"));
			lista.add(datos.getString("comentario"));
		}
		return lista;
	}
	
	
	
	/**
	 * Metodo insertarComentario
	 * Permite insertar un comentario en una canción
	 * @param idCancion - ID de la canción
	 * @param id - ID del Usuario
	 * @param comentario - String con el comentario
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> insertarComentario(String idCancion, String id, String comentario) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "insertarComentario"));
		parametros.add(new BasicNameValuePair("id_cancion", idCancion));
		parametros.add(new BasicNameValuePair("id_usuario", id));
		parametros.add(new BasicNameValuePair("comentario", comentario));
		
		array = getJsonArray(parametros, "comentarios");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
	
	
	
	/**
	 * Metodo getListasReproduccion
	 * Obtiene las listas de reproducción del usuario
	 * @param id - ID del usuario
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> getListasReproduccion(String id) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "getListasReproduccion"));
		parametros.add(new BasicNameValuePair("id", id));
		
		array = getJsonArray(parametros, "listas");
		lista = new ArrayList<String>();
		
		if(array != null){
		
			for(int i = 0; i<array.length() ; i++){
				
				datos = array.getJSONObject(i);
				lista.add(datos.getString("id"));
				
				if(!lista.get(0).equals("0")){
					
					lista.add(datos.getString("nombre"));
				}
			}
		}
		return lista;
	}
	
	
	
	/**
	 * Metodo getCancionesLista
	 * Obtiene las canciones de una lista de reproducción
	 * @param id - Id de la Lista de Reproducción
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> getCancionesLista(String id) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "getCancionesLista"));
		parametros.add(new BasicNameValuePair("id", id));
		
		array = getJsonArray(parametros, "canciones");
		lista = new ArrayList<String>();
		
		if(array != null){
		
			for(int i = 0; i<array.length() ; i++){
				
				datos = array.getJSONObject(i);
				lista.add(datos.getString("id"));
				
				if(!lista.get(0).equals("0")){
					lista.add(datos.getString("nombreCancion"));
					lista.add(datos.getString("autor_id"));
					lista.add(datos.getString("nombre"));
					lista.add(datos.getString("cancion"));
					lista.add(datos.getString("imagen"));
				}
			}
		}
		return lista;
	}
	
	
	
	/**
	 * Metodo insertarCancionLista
	 * Inserta una canción en una lista de reproducción
	 * @param id_lista - ID de la lista de reproducción
	 * @param id_cancion - ID de la canción
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> insertarCancionLista(String id_lista, String id_cancion) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "insertarCancionLista"));
		parametros.add(new BasicNameValuePair("id_lista", id_lista));
		parametros.add(new BasicNameValuePair("id_cancion", id_cancion));
		
		array = getJsonArray(parametros, "agregar");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
	
	
	
	/**
	 * Metodo eliminarCancionLista
	 * Elimina una canción de una lista de reproducción
	 * @param id_lista - ID de la lista de reproducción
	 * @param id_cancion - ID de la canción
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> eliminarCancionLista(String id_lista, String id_cancion) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "eliminarCancionLista"));
		parametros.add(new BasicNameValuePair("id_lista", id_lista));
		parametros.add(new BasicNameValuePair("id_cancion", id_cancion));
		
		array = getJsonArray(parametros, "eliminar");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
	
	
	
	/**
	 * Metodo getLikes
	 * Obtiene los "Me gusta" de una canción 
	 * @param id_cancion - ID de la canción
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> getLikes(String id_cancion) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "getLikes"));
		parametros.add(new BasicNameValuePair("id_cancion", id_cancion));
		
		array = getJsonArray(parametros, "likes");
		lista = new ArrayList<String>();
		
		if(array != null){
			
			for(int i = 0 ; i<array.length() ; i++){
			
				datos = array.getJSONObject(i);
				lista.add(datos.getString("usuario_id"));
			}
		}
		return lista;
	}
	
	
	
	/**
	 * Metodo insertarLike
	 * Inserta un "Me gusta" en una canción
	 * @param id_usuario - ID del usuario que pone Me gusta
	 * @param id_cancion - ID de la canción
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> insertarLike(String id_usuario, String id_cancion) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "insertarLike"));
		parametros.add(new BasicNameValuePair("id_usuario", id_usuario));
		parametros.add(new BasicNameValuePair("id_cancion", id_cancion));
		
		array = getJsonArray(parametros, "insertar");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
	
	
	
	/**
	 * Metodo eliminarLike
	 * Elimina un "Me gusta" de una canción
	 * @param id_usuario - ID del usuario que pone Me gusta
	 * @param id_cancion - ID de la canción
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> eliminarLike(String id_usuario, String id_cancion) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "eliminarLike"));
		parametros.add(new BasicNameValuePair("id_usuario", id_usuario));
		parametros.add(new BasicNameValuePair("id_cancion", id_cancion));
		
		array = getJsonArray(parametros, "eliminar");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
	
	
	
	/**
	 * Metodo enviarNotificacion
	 * Envia una notificación a un determinado usuario cuando se produce un determinado evento
	 * @param id_amigo - ID del Amigo que recibira la notificación
	 * @param id_usuario - ID del Usuario que manda la notificación
	 * @param notificacion - String con el contenido de la notificación
	 * @param url - Url a la que dirige dicha notificación
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> enviarNotificacion(String id_amigo, String id_usuario, String notificacion, String url) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "insertarNotificacion"));
		parametros.add(new BasicNameValuePair("id_amigo", id_amigo));
		parametros.add(new BasicNameValuePair("id_usuario", id_usuario));
		parametros.add(new BasicNameValuePair("notificacion", notificacion));
		parametros.add(new BasicNameValuePair("url", url));
		
		array = getJsonArray(parametros, "notificacion");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
	
	
	
	/**
	 * Metodo addAmigo
	 * Permite añadir un nuevo usuario a tu lista de amigos
	 * @param id_usuario - ID del usuario
	 * @param id_amigo - ID del usuario que queremos añadir como amigo
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> addAmigo(String id_usuario, String id_amigo) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "addAmigo"));
		parametros.add(new BasicNameValuePair("id_usuario", id_usuario));
		parametros.add(new BasicNameValuePair("id_amigo", id_amigo));
		
		array = getJsonArray(parametros, "amigos");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
	
	
	
	/**
	 * Metodo deleteAmigo
	 * Elimina la amistad con un usuario
	 * @param id_usuario - ID del usuario
	 * @param id_amigo - ID del amigo que queremos eliminar
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> deleteAmigo(String id_usuario, String id_amigo) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "deleteAmigo"));
		parametros.add(new BasicNameValuePair("id_usuario", id_usuario));
		parametros.add(new BasicNameValuePair("id_amigo", id_amigo));
		
		array = getJsonArray(parametros, "amigos");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
	
	
	
	/**
	 * Metodo insertarListaReproduccion
	 * Permite añadir una nueva lista de reproducción
	 * @param id_usuario - ID del usuario
	 * @param lista_nombre - nombre de la lista
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> insertarListaReproduccion(String id_usuario, String lista_nombre) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "insertarListaReproduccion"));
		parametros.add(new BasicNameValuePair("id_usuario", id_usuario));
		parametros.add(new BasicNameValuePair("lista_nombre", lista_nombre));
		
		array = getJsonArray(parametros, "insertar");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
	
	
	
	/**
	 * Metodo eliminarListaReproducción
	 * Permite eliminar una lista de reproducción
	 * @param id_usuario - ID del usuario
	 * @param id_lista - ID de la lista
	 * @return List<String> con el resultado
	 * @throws JSONException
	 */
	public List<String> eliminarListaReproduccion(String id_usuario, String id_lista) throws JSONException{
		
		parametros.add(new BasicNameValuePair("nombre", "eliminarListaReproduccion"));
		parametros.add(new BasicNameValuePair("id_usuario", id_usuario));
		parametros.add(new BasicNameValuePair("id_lista", id_lista));
		
		array = getJsonArray(parametros, "eliminar");
		lista = new ArrayList<String>();
		
		datos = array.getJSONObject(0);
		lista.add(datos.getString("resultado"));
		
		return lista;
	}
}