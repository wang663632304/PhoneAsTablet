package info.bits.phoneastablet.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author LiTTle
 * An utility class that runs the superuser's commands.
 */
public final class SuCommands {
    
    /**
     * Modifies the current resolution according to user's preferences.
     * @param width width size
     * @param height height size
     * @throws IOException if and I/O error occured
     */
    public static void changeResolution(String width, String height) throws IOException{
	Process suProcess;
	suProcess = Runtime.getRuntime().exec("su");
	DataOutputStream dos = new DataOutputStream(suProcess.getOutputStream());
	dos.writeBytes("wm size "+width+"x"+height);
	dos.flush();
	dos.close();
    }
    
    /**
     * Returns the available resolution sizes. 
     * These sizes are stored to the device.
     * Usually contains "Physical size" and "Overriden size".
     * @return resolution sizes saved on device
     * @throws IOException
     */
    public static String[] getAvailableResolutions() throws IOException{
	Process suProcess;
	suProcess = Runtime.getRuntime().exec("su");
	DataInputStream dis = new DataInputStream(suProcess.getInputStream());
	DataOutputStream dos = new DataOutputStream(suProcess.getOutputStream());
	dos.writeBytes("wm size");
	dos.flush();
	dos.close();
	StringBuilder sb = new StringBuilder();
	int read = 0;
	while(read != -1){
	    read = dis.read();
	    if(read != -1)
		sb.append((char)read);
	}
	dis.close();
	return split(sb.toString());
    }
    
    /* Split a text into particular parts. The separator used is "\n".
     * @param input the string value to split
     * @return array of string values derived from the parameter passed to the method
     */
    private static String[] split(String input){
	String[] pixels;
	String[] resolutions = input.split("\n");
	
	if(existsOverridenResolution(resolutions)){
	    pixels = new String[4];
		String currentResolution = resolutions[1].substring(
			resolutions[1].indexOf(": "), resolutions[1].length());
		pixels[2] = currentResolution.substring(2, currentResolution.indexOf("x"));
		pixels[3] = currentResolution.substring(
			currentResolution.indexOf("x") + 1, currentResolution.length());
	}
	else
	    pixels = new String[2];
	
	String physicalResolution = resolutions[0].substring(
		resolutions[0].indexOf(": "), resolutions[0].length());
	pixels[0] = physicalResolution.substring(2, physicalResolution.indexOf("x"));
	pixels[1] = physicalResolution.substring(
		physicalResolution.indexOf("x") + 1, physicalResolution.length());
	
	return pixels;
    }
    
    /* Checks if any "Overriden size" exists.
     * @param resolutions available resolution stored in device
     * @return
     */
    private static boolean existsOverridenResolution(String[] resolutions){
	if (resolutions.length > 1)
	    return resolutions[1].contains("size");
	else
	    return false;
    }
    
    /**
     * Resets the resolution size to default according to devices OEM resolution size.
     * @throws IOException when I/O error occurred
     */
    public static void fallbackToDefaultResolution() throws IOException{
	Process suProcess;
	suProcess = Runtime.getRuntime().exec("su");
	DataOutputStream dos = new DataOutputStream(suProcess.getOutputStream());
	dos.writeBytes("wm size reset");
	dos.flush();
	dos.close();
    }
}
