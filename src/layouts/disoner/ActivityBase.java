/*	Autor: Marcos Aguayo Mora
 * 	Fecha de inicio: 01/05/13
 *  Fecha de finalización: 01/05/13
 *  Objetivo: Clase base para las actividades
 */

package layouts.disoner;


import constantes.disoner.Info;
import disoner.login.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityBase extends ListActivity {
	
	//Variables
	private Intent i;
	
	
	/**
	 * Metodo onCreate
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		ActionBar actBar = getActionBar();
		actBar.setDisplayHomeAsUpEnabled(true);
		setTitle(" "+Info.USUARIO_NOMBRE);
	}
	
	
	
	/**
	 * Metodo onCreateOptionsMenu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.menu_opciones, menu);
	    return true;
	}
	
	
	
	/**
	 * Metodo onOptionsItemSelected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch(item.getItemId()){
		
			case R.id.menu_salir:
				
									i = new Intent(this, Login.class);
									startActivity(i);
									finish();
									break;
			
			case R.id.menu_perfil: 
				
									if(Info.PANTALLA_ACTUAL != Info.PERFIL){
										i = new Intent(this, Perfil.class);
										startActivity(i);
										if(Info.PANTALLA_ACTUAL != Info.INICIO){
											finish();
										}
									}
								   break;
								   
			case R.id.menu_listas:
									
									i = new Intent(this, ListasReproduccion.class);
									startActivity(i);
									break;
				
			case android.R.id.home:					   
			case R.id.menu_inicio: 
				
									if(Info.PANTALLA_ACTUAL != Info.INICIO){
										finish();
									}
									Info.PANTALLA_ACTUAL = Info.INICIO;
									break;
		}
		return true;
	}
}
