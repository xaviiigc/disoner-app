/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 02/05/13
 *  Fecha de finalización: 09/05/13
 *  Objetivo: Clase que permite descargar imagenes desde una URL o cargarlas del Directorio "cache"
 */

package asynctask.disoner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utilidades.disoner.DirectorioCache;
import utilidades.disoner.MemoriaCache;
import utilidades.disoner.Utilidades;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import disoner.login.R;

public class DescargarImagenes {
    
	
	//Variables
    private MemoriaCache memoryCache = new MemoriaCache();
    private DirectorioCache cache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    private Handler handler=new Handler();
    private final int stub_id = R.drawable.stub;
    
    
    /**
     * Constructor DescargarImagenes
     * @param context Contexto
     */
    public DescargarImagenes(Context context){
        
    	cache = new DirectorioCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }
    
    
    
    /**
     * Metodo DisplayImage
     * Esta imagen carga la imagen en el ImageView si esta en cache, sino llama al metodo para descargarla
     * @param url direccion de la imagen
     * @param imageView Vista donde ire a parar la imagen
     */
    public void DisplayImage(String url, ImageView imageView){
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else{
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }
    
    
    
    /**
     * Metodo queuePhoto
     * Esta metodo activa el Thread que descargara la imagen en segundo plano
     * @param url dirección de la imagen
     * @param imageView Vista donde ira a parar la imagen
     */
    private void queuePhoto(String url, ImageView imageView){
        
    	PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    
    
    
    /**
     * Metodo getBitmap
     * Metodo que coge las imagenes desde la SD o las descarga de internet
     * @param url direccion de la imagen
     * @return Bitmap imagen
     */
    private Bitmap getBitmap(String url){
        File f = cache.getFile(url);
        
        //Desde la SD
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
        
        //Desde la web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utilidades.CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex){
           Log.d("ERROR", "Mensaje: "+ex.getMessage());
           if(ex instanceof OutOfMemoryError)
               memoryCache.clear();
           return null;
        }
    }

    
    
    /**
     * Metodo decodeFile
     * Este metodo coge la imagen y la decodifica y recorta para reducir el consumo de memoria de la cache
     * @param f archivo de la imagen
     * @return Bitmap
     */
    private Bitmap decodeFile(File f){
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();
            
            final int REQUIRED_SIZE=130;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        }
        catch (FileNotFoundException e) {} 
        catch (IOException e) {}
        return null;
    }

    
    
    /**
     * Clase PhotoToLoad
     * Clase que define la estructura de las fotos (Url y ImageView)
     */
    private class PhotoToLoad{
    	
        public String url;
        public ImageView imageView;
        
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    
    
    /**
     * Clase PhotosLoader
     * Clase que contiene el thread que llama a descargar las imagenes en segundo plano
     * @implements Runnable
     */
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
            try{
                if(imageViewReused(photoToLoad))
                    return;
                Bitmap bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    
    
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    
    
    public void clearCache() {
        memoryCache.clear();
        cache.clear();
    }
}
