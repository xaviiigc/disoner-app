/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 16/05/13
 *  Fecha de finalización: 22/05/13
 *  Objetivo: Clase para los fragmentos de cada lista de reproducción
 */

package layouts.disoner;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import adaptadores.disoner.AdaptadorCanciones;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import clases.disoner.Cancion;
import constantes.disoner.Info;
import disoner.login.R;

@SuppressLint("ValidFragment")
public class ListasCanciones extends ListFragment{
	
	//Variables
	private List<String> resultado = new ArrayList<String>();
	private List<Cancion> canciones = new ArrayList<Cancion>();
	private AdaptadorCanciones adaptador;
	private int posicion;
	private String idLista;
	private boolean isCargado = false;
	private TextView txtInfo;
	private Integer error = 2;
	
	
	/**
	 * Constructor ListasCanciones
	 * @param id - Id de la lista de reproducción
	 */
	public ListasCanciones(String id) {
		
		this.idLista = id;
	}
	
	
	
	/**
	 * Metodo getIdLIsta
	 * Devuelve el id de la lista de reproducción
	 * @return String id lista reproducción
	 */
	protected String getIdLista(){
		
		return this.idLista;
	}
	
	
	
	/**
	 * Metodo onCreateView
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
    	
        View v = inflater.inflate(R.layout.activity_perfil_listas, container, false); 
                    
        txtInfo = (TextView) v.findViewById(R.id.txtPerfilListasInformacion);
        
        if(getIdLista().equals("0")){
        	
        	txtInfo.setText(getString(R.string.msg_lista_no));
        	txtInfo.setVisibility(View.VISIBLE);
        }
        else{
	        if(!isCargado){
				new cargarCanciones().execute(getIdLista());
				isCargado = true;
			}
        }
        cargarLista(error);
        return v;  
	}
	
	
	
	/**
	 * Metodo onActivityCreated
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	
	
	/**
	 * Metodo onCreateContextMenu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        posicion = info.position;
        
        Cancion c = (Cancion) getListView().getAdapter().getItem(posicion);
        Info.CANCION_ELIMINAR = c.getId();
                
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.menu_contextual, menu);
	}
	
	
	
	/**
	 * Metodo onContextItemSelected
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		
		if(item.getItemId() == R.id.menu_cancion_eliminar){

			new eliminarCanciones().execute(ListasReproduccion.getIdLista(), Info.CANCION_ELIMINAR);			
		}
		return true;
	}
	

	
	/**
	 * Clase privada cargarCanciones
	 * carga las canciónes mediante una petición al servidor
	 * @extends AsyncTask
	 */
	private class cargarCanciones extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				resultado = Info.CONEXION.getCancionesLista(id[0]);
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
	 * Clase privada eliminarCanciones
	 * elimina las canciones mediante una petición al servidor
	 * @extends AsyncTask
	 *
	 */
	private class eliminarCanciones extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				resultado = Info.CONEXION.eliminarCancionLista(id[0], id[1]);
				if(resultado.get(0).equals("0")) return 0;
				else return 1;
			} catch (JSONException e) {
				return -1;
			}
		}
				
		@Override
		protected void onPostExecute(Integer result) {
		
			super.onPostExecute(result);
			eliminarCancion(result);
		}
	}
	
	
	
	/**
	 * Metodo eliminarCancion
	 * Elimina una canción de la lista de reproducción
	 * @param codigoError
	 */
	private void eliminarCancion(Integer codigoError){
		
		switch(codigoError){
		
			case 1:
				
				Toast.makeText(getActivity(), getString(R.string.msg_lista_cancion_eliminada), Toast.LENGTH_SHORT).show();
				getActivity().finish();
				startActivity(getActivity().getIntent());	
		}
	}
	

	
	/**
	 * Metodo cargarLista
	 * Carga una lista de reproducción
	 * @param codigoError
	 * -1: fallo en el servidor
	 * 0: No hay canciones
	 * 1: Ok
	 */
	private void cargarLista(Integer codigoError){
		
		error = codigoError;
		
		switch (codigoError) {
		
		case -1:
			
			txtInfo.setText(getString(R.string.msg_error_servidor));
			txtInfo.setVisibility(View.VISIBLE);
			break;
		
		case 0:
			
			txtInfo.setText(getString(R.string.msg_lista_vacias));
			txtInfo.setVisibility(View.VISIBLE);
			break;
		
		case 1:
			
			for(int i = 0 ; i < resultado.size() ; i+=6){
				canciones.add(new Cancion(
						resultado.get(i), 
						resultado.get(i+1),
						resultado.get(i+2), 
						resultado.get(i+3), resultado.get(i+4), 
						resultado.get(i+5)));
			}
			
			adaptador = new AdaptadorCanciones(getActivity(), canciones);
	     	setListAdapter(adaptador);
	     	error = 2;
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