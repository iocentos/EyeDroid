package dk.itu.eyedroid;

/**
 * Network client configuration. 
 * Allows to configure calibration type.
 * Define constants for messages used in the comunication protocol with client applications. 
 * Standard 12 byte int msgs.
 * 
 * Test client: https://github.com/dmardanbeigi/GlassGaze
 */

public class NetClientConfig {
	
	public static final boolean IS_CALIBRATION_ENABLED = true;
	public static final boolean USE_HMGT = true;					//RGT if false
	public final static int MSG_SIZE = 12; 								// byteArray[12]
	
	//Client app to EyeDroid
	
	public static final int TO_EYEDROID_CALIBRATE_DISPLAY_4 = 1005;
	public static final int TO_EYEDROID_READY = 1000;
	public static final int TO_EYEDROID_STREAM_GAZE_RGT_START = 1002;
	public static final int TO_EYEDROID_STREAM_GAZE_RGT_STOP = 1004;
	public static final int TO_EYEDROID_STREAM_GAZE_HMGT_START = 1001;
	public static final int TO_EYEDROID_STREAM_GAZE_HMGT_STOP = 1003;
	public static final int TO_EYEDROID_CALIBRATE_DISPLAY_Correct = 1006;
	
	//EyeDroid to client
	
	public static final int TO_CLIENT_CALIBRATE_DISPLAY = 2004;
	public static final int TO_CLIENT_GAZE_RGT = 2002;
	public static final int TO_CLIENT_GAZE_HMGT = 2003;
	
}
