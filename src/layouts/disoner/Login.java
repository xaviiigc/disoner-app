/*	Autor: Xavier Güell Castellà
 *  Fecha de inicio: 30/04
 * 	Fecha de finalizacion: 02/05
 * 	Objetivo: Clase para realizar el login en la plataforma
 */


package layouts.disoner;


import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import constantes.disoner.Info;
import disoner.login.R;


public class Login extends Activity implements OnClickListener {

	//Variables
	private TextView txtCorreo, txtPwd;
	private boolean isLogin = false;
	
	
	
	/**
	 * Metodo onCreate
	 * @param savedInstanceState
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Capturamos los eventos necesarios
		txtCorreo = (TextView)findViewById(R.id.txtLoginUsuario);
		txtPwd = (TextView)findViewById(R.id.txtLoginPwd);
		Button btnAceptar = (Button)findViewById(R.id.btnAceptar);
		 
		//Añadimos un listener al hacer click en el boton 'Aceptar'
		btnAceptar.setOnClickListener(this);
	}
	
	
	
	/**
	 * Metodo onClick
	 * Listener que captura en que vista hemos hecho clic
	 * @param elemento: Vista capturada
	 */
	
	public void onClick(View elemento){
		
		if(elemento.getId() == R.id.btnAceptar && !isLogin){

			new getID().execute(txtCorreo.getText().toString(), txtPwd.getText().toString());
				
		}
	}
	
	
	
	/**
	 * Metodo conectar
	 * Realiza el login a la aplicacion
	 * @param codigoError: Integer que identifica los distintos codigos de error
	 *  0: Usuario/Contraseña incorrectos
	 * -1: Error de conexion con el servidor
	 *  1: Ok
	 */
	
	public void conectar(Integer codigoError){
		
		switch(codigoError){
		
		case 0: Toast.makeText(this, getString(R.string.msg_login_incorrecto), Toast.LENGTH_SHORT).show();
				break;
		
		case -1: Toast.makeText(this, getString(R.string.msg_error_servidor), Toast.LENGTH_SHORT).show();
				 break;
				
		default: Intent i = new Intent(this, Inicio.class);
				 startActivity(i);
				 finish();
				 isLogin = true;
		}
	}
	
	
	
	/**
	 * Clase getID
	 * @extends AsyncTask
	 * Esta clase realiza una petición al servidor mediante un web service para obtener el ID de usuario que quiere loguearse
	 */
	
	private class getID extends AsyncTask<String, Void, Integer> {
		
		
		/**
		 * Metodo doInBackground
		 * Es la tarea que se ejecuta en segundo plano
		 */
		
		@Override
		protected Integer doInBackground(String... parametros) {

			try {
				//Realizamos la peticion
				List<String> resultado = Info.CONEXION.getSesion(parametros[0], parametros[1]);
				
				if(resultado.size() > 0){
				
					//Guardamos el id obtenido en las Constantes del programa
					Info.USUARIO_ID = resultado.get(0);
					
					//Se comprueba si el login es correcto
					if(Info.USUARIO_ID.equals("0")) return 0;
					else{
						//Si el login es correcto se almacenan los demas datos del usuario en las constantes locales
						Info.USUARIO_NOMBRE = resultado.get(1);
						Info.USUARIO_FOTO = Info.USUARIO_FOTO + resultado.get(2);
						Info.USUARIO_BIOGRAFIA = resultado.get(3);
						Info.USUARIO_GUSTOS= resultado.get(4);
						Info.USUARIO_TIPO_CUENTA = resultado.get(5);
						Info.USUARIO_SLUG = Info.URL_PERFIL + resultado.get(6);
						Info.USUARIO_CORREO = parametros[0];
						return 1;
					}
				}
				else return -1;

			} catch (JSONException e) {
				return -1;
			}		
		}
		
		
		
		/**
		 * Metodo onPostExecute
		 * Este metodo es llamado cuando la tarea asincrona termina, su funcion es llamar a otro metodo para 
		 * trabajar con los datos obtenidos de la tarea asincrona
		 */
		
		@Override
		protected void onPostExecute(Integer result) {

			conectar(result);	
		}
	}
}