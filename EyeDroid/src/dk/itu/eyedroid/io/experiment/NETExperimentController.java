package dk.itu.eyedroid.io.experiment;

import java.io.IOException;

import org.opencv.core.Point;

import dk.itu.eyedroid.io.Server;
import dk.itu.eyedroid.io.calibration.CalibrationMapper;
import dk.itu.eyedroid.io.protocols.OutputNetProtocol;

/**
 * Controller for experiment used to evaluate the accuracy of the system.
 */
public abstract class NETExperimentController {
	protected Server mServer; // UDP server
	protected OutputNetProtocol mOutputProtocol; // Protocol writter
	protected ExperimentCallbacks mExperimentCallbacks; // Experiment callbacks
	protected final CalibrationMapper mCalibrationMapper; // Calibration Mapper
															// instance

	public interface ExperimentCallbacks {
		public void onExperimentStarted();

		public void onExperimentFinished();

		public void onExperimentError();
	}

	/**
	 * Default constructor
	 * 
	 * @param mapper
	 *            Calibration mapper
	 */
	public NETExperimentController(CalibrationMapper mapper) {
		this.mCalibrationMapper = mapper;
	}

	// ***Getters/Setters*****

	public CalibrationMapper getCalibrationMapper() {
		return mCalibrationMapper;
	}

	public Server getServer() {
		return mServer;
	}

	public void setServer(Server mServer) {
		this.mServer = mServer;
	}

	public ExperimentCallbacks getExperimentCallbacks() {
		return mExperimentCallbacks;
	}

	public void setExperimentCallbacks(ExperimentCallbacks mExperimentCallbacks) {
		this.mExperimentCallbacks = mExperimentCallbacks;
	}

	public OutputNetProtocol getOutputProtocol() {
		return mOutputProtocol;
	}

	public void setOutputProtocol(OutputNetProtocol mOutputProtocol) {
		this.mOutputProtocol = mOutputProtocol;
	}

	/**
	 * Main experiment method
	 * 
	 * @throws IOException
	 */
	public abstract void experiment() throws IOException;

	/**
	 * Get current coordinates sample from the core
	 * 
	 * @return Sampled coordinates
	 */
	protected abstract Point getSampleFromCore();
}
