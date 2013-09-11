/*	Autor: Marcos Aguayo Mora
 * 	Fecha de inicio: 06/05/13
 *  Fecha de finalización: 07/05/13
 *  Objetivo: Clase para definir a los Amigo / Fans
 */

package clases.disoner;

public class AmigosFans {
	
	//Variables
	private String id, nombre, foto;
	
	
	/**
	 * Constructor AmigosFans
	 * @param id - Id del usuario
	 * @param nombre - Nombre del usuario
	 * @param foto - Ruta de la foto de perfil
	 */
	public AmigosFans(String id, String nombre, String foto){
		
		this.id = id;
		this.nombre = nombre;
		this.foto = foto;
	}
	
	
	/**
	 * Metodo getID
	 * Devuelve el id del usuario
	 * @return String id del usuario
	 */
	public String getID(){ return this.id; }
	
	
	/**
	 * Metodo getNombre
	 * Devuelve el nombre del usuario
	 * @return String nombre del usuario
	 */
	public String getNombre(){ return this.nombre; }
	
	
	/**
	 * Metodo getFoto
	 * Devuelve la url de la foto del usuario
	 * @return String url de la foto del usuario
	 */
	public String getFoto(){ return this.foto; }
}
