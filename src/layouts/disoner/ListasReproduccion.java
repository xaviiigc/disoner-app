/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 16/05/13
 *  Fecha de finalización: 22/05/13
 *  Objetivo: Clase para el layout Listas de reproducción
 */

package layouts.disoner;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import constantes.disoner.Info;
import disoner.login.R;

public class ListasReproduccion extends FragmentActivity{
	
	
	//Variables
	private List<String> resultado = new ArrayList<String>();
	private static String idListaActual = "0";
	private List<Fragment> fragments = new ArrayList<Fragment>();
    SectionsPagerAdapter adaptador;    
    ViewPager pager;
    
    
    /**
     * Metodo onCreate
     */
    @SuppressLint("NewApi")
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_pestanas);
        
        pager = (ViewPager)findViewById(R.id.pager);
        
        ActionBar actBar = getActionBar();
		actBar.setDisplayHomeAsUpEnabled(true);
        		
		setTitle(" "+Info.USUARIO_NOMBRE);
		
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int posicion) {

				ListasCanciones l = (ListasCanciones) fragments.get(posicion);
				idListaActual =  l.getIdLista();
				
			}
			
			@Override
			public void onPageScrolled(int posicion, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int posicion) {}
		});
		
		new cargarListas().execute(Info.USUARIO_ID);
    }
    
    
    
    /**
     * Metodo getIdLista
     * Devuelve el id de la lista actual
     * @return String id Lista
     */
    public static String getIdLista(){
    	
    	return idListaActual;
    }
    
    
    
    /**
     * Metodo onCreateOptionsMenu
     */
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.menu_opciones_listas, menu);
	    return true;
	}
    
    
    /**
     * Metodo onOptionsItemSelected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()){
    	
    	case R.id.menu_lista_insertar:
    		
    		final EditText lista = new EditText(this);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setTitle("Crear lista de reproducción");
			builder.setMessage("Nombre de la lista: ");
			builder.setView(lista);
			builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					if(lista.getText().length() == 0){
						
						Toast.makeText(getApplicationContext(), getString(R.string.msg_lista_sin_nombre), Toast.LENGTH_LONG).show();
					}
					else{
						new insertarLista().execute(Info.USUARIO_ID, lista.getText().toString());
						Intent i = getIntent();
						startActivity(i);
						finish();
					}
				}
			});

			builder.setNegativeButton("Cancelar", null);
			AlertDialog alert = builder.create();
			alert.show();
			break;
			
    	case R.id.menu_lista_eliminar:
    		
    		new eliminarLista().execute(Info.USUARIO_ID, idListaActual);
    		Intent i2 = getIntent();
    		startActivity(i2);
    		finish();
    		
    		break;
    		
    		
    	case android.R.id.home:
    		finish();
    		break;
    	}
    	return true;
    }
    
    
    
    /**
     * Metodo finalizar
     * @param codigoError
     */
    public void finalizar(Integer codigoError){
    	
    	//Crea el adaptador, que nos retornara los fragmentos que se van a cargar
    	if(codigoError == 0) adaptador = new SectionsPagerAdapter(getSupportFragmentManager(), true);
    	else adaptador = new SectionsPagerAdapter(getSupportFragmentManager(), false);  
  
        //Carga el visualizador de paginas
    	ViewPager vp = (ViewPager) findViewById(R.id.pager);  
        vp.setAdapter(adaptador);
        
        ListasCanciones l = (ListasCanciones) fragments.get(0);
		idListaActual =  l.getIdLista();
    }
    
    
    
    /**
     * Clase cargarListas
     *
     */
    private class cargarListas extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				resultado = Info.CONEXION.getListasReproduccion(id[0]);
				if(resultado.get(0).equals("0")) return 0;
				else return 1;
				
			} catch (JSONException e) {
				return -1;
			}
		}
				
		@Override
		protected void onPostExecute(Integer result) {
		
			finalizar(result);
		}
	}
    

  
  
    /** 
     * Clase SectionsPagerAdapter
     * Retorna el fragmento correspondiente a cada una de las pestañas
     */  
    public class SectionsPagerAdapter extends FragmentPagerAdapter {  
  
    	boolean isVacio = true;
    	
        public SectionsPagerAdapter(FragmentManager fm, boolean isVacio) {  
            super(fm);
            this.isVacio = isVacio;
        }  
  
        
        @Override  
        public Fragment getItem(int position) {  
        	
        	
        	if(isVacio) fragments.add(new ListasCanciones("0"));
        	else fragments.add(new ListasCanciones(resultado.get(position*2)));

            return fragments.get(position);              
        }  
        
        
        @Override  
        public int getCount() {  
            
        	if(isVacio) return 1;
        	else return resultado.size()/2;
        } 
        
  
        @Override  
        public CharSequence getPageTitle(int position) {  
        	
        	if(isVacio) return getString(R.string.msg_lista_titulo); 
        	else{
        		
        		if(position == 0) return resultado.get(1);
            	if(position == 1) return resultado.get(3);
                return resultado.get(position*2+1);
        	}
        }    
    } 
    
    
    
    
    /**
     * Clase insertarLista
     *
     */
    private class insertarLista extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				resultado = Info.CONEXION.insertarListaReproduccion(id[0], id[1]);
				if(resultado.get(0).equals("0")) return 0;
				else return 1;
			
			} catch (JSONException e) {
				return -1;
			}
		}
				
		@Override
		protected void onPostExecute(Integer result) {
		
			super.onPostExecute(result);
			insertarLista(result);
		}
	}
	
    
    
    
    /**
     * Clase eliminarLista
     */
    private class eliminarLista extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				resultado = Info.CONEXION.eliminarListaReproduccion(id[0], id[1]);
				if(resultado.get(0).equals("0")) return 0;
				else return 1;

			} catch (JSONException e) {
				return -1;
			}
		}
				
		@Override
		protected void onPostExecute(Integer result) {
		
			super.onPostExecute(result);
			insertarLista(result);
		}
	}
    
    
	
	
	
	private void insertarLista(Integer result){
		
		switch(result){
		
			case 1:
				
				Toast.makeText(this, getString(R.string.msg_lista_anadida), Toast.LENGTH_SHORT).show();
		}
	}
}
