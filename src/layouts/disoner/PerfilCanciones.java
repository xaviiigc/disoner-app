/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 06/05/13
 *  Fecha de finalización: 07/05/13
 *  Objetivo: Clase para el fragmento Canciones
 */

package layouts.disoner;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import adaptadores.disoner.AdaptadorCanciones;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import clases.disoner.Cancion;
import constantes.disoner.Info;
import disoner.login.R;

public class PerfilCanciones extends ListFragment{
	
	//Variables
	private List<String> resultado = new ArrayList<String>();
	private List<Cancion> canciones = new ArrayList<Cancion>();
	private boolean isCargado = false;
	private Integer error = 2;
	private TextView informacion;
	

	/**
	 * Metodo onCreateView
	 */
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
    	
        View v = inflater.inflate(R.layout.activity_perfil_listas, container, false);  
                    
        informacion = (TextView) v.findViewById(R.id.txtPerfilListasInformacion);
        
        if(!isCargado){
			new cargarCanciones().execute(Perfil.getID());
			isCargado = true;
		}
        cargarLista(error);
        return v;
    }
	
	

	/**
	 * Clase privada cargarCanciones
	 * Carga las canciones del perfil mediante una petición al servidor
	 * @extends AsyncTask
	 */
	private class cargarCanciones extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				
				resultado = Info.CONEXION.getCancionesPerfil(id[0]);
				if(resultado.get(0).equals("0")) return 0;
				else return 1;
			
			} catch (JSONException e) {
				return -1;
			}
		}
		
		
		@Override
		protected void onPostExecute(Integer result) {
		
			cargarLista(result);
		}
	}
	
	
	
	/**
	 * Metodo cargarLista
	 * Carga la lista de canciones
	 * @param codigoError 
	 * -1: Error de conexión
	 * 0: No hay canciones
	 * 1: Ok
	 */
	private void cargarLista(Integer codigoError){
		
		switch(codigoError){
		
		case -1:
			
			informacion.setText(getString(R.string.msg_error_servidor));
			informacion.setVisibility(View.VISIBLE);
			error = -1;
			break;
		
		case 0:
			
			informacion.setText(getString(R.string.msg_perfil_canciones));
			informacion.setVisibility(View.VISIBLE);
			error = 0;
			break;
			
		case 1:
			
			for(int i = 0 ; i < resultado.size() ; i+=6){
				canciones.add(new Cancion(
						resultado.get(i), 
						resultado.get(i+1), 
						resultado.get(i+2), 
						resultado.get(i+3), 
						resultado.get(i+4), 
						resultado.get(i+5))
				);
			}		
	     	AdaptadorCanciones adapter = new AdaptadorCanciones(getActivity(), canciones);
  	        setListAdapter(adapter);
		}
	}
	
	
	
	/**
	 * Metodo onListItemClick
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(getActivity(), Reproductor.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", canciones.get(position).getCancion());
		bundle.putString("nombreCancion", canciones.get(position).getNombre());
		bundle.putString("id", canciones.get(position).getId().toString());
		i.putExtras(bundle);
		startActivity(i);
	}
}
