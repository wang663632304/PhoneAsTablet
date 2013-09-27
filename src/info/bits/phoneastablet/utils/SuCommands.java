/**
 * 
 */
package info.bits.phoneastablet.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author little
 *
 */
public final class SuCommands {

    /**
     * 
     */
    private SuCommands() {
	// TODO Auto-generated constructor stub
    }
    
    public static void changeResolution(String width, String height) throws IOException{
	Process suProcess;
	suProcess = Runtime.getRuntime().exec("su");
	DataOutputStream dos = new DataOutputStream(suProcess.getOutputStream());
	dos.writeBytes("wm size "+width+"x"+height);
	dos.flush();
	dos.close();
    }
    
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
    
    private static boolean existsOverridenResolution(String[] resolutions){
	return resolutions[1].contains("size");
    }
    
    public static void fallbackToDefaultResolution() throws IOException{
	Process suProcess;
	suProcess = Runtime.getRuntime().exec("su");
	DataOutputStream dos = new DataOutputStream(suProcess.getOutputStream());
	dos.writeBytes("wm size reset");
	dos.flush();
	dos.close();
    }
}
