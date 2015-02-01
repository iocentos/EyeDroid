package dk.itu.eyedroid.io.calibration;

import java.io.IOException;

import org.opencv.core.Point;

import dk.itu.eyedroid.io.Server;
import dk.itu.eyedroid.io.protocols.OutputNetProtocol;

public abstract class NETCalibrationController {
	protected  Server mServer;								// UDP server
	protected OutputNetProtocol mOutputProtocol;			// Protocol writter
	protected final CalibrationMapper mCalibrationMapper;	//Calibration Mapper instance
	protected CalibrationCallbacks mCalibrationCallbacks;

	public interface CalibrationCallbacks{
		public void  onCalibrationStarted();
		public void onCalibrationFinished();
		public void onCalibrationError();
	}
	
	/**
	 * Default constructor
	 * @param mapper Calibration mapper
	 */
	public NETCalibrationController(CalibrationMapper mapper){
		this.mCalibrationMapper = mapper;
	}
	
	public CalibrationMapper getCalibrationMapper(){
		return mCalibrationMapper;
	}

	/**
	 * Main calibration method
	 * @throws IOException
	 */
	public abstract void calibrate() throws IOException;
	
	protected abstract Point getSampleFromCore();
	
	public Server getServer() {
		return mServer;
	}

	public void setServer(Server mServer) {
		this.mServer = mServer;
	}

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
