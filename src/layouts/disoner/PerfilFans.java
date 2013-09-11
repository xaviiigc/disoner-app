/*	Autor: Marcos Aguayo Mora
 * 	Fecha de inicio: 06/05/13
 *  Fecha de finalización: 08/05/13
 *  Objetivo: Clase para el fragmento Fans
 */

package layouts.disoner;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import adaptadores.disoner.AdaptadorAmigosFans;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import clases.disoner.AmigosFans;
import constantes.disoner.Info;
import disoner.login.R;

public class PerfilFans extends ListFragment{
	
	//Variables
	private List<String> resultado = new ArrayList<String>();
	private List<AmigosFans> fans = new ArrayList<AmigosFans>();
	private boolean isCargado = false;
	private Integer error = 2;
	private TextView txtInfo;
	

	/**
	 * Metodo onCreateView
	 */
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
    	
        View v = inflater.inflate(R.layout.activity_perfil_listas, container, false);  
                    
        txtInfo = (TextView) v.findViewById(R.id.txtPerfilListasInformacion);  
        
        if(!isCargado){
			new cargarFans().execute(Perfil.getID());
			isCargado = true;
		}
        cargarLista(error);
        return v;  
    }
	
	
	/**
	 * Clase privada cargarFans
	 * Obtiene una lista de los fans mediante una petición al servidor
	 * @extends AsyncTask
	 */
	private class cargarFans extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				resultado = Info.CONEXION.getFans(id[0]);
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
	 * Carga la lista de fans en el ListView
	 * @param codigoError
	 * -1: Error del servidor
	 * 0: No hay fans
	 * 1: Ok
	 */
	private void cargarLista(Integer codigoError){
		
		error = codigoError;
		
		switch(codigoError){
		
			case -1:
				
				txtInfo.setText(getString(R.string.msg_error_servidor));
				txtInfo.setVisibility(View.VISIBLE);
				break;
				
			case 0:
				
				txtInfo.setText(getString(R.string.msg_perfil_fans));
				txtInfo.setVisibility(View.VISIBLE);
				break;
			
			case 1: 
				
				for(int i = 0 ; i < resultado.size() ; i+=3){

					fans.add(new AmigosFans(
							resultado.get(i),
							resultado.get(i+1),
							Info.URL_FOTO_PERFIL+resultado.get(i+2)));
				}
				AdaptadorAmigosFans adapter = new AdaptadorAmigosFans(getActivity(), fans);
	  	        setListAdapter(adapter);
		     	error = 2;
		}
	}
	
	
	
	/**
	 * Metodo onListItemClick
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(getActivity(), Perfil.class);
		Bundle bundle = new Bundle();
		bundle.putString("id", fans.get(position).getID());
		bundle.putString("nombre", fans.get(position).getNombre());
		i.putExtras(bundle);
		startActivity(i);
		getActivity().finish();
	}
}
