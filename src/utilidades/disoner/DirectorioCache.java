/*	Autor: Marcos Aguayo Mora
 * 	Fecha de inicio: 12/05/13
 *  Fecha de finalización: 22/05/13
 *  Objetivo: Clase para seleccionar el directorio de la memoria cache
 */

package utilidades.disoner;

import java.io.File;

import android.content.Context;

public class DirectorioCache {
    
	
	//Variable
    private File dirCache;
    
    
    /**
     * Constructor DirectorioCache
     * @param context contexto actual
     */
    public DirectorioCache(Context context){
        
    	//Busca el directorio LazyList, si no existe lo crea
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
        	
        	dirCache = new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
        }
        else{
       
        	dirCache=context.getCacheDir();
        }
        
        if(!dirCache.exists()){
        	
    		dirCache.mkdirs();
    	}
    }
    
    
    
    /**
     * Metodo getFile
     * Obtiene los ficheros del directorio cache
     * @param url - ruta del archivo
     * @return File - fichero solicitado
     */
    public File getFile(String url){
    	      
        String filename = String.valueOf(url.hashCode());
        File f = new File(dirCache, filename);
        return f;
    }
    
    
    
    /**
     * Metodo clear
     * Vacia la memoria cache
     */
    public void clear(){
       
    	File[] files=dirCache.listFiles();
        
    	if(files == null) return;
        for(File f:files) f.delete();
    }
}