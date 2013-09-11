/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 03/05/13
 *  Fecha de finalización: 06/05/13
 *  Objetivo: Clase para el layout Perfil
 */

package layouts.disoner;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import constantes.disoner.Info;
import disoner.login.R;


@SuppressLint("NewApi")
public class Perfil extends FragmentActivity{
	
	
	//Variables
	private static String id = null;
    private SectionsPagerAdapter mSectionsPagerAdapter;    
    private ViewPager mViewPager;  
  
    
    /**
     * Metodo onCreate
     */
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_pestanas);
        
        ActionBar actBar = getActionBar();
		actBar.setDisplayHomeAsUpEnabled(true);
    
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			id = extras.getString("id");
			setTitle(" "+extras.getString("nombre"));
		}
		else{
			id = Info.USUARIO_ID;
			setTitle(" "+Info.USUARIO_NOMBRE);
		}

        // Crea el adaptador este retorna un fragmento por cada una de las 3 secciones primarias de la app
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());  
  
        //Prepara el ViewPager con las secciones del adaptador
        mViewPager = (ViewPager) findViewById(R.id.pager);  
        mViewPager.setAdapter(mSectionsPagerAdapter);  
    }
    
    
    
    /**
     * Metodo getID
     * Devuelve el ID del fragmento que se esta mostrando
     * @return String id del fragmento
     */
    public static String getID(){
    	
    	return id; 
    }
    
  
    
    /**
     * Clase SectionsPagerAdapter
     * Adaptador de paginas 
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {  
  
    	
    	/**
    	 * Constructor
    	 * @param fm FragmentManager
    	 */
        public SectionsPagerAdapter(FragmentManager fm) {  
            
        	super(fm);  
        }  
        
        
        /**
         * Metodo getItem
         * Devuelve el fragmento segun la posición
         */
        @Override  
        public Fragment getItem(int position) {  
        	
        	
        	Fragment fragment = new Fragment();  
            switch (position) {  
            case 0:  
                return fragment = new PerfilCanciones();  
            case 1:  
                return fragment = new PerfilAmigos();  
            case 2:  
                return fragment = new PerfilFans();
            default:  
                break;  
            }  
            return fragment;  
        }  
        
        
        /**
         * Metodo getCount
         * Devuelve la cuenta total de fragmentos
         */
        @Override  
        public int getCount() {  
            return 3;  
        } 
        
        
        /**
         * Metodo getPageTitle
         * Devuelve el titulo del fragmento
         */
        @Override  
        public CharSequence getPageTitle(int position) {  

            switch (position) { 
	            case 0:  
	                return getString(R.string.pestana_canciones);  
	            case 1:  
	                return getString(R.string.pestana_amigos);  
	            case 2:  
	                return getString(R.string.pestana_fans);
	            }  
            return null;  
        }  
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if(item.getItemId() == android.R.id.home) finish();
    	
    	return true;
    }
}  
