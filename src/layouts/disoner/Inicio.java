/*	Autor: Marcos Aguayo Mora / Xavier Güell Castella
 *  Fecha de inicio: 30/04/13
 * 	Fecha de finalizacion: 02/05/13
 * 	Objetivo: Clase para el layout Inicio
 */


package layouts.disoner;


import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import adaptadores.disoner.AdaptadorCanciones;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import clases.disoner.Cancion;
import constantes.disoner.Info;
import disoner.login.R;


public class Inicio extends ActivityBase{
	
	//Variables
	private List<String> canciones = new ArrayList<String>();
	private List<Cancion> lista = new ArrayList<Cancion>();
	private TextView txtAviso;
	
	
	/**
	 * Metodo onCreate
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
		
		//Assignamos la pantalla actual
		Info.PANTALLA_ACTUAL = Info.INICIO;
		
		//Capturamos las vistas que necesitamos
		txtAviso = (TextView)findViewById(R.id.txtInicioAviso);
		
		//Ejecutamos la tarea asyncrona
		new getCancionesInicio().execute(Info.USUARIO_ID);
	}
	
	
	
	/**
	 * Metodo cargarCanciones
	 * Carga las canciones en el ListView, si no hay canciones muestra un mensaje
	 * @param codigoError
	 * -1: Error del servidor
	 * 0: No hay canciones
	 * 1: Ok
	 */
	public void cargarCanciones(Integer codigoError){
		
		
		switch(codigoError){
		
		case -1:
			txtAviso.setText(getString(R.string.msg_error_servidor));
			txtAviso.setVisibility(View.VISIBLE);
			break;
			
		case 0:
			txtAviso.setVisibility(View.VISIBLE);
			break;
			
		default:
			for(int i = 0 ; i < canciones.size() ; i+=6){
			
				lista.add(new Cancion(
						canciones.get(i), 
						canciones.get(i+1), 
						canciones.get(i+2),
						canciones.get(i+3), 
						canciones.get(i+4), 
						canciones.get(i+5)));
			}
			
  	        AdaptadorCanciones adapter = new AdaptadorCanciones(this, lista);
  	        getListView().setAdapter(adapter);
		}
	}


	
	/**
	 * clase privada getCancionesInicio
	 * @extends AsyncTask
	 * Esta clase realiza una petición al servidor mediante el WebService para obtener las canciones del Inicio
	 */
	private class getCancionesInicio extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... parametros) {
			try {
				canciones = Info.CONEXION.getCancionesInicio(parametros[0]);
				if(canciones.get(0).equals("0")) return 0;
				else return 1;
			} catch (JSONException e) {
				return -1;
			}
		}
		
		
		@Override
		protected void onPostExecute(Integer result) {
	
			cargarCanciones(result);
		}
	}
	
	
	

	/**
	 * Metodo onListItemClick
	 * Este metodo señala la acción a realizar al hacer clic sobre un elemento del ListView
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(this, Reproductor.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", lista.get(position).getCancion());
		bundle.putString("nombreCancion", lista.get(position).getNombre());
		bundle.putString("id", lista.get(position).getId().toString());
		bundle.putString("id_autor", lista.get(position).getAutorID().toString());
		i.putExtras(bundle);
		startActivity(i);
	}
	
	
	
	/**
	 * Metodo onDestroy
	 * Este metodo es llamado cuando se cierra el Layout en este caso paramos el MediaPlayer
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Info.MP.pause();	
	}
}
