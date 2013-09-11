/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 10/05/13
 *  Fecha de finalización: 29/05/13
 *  Objetivo: Clase base para las actividades
 */


package layouts.disoner;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import utilidades.disoner.Utilidades;
import adaptadores.disoner.AdaptadorComentarios;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import asynctask.disoner.EnviarNotificaciones;
import clases.disoner.Comentario;
import constantes.disoner.Info;
import disoner.login.R;



public class Reproductor extends ActivityBase implements OnClickListener, OnSeekBarChangeListener {

	//Variables
	private String id, id_autor;
	private int likes = 0;
	private List<String> resultado = new ArrayList<String>();
	private List<String> resultado2 = new ArrayList<String>();
	private List<Comentario> comentarios = new ArrayList<Comentario>();
	private Button btnPlay, btnMeGusta;
	private TextView txtNombre, txtTiempoTotal, txtTiempo, txtComentario;
	private boolean isLiked = false;
	private SeekBar barraProgreso;
	private Handler miHandler = new Handler();

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reproductor);

		Info.EXTRAS = getIntent().getExtras();

		// Caputarmos las vistas que necesitamos
		txtNombre = (TextView) findViewById(R.id.lblRepNombre);
		txtComentario = (EditText) findViewById(R.id.txtRepComentario);
		txtTiempo = (TextView) findViewById(R.id.lblRepTiempo);
		txtTiempoTotal = (TextView) findViewById(R.id.lblRepTiempoTotal);
		btnPlay = (Button) findViewById(R.id.btnRepPlay);
		Button btnComentar = (Button) findViewById(R.id.btnRepComentar);
		btnMeGusta = (Button) findViewById(R.id.btnRepLike);
		Button btnLista = (Button) findViewById(R.id.btnRepLista);
		barraProgreso = (SeekBar) findViewById(R.id.seekBar);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		btnPlay.setOnClickListener(this);
		btnComentar.setOnClickListener(this);
		btnMeGusta.setOnClickListener(this);
		btnLista.setOnClickListener(this);
		barraProgreso.setOnSeekBarChangeListener(this);
		

		id = Info.EXTRAS.getString("id");
		id_autor = Info.EXTRAS.getString("id_autor");
		txtNombre.setText(Info.EXTRAS.getString("nombreCancion"));
	
		//Si el ID CANCION coincide con el id cancion layout Cargamos la barra + boton pausa 
		if(Info.ID_CANCION.equals(id)) actualizaBarraProgreso();
		if(Info.MP.isPlaying()) btnPlay.setBackgroundResource(R.drawable.btn_pause_anim);
		
		barraProgreso.setProgress(0);
		barraProgreso.setMax(100);

		new getComentario().execute(id);
		new getLikes().execute(id);
	}

	

	public void actualizaBarraProgreso() {
		
		miHandler.postDelayed(mUpdateTimeTask, 100);
	}

	
	
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {

			long totalDuration = Info.MP.getDuration();
			long currentDuration = Info.MP.getCurrentPosition();
			// Mostrar el tiempo en las lables
			txtTiempoTotal.setText(""
					+ Utilidades.milisecToTime(totalDuration));
			txtTiempo.setText(""
					+ Utilidades.milisecToTime(currentDuration));
			// Actualizar la barra de progreso
			int progres = (int) (Utilidades.getProgressPercentage(currentDuration, totalDuration));
			barraProgreso.setProgress(progres);
			// Timer para llamar a la función
			miHandler.postDelayed(this, 100);
		}
	};

	
	
	public void onStartTrackingTouch(SeekBar seekBar) {
		miHandler.removeCallbacks(mUpdateTimeTask);
	}
	
	
	public void onStopTrackingTouch(SeekBar seekBar) {
		miHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = Info.MP.getDuration();
		int currentPosition = Utilidades.progressToTimer(seekBar.getProgress(),
				totalDuration);
		// adelantar o retrasar hasta unos segundos
		Info.MP.seekTo(currentPosition);
		// actualizar la barra de progreso
		actualizaBarraProgreso();
	}

	
	
	@Override
	public void onClick(View vista) {

		switch (vista.getId()) {

		case R.id.btnRepPlay:
			
			play();
			//else stop();
			break;

		case R.id.btnRepComentar:

			String comentario = txtComentario.getText().toString();

			if (comentario.equals("")) {

				Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_comentario_vacio), Toast.LENGTH_SHORT).show();
			}
			else{
				new insertarComentarios().execute(id, Info.USUARIO_ID, comentario);
			}
			break;

		case R.id.btnRepLike:

			if (!isLiked)
				new insertarLike().execute(Info.USUARIO_ID, id);
			else
				new eliminarLike().execute(Info.USUARIO_ID, id);
			break;

		case R.id.btnRepLista:

			new cargarListas().execute(Info.USUARIO_ID);
		}
	}

	

	private void play() {

		//Si no suena nada Guardamos los extras y le damos al play!
		if(!Info.MP.isPlaying()){

			Info.MP = MediaPlayer.create(this, Uri.parse(Info.URL_CANCION + Info.EXTRAS.getString("url")));
			Info.ID_CANCION = id;			
			Info.MP.start();
			btnPlay.setBackgroundResource(R.drawable.btn_pause_anim);
			
			// Actualizar la barra de progreso
			actualizaBarraProgreso();
		}
		else{
			stop();
			btnPlay.setBackgroundResource(R.drawable.btn_play_anim);
		}
	}
	
	
	
	public void stop() {

		btnPlay.setBackgroundResource(R.drawable.btn_play_anim);
		Info.MP.pause();
		Info.MP.seekTo(0);
		NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNotificationManager.cancel(100);
	}
	
	
	//-------------------------------------------------------------------------------------------------------------
	//								METODOS RESULTADO DE LAS TAREAS ASINCRONAS
	//-------------------------------------------------------------------------------------------------------------

	
	
	/**
	 * 
	 * @param codigoError
	 */
	public void cargarComentarios(Integer codigoError) {

		for (int i = 0; i < resultado.size(); i += 2) {
			comentarios.add(new Comentario(resultado.get(i), resultado.get(i+1)));
		}

		ArrayAdapter<Comentario> adaptador = new AdaptadorComentarios(this, comentarios);
		setListAdapter(adaptador);	
	}
	
	
	
	/**
	 * 
	 * @param result
	 */
	private void actualizarComentarios(Integer codigoError) {

		if(codigoError == 1){
			
			comentarios.add(new Comentario(Info.USUARIO_NOMBRE, txtComentario.getText().toString()));
			txtComentario.setText("");
			getListView().invalidateViews();
		}
	}

	
	
	/**
	 * 
	 * @param result
	 */
	public void cargarLikes(Integer codigoError) {

		if(codigoError == 1) {
			if (resultado2.contains("" + Info.USUARIO_ID)) isLiked = true;
			likes = resultado2.size();
			btnMeGusta.setText("" + likes);
		}
	}
	
	
	
	/**
	 * 
	 * @param result
	 */
	private void agregarLike(Integer codigoError) {

		if (codigoError == 1) {

			likes += 1;
			btnMeGusta.setText("" + likes);
			isLiked = true;
		}
	}

	
	
	/**
	 * 
	 * @param result
	 */
	private void eliminarLike(Integer codigoError) {

		if (codigoError == 1) {

			likes -= 1;
			btnMeGusta.setText("" + (likes));
			isLiked = false;
		}
	}
	
	
	
	/**
	 * 
	 */
	private void mostrarListasReproduccion(Integer codigoError){
		
		if(codigoError == 0){
			
			Toast.makeText(getApplicationContext(), "No tienes listas de reproducción", Toast.LENGTH_SHORT).show();
		}
		
		if(codigoError == 1){
			
			final String[] opciones = new String[resultado.size() / 2];
			final String[] ids = new String[resultado.size() / 2];

			for (int i = 0, j = 0; i < resultado.size(); i += 2, j++) {

				opciones[j] = resultado.get(i + 1);
				ids[j] = resultado.get(i);

			}

		
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Elige una Lista de Reproducción");
			builder.setItems(opciones, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {

					new insertarCancionLista().execute(ids[item], id);

				}
			});

			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	
	
	/**
	 * 
	 * @param codigo
	 */
	private void informarResultado(Integer codigoError) {

		switch(codigoError){

		case 0:
			Toast.makeText(getApplicationContext(),"No se ha podido añadir la cancion a la lista de reproducción",Toast.LENGTH_SHORT).show();
			break;

		case 1:
			Toast.makeText(getApplicationContext(),"Cancion añadida con exito", Toast.LENGTH_SHORT).show();
			break;

		case -1:

			break;
		}
	}

	
	
	//-------------------------------------------------------------------------------------------------------------
	//											CLASES PRIVADAS
	//-------------------------------------------------------------------------------------------------------------
	
	
	
	/**
	 * 
	 * @author Xavi
	 * 
	 */
	private class getComentario extends AsyncTask<String, Void, Integer> {

		
		@Override
		protected Integer doInBackground(String... parametros) {
			try {
				
				resultado = Info.CONEXION.getComentarios(parametros[0]);
				if (resultado.get(0).equals("0")) return 0;
				else return 1;

			} catch (JSONException e) {
				return -1;
			}
		}

		
		@Override
		protected void onPostExecute(Integer result) {
			cargarComentarios(result);
		}
	}

	
	
	/**
	 * 
	 * @author Xavi
	 * 
	 */
	private class getLikes extends AsyncTask<String, Void, Integer> {

		
		@Override
		protected Integer doInBackground(String... id) {
			try {
				
				resultado2 = Info.CONEXION.getLikes(id[0]);
				if (resultado2.get(0).equals("0")) return 0;
				else return 1;
				
			} catch (JSONException e) {
				return -1;
			}
		}

		
		@Override
		protected void onPostExecute(Integer result) {
			cargarLikes(result);
		}
	}

	
	
	/**
	 * 
	 * @author Xavi
	 * 
	 */
	private class insertarComentarios extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... parametros) {
			try {
				resultado = Info.CONEXION.insertarComentario(parametros[0],
						parametros[1], parametros[2]);
				if (resultado.get(0).equals("0"))
					return 0;
				else
					return 1;

			} catch (JSONException e) {
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {

			String mensaje = EnviarNotificaciones.generarNotificacion(
					Info.USUARIO_NOMBRE,
					txtNombre.getText().toString() + " : ",
					Info.NOTIFICACION_COMENTARIO);

			new EnviarNotificaciones().execute(id_autor, Info.USUARIO_ID,
					mensaje, "http://disoner.com/cancion/" + id);
			actualizarComentarios(result);
		}
	}

	
	
	/**
	 * 
	 * @author Xavi
	 * 
	 */
	private class cargarListas extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				resultado = Info.CONEXION.getListasReproduccion(id[0]);
				if (resultado.get(0).equals("0"))
					return 0;
				else
					return 1;

			} catch (JSONException e) {
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);

			mostrarListasReproduccion(result);
		}
	}

	
	
	/**
	 * 
	 * @author Xavi
	 * 
	 */
	private class insertarCancionLista extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... parametros) {
			try {
				resultado = Info.CONEXION.insertarCancionLista(parametros[0],
						parametros[1]);
				if (resultado.get(0).equals("0"))
					return 0;
				else
					return 1;

			} catch (JSONException e) {
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
			informarResultado(result);
		}
	}

	
	
	/**
	 * 
	 * @author Xavi
	 * 
	 */
	private class insertarLike extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				resultado = Info.CONEXION.insertarLike(id[0], id[1]);
				if (resultado.get(0).equals("0"))
					return 0;
				else
					return 1;

			} catch (JSONException e) {
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {

			String mensaje = EnviarNotificaciones.generarNotificacion(
					Info.USUARIO_NOMBRE, txtNombre.getText().toString(),
					Info.NOTIFICACION_LIKE);

			super.onPostExecute(result);
			new EnviarNotificaciones().execute(id_autor, Info.USUARIO_ID,
					mensaje, "http://disoner.com/cancion/" + id);
			agregarLike(result);
		}
	}

	
	
	/**
	 * 
	 * @author Xavi
	 * 
	 */
	private class eliminarLike extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... id) {
			try {
				resultado = Info.CONEXION.eliminarLike(id[0], id[1]);
				if (resultado.get(0).equals("0"))
					return 0;
				else
					return 1;

			} catch (JSONException e) {
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
			eliminarLike(result);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == android.R.id.home) finish();
		return true;
	}
}
