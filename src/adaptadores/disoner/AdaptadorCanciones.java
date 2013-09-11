/*	Autor: Marcos Aguayo Mora
 * 	Fecha de inicio: 30/04/13
 *  Fecha de finalización: 02/05/13
 *  Objetivo: Adaptador para inflar en un ListView las canciones
 */


package adaptadores.disoner;


import java.util.List;

import constantes.disoner.Info;

import clases.disoner.Cancion;
import disoner.login.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import asynctask.disoner.DescargarImagenes;


public class AdaptadorCanciones extends BaseAdapter {
    
	//Variables
    private Activity activity;
    private List<Cancion> datos;
    private static LayoutInflater inflater = null;
    private DescargarImagenes imagenes; 
    
    
    /**
     * Constructor AdaptadorCanciones
     * @param a - Actividad actual
     * @param d - List<Cancion> lista de canciones
     */
    public AdaptadorCanciones(Activity a, List<Cancion> d) {
        
    	this.activity = a;
        this.datos=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imagenes = new DescargarImagenes(activity.getApplicationContext());
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
     * @param position posicion
     * Devuelve un objeto dada una posicion:
     */
    public Object getItem(int position) {
        
    	return datos.get(position);
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
        
    	if(convertView==null) vista = inflater.inflate(R.layout.activity_list_item_cancion, null);

        TextView text=(TextView)vista.findViewById(R.id.txtAutor);
        TextView txtTitulo = (TextView)vista.findViewById(R.id.txtTitulo);
        ImageView image=(ImageView)vista.findViewById(R.id.imgFotoCancion);
        
        text.setText(datos.get(position).getAutor());
        txtTitulo.setText(datos.get(position).getNombre());
        imagenes.DisplayImage(Info.URL_FOTO_CANCION + datos.get(position).getFoto(), image);
        return vista;
    }
}