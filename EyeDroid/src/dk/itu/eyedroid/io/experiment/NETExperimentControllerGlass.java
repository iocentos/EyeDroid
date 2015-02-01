package dk.itu.eyedroid.io.experiment;

import java.io.IOException;

import org.opencv.core.Point;

import android.util.Log;
import dk.itu.eyedroid.io.GlassConfig;
import dk.itu.eyedroid.io.calibration.CalibrationMapper;

public class NETExperimentControllerGlass extends NETExperimentController {

	/**
	 * Default constructor
	 * Calibration mapper
	 * @param mapper
	 */
	public NETExperimentControllerGlass(CalibrationMapper mapper) {
		super(mapper);
	}

	/**
	 * Experiment process
	 * @throws IOException
	 */
	public void experiment() throws IOException {

		Point[] sourcePoints = GlassConfig.EXPERIMENT_POINTS;
		Point[] sampledPoints = new Point[GlassConfig.EXPERIMENT_POINTS.length];
		Point[] sampledMappedPoints = new Point[GlassConfig.EXPERIMENT_POINTS.length];

		int[] message;
		boolean error = false;

		if (super.mExperimentCallbacks != null)
			mExperimentCallbacks.onExperimentStarted();

		try {
			//Start experiment
			super.mServer.send(GlassConfig.TO_CLIENT_EXPERIMENT, -1, -1);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//Send points to glass and sample
			int counter = 0;
			while (counter < GlassConfig.EXPERIMENT_POINTS.length) {

				message = super.mServer.read();
				while (message[0] == -1) {
					message = super.mServer.read();
				}
				if (GlassConfig.TO_EYEDROID_READY != message[0]) {
					Log.i(GlassConfig.TAG,
							"Mesasge is not TO_EYEDROID_READY " + message[0]);
					continue;
				}

				//Get the experiment point and send it to the client
				Point clientPoint = sourcePoints[counter];
				super.mServer.send(GlassConfig.TO_CLIENT_EXPERIMENT, (int) clientPoint.x, (int) clientPoint.y);

				//Sample point from core
				final Point sampledPoint = getSampleFromCore();
				sampledPoints[counter] = sampledPoint;

				//Map sampled point
				final int[] mappedPoint = super.getCalibrationMapper().map((float)sampledPoint.x, (float)sampledPoint.y);
				sampledMappedPoints[counter] = new Point(mappedPoint[0],mappedPoint[1]);

				counter++;
			}

			if (!error) {
				super.mServer.send(GlassConfig.TO_CLIENT_EXPERIMENT_STOP, -1, -1);

				saveExperimentFile(sourcePoints, sampledPoints, sampledMappedPoints);

				if (super.mExperimentCallbacks != null)
					super.mExperimentCallbacks.onExperimentFinished();
			} else {
				if (super.mExperimentCallbacks != null)
					super.mExperimentCallbacks.onExperimentError();
			}
		} catch (IOException e) {
			if (super.mExperimentCallbacks != null)
				super.mExperimentCallbacks.onExperimentError();
		}

	}

	@Override
	protected Point getSampleFromCore() {

		int[] xy;
		int sumX = 0, sumY = 0;

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		for (int j = 0; j < GlassConfig.NO_SAMPLES; j++) {
			//Thread.currentThread();
			try {
				Thread.sleep(GlassConfig.WAIT_TO_SAMPLE);
				if (mOutputProtocol != null) {
					xy = this.mOutputProtocol.getXY();
					sumX += xy[0];
					sumY += xy[1];
				}
			} catch (InterruptedException e) {
			}
		}
		return new Point(sumX / GlassConfig.NO_SAMPLES, sumY / GlassConfig.NO_SAMPLES);
	}
	
	private void saveExperimentFile(Point[] sourcePoints, Point[] sampledPoints, Point[] sampledMappedPoints){
		//TODO implement
	}
}
