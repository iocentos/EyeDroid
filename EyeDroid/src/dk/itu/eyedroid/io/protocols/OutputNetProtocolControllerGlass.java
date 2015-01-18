package dk.itu.eyedroid.io.protocols;

import java.io.IOException;

import dk.itu.eyedroid.io.NetClientConfig;
import dk.itu.eyedroid.io.calibration.NETCalibrationController;

public class OutputNetProtocolControllerGlass extends OutputNetProtocolController{

	//TODO use calibration object	
	private Boolean isCalibrated;					// True when system is calibrated				

	/**
	 * Default constructor
	 * @param server Server
	 * @param calibration Calibration controller
	 */
	public  OutputNetProtocolControllerGlass(NETCalibrationController calibration){
		super(calibration);
	}

	/**
	 * Process messages from client application
	 *  @param message Message code
	 * @throws IOException 
	 */
	public void processMessage(int[] message) throws IOException{
		switch(message[0]){
		case NetClientConfig.TO_EYEDROID_CALIBRATE_DISPLAY_4:	//Start calibration
			isCalibrated = false;
			super.isStarted.set(false);
			super.isCalibrating.set(true);
			if(super.mCalibration.calibrate())
				isCalibrated = false;
			
			super.isCalibrating.set(false);
			break;
		case NetClientConfig.TO_EYEDROID_STREAM_GAZE_HMGT_START:
			if(isCalibrated){
				super.mUseHMGT = true;
				super.isStarted.set(true);
			}
			break;
		case NetClientConfig.TO_EYEDROID_STREAM_GAZE_RGT_START:
			if(isCalibrated){
				super.mUseHMGT = false;
				super.isStarted.set(true);
			}
			break;
		case NetClientConfig.TO_EYEDROID_STREAM_GAZE_HMGT_STOP:
		case NetClientConfig.TO_EYEDROID_STREAM_GAZE_RGT_STOP:
			super.isStarted.set(false);
			break;
		default:
			break;
		}
	}

}
