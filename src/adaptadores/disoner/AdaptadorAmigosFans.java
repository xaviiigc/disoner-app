/*	Autor: Xavier Güell Castellà
 * 	Fecha de inicio: 06/05/13
 * 	Fecha de finalización: 07/05/13
 * 	Objetivo: Adaptador para inflar en un ListView los Amigos o Fans
 */

package adaptadores.disoner;

import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import asynctask.disoner.DescargarImagenes;
import asynctask.disoner.EnviarNotificaciones;
import clases.disoner.AmigosFans;
import constantes.disoner.Info;
import disoner.login.R;

public class AdaptadorAmigosFans extends BaseAdapter {
    
	//Variables
    private Activity activity;
    private List<AmigosFans> datos;
    private static LayoutInflater inflater = null;
    private DescargarImagenes imageLoader;
    private Button btnAmigo;
	private String id_amigo;
    
    
    /**
     * Constructor AdaptadorAmigosFans
     * @param a Actividad actual
     * @param d List<AmigosFans> lista de amigos / fans
     */
    public AdaptadorAmigosFans(Activity a, List<AmigosFans> d) {
        
    	this.activity = a;
        this.datos=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new DescargarImagenes(activity.getApplicationContext());
    }

    
    
    /**
     * Metodo getCount
     * Devuelve el numero total de filas que tiene el ListView
     */
    public int getCount() {
        
    	return datos.size();
    }

    
    
    /**
     * Metodo getItem
     */
    public Object getItem(int position) {
        
    	return position;
    }

    
    
    /**
     * Metodo getItemId
     */
    public long getItemId(int position) {
        
    	return position;
    }
    
    
    
    /**
     * Metodo getView
     * Dibuja las filas en el layout
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	View vista = convertView;
        
    	if(convertView==null) vista = inflater.inflate(R.layout.activity_list_item_amigos_fans, null);

        TextView text=(TextView)vista.findViewById(R.id.txtAmigosFansNombre);
        ImageView image=(ImageView)vista.findViewById(R.id.imgAmigosFans);
        Button btnAmigo = (Button)vista.findViewById(R.id.btnAmigosFansAgregar);
        
        text.setText(datos.get(position).getNombre());
        btnAmigo.setOnClickListener(new myListener(position));
        imageLoader.DisplayImage(datos.get(position).getFoto(), image);
        
        if(datos.get(position).getID().equals(Info.USUARIO_ID)) btnAmigo.setVisibility(View.GONE);
        if(Info.AMIGOS.contains(datos.get(position).getID())) btnAmigo.setText(R.string.btn_amigo_on);

        return vista;
    }
    
    
    
    /**
     * Clase privada myListener
     * @implements OnClickListener
     */
    private class myListener implements OnClickListener{
		
    	//Variables
		int position;
		
		/**
		 * Constructor myListener
		 * @param position posicion
		 */
		public myListener(int position){
			
			this.position = position;
		}

		
		/**
		 * Metodo onClick
		 */
		@Override
		public void onClick(View v) {
			
			id_amigo = datos.get(position).getID();
			btnAmigo = (Button)v;
		
			if(btnAmigo.getText().toString().equals(activity.getString(R.string.btn_amigo_on))) new deleteAmigo().execute(Info.USUARIO_ID, id_amigo);
			else new addAmigo().execute(Info.USUARIO_ID, id_amigo);
		}
	}
	
	
	
	/**
	 * Clase privada addAmigo
	 * Clase que permite añadir un nuevo amigo mediante una petición al servidor
	 * @extends AsyncTask
	 */
	private class addAmigo extends AsyncTask<String, Void, Integer> {	
		
		@Override
		protected Integer doInBackground(String... id) {
			
			try {
			
				List<String> resultado = Info.CONEXION.addAmigo(id[0], id[1]);
				if(resultado.get(0).equals("0")) return 0;
				else return 1;
			
			} catch (JSONException e) { 
				return -1;
			}
		}
		
		
		@Override
		protected void onPostExecute(Integer result) {
			actualizarBoton(result);
			new EnviarNotificaciones().execute(
					id_amigo, 
					Info.USUARIO_ID, 
					EnviarNotificaciones.generarNotificacion(Info.USUARIO_NOMBRE, null, Info.NOTIFICACION_AMISTAT), 
					Info.USUARIO_SLUG);
		}
	}
	
	
	
	/**
	 * Clase privada deleteAmigo
	 * Clase que permite eliminar a un amigo mediante una petición al servidor
	 * @extends AsyncTask
	 */
	public class deleteAmigo extends AsyncTask<String, Void, Integer> {	
		
		@Override
		protected Integer doInBackground(String... id) {
			
			try {
				
				List<String> resultado = Info.CONEXION.deleteAmigo(id[0], id[1]);
				if(resultado.get(0).equals("0")) return 0;
				else return 2;
	
			} catch (JSONException e) {
				return -1;
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
		
			actualizarBoton(result);
		}
	}
	
	
	
	/**
     * Metodo actualizarBoton
     * Actualiza el texto del boton de los Amigos/Fans 
     * @param codigoError
     * 1: Poner texto Amigo
     * 2: Poner texto Agregar
     */
	private void actualizarBoton(Integer codigoError){
		
		if(codigoError == 1){
			
			btnAmigo.setText(R.string.btn_amigo_on);
		}
		if(codigoError == 2){
			
			btnAmigo.setText(R.string.btn_amigo_off);
		}
	}
}
