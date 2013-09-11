/*	Autor: Marcos Aguayo Mora
 * 	Fecha de inicio: 16/05/13
 *  Fecha de finalización: 16/05/13
 *  Objetivo: Adaptador para inflar en un ListView los comentarios
 */

package adaptadores.disoner;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import clases.disoner.Comentario;
import disoner.login.R;


public class AdaptadorComentarios extends ArrayAdapter<Comentario> {
	
	//Variables
	Activity contexto;
	private List<Comentario> datos;

	
	/**
	 * Constructor AdaptadorComentarios
	 * @param contexto - Actividad actual
	 * @param datos - List<Comentarios> lista de los comentarios
	 */
	public AdaptadorComentarios(Activity contexto, List<Comentario> datos) {
		
		super(contexto, R.layout.activity_list_item_comentario, datos);
		this.datos = datos;
		this.contexto = contexto;
	}

	
	
	/**
	 * Metodo getView 
	 * Dibuja las filas en el layout
	 */
	public View getView(int position, View convertView, ViewGroup parent){
		
		View item = convertView;
		VistaTag vistaTag;
		LayoutInflater inflater = contexto.getLayoutInflater();
		
		item = inflater.inflate( R.layout.activity_list_item_comentario, null);
		vistaTag = new VistaTag();
		vistaTag.usuario = (TextView)item.findViewById(R.id.txtComentarioNombre);
		vistaTag.comentario = (TextView)item.findViewById(R.id.txtComentarioContenido);
		
		vistaTag.usuario.setText(datos.get(position).getNombre());
		vistaTag.comentario.setText(datos.get(position).getContenido());
		
		item.setTag(vistaTag);

		return(item);	
	} 
	
	
	
	/**
	 * Clase privada VistaTag
	 * Clase con las vista que contiene cada fila que añadimos al listview
	 */
	private class VistaTag {

		TextView usuario;
		TextView comentario;
	}
}






