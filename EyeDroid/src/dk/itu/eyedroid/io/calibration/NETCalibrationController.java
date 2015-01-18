package dk.itu.eyedroid.io.calibration;

import java.io.IOException;

import org.opencv.core.Point;

import dk.itu.eyedroid.io.Server;
import dk.itu.eyedroid.io.protocols.OutputNetProtocol;

public abstract class NETCalibrationController {
	protected final Server mServer;							// UDP server
	protected OutputNetProtocol mOutputProtocol;		// Protocol writter
	protected final CalibrationMapper mCalibrationMapper;	//Calibration Mapper instance
	protected CalibrationCallbacks mCalibrationCallbacks;

	public interface CalibrationCallbacks{
		public void  onCalibrationStarted();
		public void onCalibrationFinished();
		public void onCalibrationError();
	}
	
	/**
	 * Default constructor
	 * @param server Network server
	 */
	public NETCalibrationController(Server server, CalibrationMapper mapper){
		this.mServer = server;
		this.mCalibrationMapper = mapper;
	}
	
	public CalibrationMapper getCalibrationMapper(){
		return mCalibrationMapper;
	}

	/**
	 * Main calibration method
	 * @param receivePacket Calibration message received by the server
	 * @throws IOException
	 */
	public abstract void calibrate() throws IOException;
	
	protected abstract Point getSampleFromCore();
	

	
	
	public CalibrationCallbacks getCalibrationCallbacks() {
		return mCalibrationCallbacks;
	}

	public void setCalibrationCallbacks(CalibrationCallbacks mCalibrationCallbacks) {
		this.mCalibrationCallbacks = mCalibrationCallbacks;
	}

	public OutputNetProtocol getOutputProtocol() {
		return mOutputProtocol;
	}

	public void setOutputProtocol(OutputNetProtocol mOutputProtocol) {
		this.mOutputProtocol = mOutputProtocol;
	}

	protected void setUpPointsToMapper(Point clientPoint , Point serverPoint){
		mCalibrationMapper.addDestinationPoint(clientPoint);
		mCalibrationMapper.addSourcePoint(serverPoint);
	}
}
