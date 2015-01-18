package dk.itu.eyedroid.io;


/**
 * Network client configuration. 
 * Allows to configure calibration type.
 * Define constants for messages used in the comunication protocol with client applications. 
 * Standard 12 byte int msgs.
 * 
 * Test client: https://github.com/dmardanbeigi/GlassGaze
 */

public class NetClientConfig {
	
	public static final String TAG = "EyeNet";
	
	public static final int NO_POINTS = 4;							//Number of points to calibrate
	public static final int WAIT_TO_SAMPLE = 500;					//Time to wait for the next sample during 1 point calibration
	public static final int NO_SAMPLES = 6;							//Number of point calibration samples
	public final static int MSG_SIZE = 12; 							//byteArray[12]
	
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
