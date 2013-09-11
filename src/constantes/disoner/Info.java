/*	Autor: Xavier Güell Castella, Marcos Aguayo Mora
 * 	Fecha de inicio: 29/05/13
 *  Fecha de finalización: 23/05/13
 *  Objetivo: Clase que contiene constantes bastante utilizadas por la aplicación
 */

package constantes.disoner;

import java.util.ArrayList;
import java.util.List;

import conexiones.disoner.ConexionHttp;

import android.media.MediaPlayer;
import android.os.Bundle;

public class Info {

	//CONEXION
	public static ConexionHttp CONEXION = new ConexionHttp();
	
	//CONSTANTE PARA ALMACENAR EL ID DEL USUARIO ACTUAL
	public static String USUARIO_ID;
	public static String USUARIO_NOMBRE;
	public static String USUARIO_FOTO = "http://disoner.com/static/fotos_perfil/";
	public static String USUARIO_BIOGRAFIA;
	public static String USUARIO_GUSTOS;
	public static String USUARIO_TIPO_CUENTA;
	public static String USUARIO_TIPO_ALMACENAMIENTO;
	public static String USUARIO_CORREO;
	public static String USUARIO_SLUG;
	
	//CONSTANTES PARA LAS RUTAS
	public static String URL_SERVICIO="http://5.56.58.218/web_service/android_ws.php";
	public static String URL_PERFIL = "http://disoner.com/perfil/";
	public static String URL_FOTO_CANCION = "http://disoner.com/static/fotos_canciones/";
	public static String URL_CANCION = "http://disoner.com/static/canciones/";
	public static String URL_FOTO_PERFIL = "http://disoner.com/static/fotos_perfil/";
	
	//CONSTANTES PARA LAS PANTALLAS ACTUALES
	public static int INICIO = 1;
	public static int PERFIL = 2;
	public static int CANCION = 3;
	
	//CONSTANTES DEL REPRODUCTOR
	public static MediaPlayer MP = new MediaPlayer();
	public static String ID_CANCION = "";
	public static Bundle EXTRAS = new Bundle();
	public static String CANCION_ELIMINAR = "";
	
	//CONSTANTES PARA LAS NOTIFICACIONES
	public static int NOTIFICACION_COMENTARIO = 1;
	public static int NOTIFICACION_LIKE = 2;
	public static int NOTIFICACION_AMISTAT = 3;
	
	
	//CONSTANTE QUE ALMACENA LA PANTALLA ACTUAL
	public static int PANTALLA_ACTUAL = INICIO;
	
	//CONSTANTE QUE ALMACENA LOS AMIGOS
	public static List<String> AMIGOS = new ArrayList<String>();
}
