/*	Autor: Xavier Güell Castella
 * 	Fecha de inicio: 10/05/13
 *  Fecha de finalización: 23/05/13
 *  Objetivo: Clase para multiples utilidades
 */

package utilidades.disoner;

import java.io.InputStream;
import java.io.OutputStream;

public class Utilidades {

	
	
	/* Convertir milisegundos a h:m:s */
	public static String milisecToTime(long milliseconds) {
		
		String finalTimerString = "";
		String secondsString = "";
		
		// Convertir la duración total
		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		
		if (hours > 0) {
			
			finalTimerString = hours + ":";
		}
		
		if (seconds < 10) {
			
			secondsString = "0" + seconds;
		}
		else {
			
			secondsString = "" + seconds;
		}
		
		finalTimerString = finalTimerString + minutes + ":" + secondsString;
		// devolver el tiempo en string
		return finalTimerString;
	}

	
	
	/* obtener el porcentaje de duración */
	public static int getProgressPercentage(long currentDuration, long totalDuration) {
		
		Double percentage = (double) 0;
		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);
		percentage = (((double) currentSeconds) / totalSeconds) * 100;
		return percentage.intValue();
	}

	
	
	/* cambiar la duración */
	public static int progressToTimer(int progress, int totalDuration) {
		
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double) progress) / 100) * totalDuration);
		return currentDuration * 1000;
	}
	
	
	
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
