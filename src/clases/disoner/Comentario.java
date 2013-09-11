/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 04/05/13
 *  Fecha de finalización: 07/05/13
 *  Objetivo: Clase para definir los comentarios
 */

package clases.disoner;

public class Comentario {
	
	//Variables
	private String nombre, contenido;
	
	
	/**
	 * Constructor Comentario
	 * @param nombre - Nombre del usuario
	 * @param contenido - Contenido del comentario
	 */
	public Comentario(String nombre, String contenido){
		
		this.nombre = nombre;
		this.contenido = contenido;
	}
	
	
	/**
	 * Metodo getNombre
	 * Devuelve el nombre del usuario
	 * @return String nombre usuario
	 */
	public String getNombre(){ return this.nombre; }
	
	
	/**
	 * Metodo getContenido
	 * Devuelve el contenido del comentario
	 * @return String contenido comentario
	 */
	public String getContenido(){ return this.contenido; }
}
