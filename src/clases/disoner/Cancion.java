/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 02/05/13
 *  Fecha de finalización: 02/05/13
 *  Objetivo: Clase para definir las Canciones
 */

package clases.disoner;


public class Cancion {
	
	//Variables
 	private String id, nombre, autor_id, autor, ruta, foto;
	
	
	/**
	 * Constructor Cancion
	 * @param id - Id de la canción
	 * @param nombre - Nombre de la canción
	 * @param autor_id - Id del autor
	 * @param autor - Nombre del autor
	 * @param ruta - Ruta de la canción en el servidor
	 * @param foto - Ruta de la foto de la canción
	 */
	public Cancion(String id, String nombre, String autor_id, String autor, String ruta, String foto){
		
		this.id = id;
		this.nombre = nombre;
		this.autor_id = autor_id;
		this.autor = autor;
		this.ruta = ruta;
		this.foto = foto;
	}
	
	
	/**
	 * Metodo getId
	 * Devuelve el ID de la canción
	 * @return String id cancion
	 */
	public String getId(){ return this.id; }
	
	
	/**
	 * Metodo getNombre
	 * Devuelve el nombre de la canción
	 * @return String nombre canción
	 */
	public String getNombre(){ return this.nombre; }
	
	
	/**
	 * Metodo getAutorID
	 * Devuelve el id del autor
	 * @return String ID Autor
	 */
	public String getAutorID(){ return this.autor_id; }
	
	
	/**
	 * Metodo getAutor
	 * Devuelve el nombre del autor
	 * @return String nombre del autor
	 */
	public String getAutor(){ return this.autor; }
	
	
	/**
	 * Metodo getCancion
	 * Devuelve la ruta de la canción en el servidor
	 * @return String ruta canción
	 */
	public String getCancion(){ return this.ruta; }
	
	
	/**
	 * Metodo getFoto
	 * Devuelve la ruta de la foto de la canción
	 * @return String ruta foto
	 */
	public String getFoto(){ return this.foto; }
}
