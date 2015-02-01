package dk.itu.eyedroid.io.calibration;

import java.io.IOException;

import org.opencv.core.Point;

import android.util.Log;
import dk.itu.eyedroid.io.GlassConfig;

public class NETCalibrationControllerGlass extends NETCalibrationController {

	/**
	 * Default constructor
	 * 
	 * @param server
	 * @param mapper
	 *            Calibration mapper
	 */
	public NETCalibrationControllerGlass(CalibrationMapper mapper) {
		super(mapper);
	}

	/**
	 * Calibration process
	 * @throws IOException
	 */

	public void calibrate() throws IOException {

		int[] message;
		boolean error = false;

		if (mCalibrationCallbacks != null)
			mCalibrationCallbacks.onCalibrationStarted();

		this.mCalibrationMapper.clean();

		try {
			super.mServer.send(GlassConfig.TO_CLIENT_CALIBRATE_DISPLAY, -1, -1);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int counter = 0;
			while (counter < GlassConfig.NO_POINTS) {

				message = super.mServer.read();
				while (message[0] == -1) {
					message = super.mServer.read();
				}
				if (GlassConfig.TO_EYEDROID_READY != message[0]) {
					Log.i(GlassConfig.TAG,
							"Mesasge is not TO_EYEDROID_READY " + message[0]);
					continue;
				}
				// get the calibration point from the mapper and send it
				// to the cliend
				Point clientPoint = NETCalibrationControllerGlass.this.mCalibrationMapper
						.getCalibrationPoint(counter);

				super.mServer.send(GlassConfig.TO_CLIENT_CALIBRATE_DISPLAY,
						(int) clientPoint.x, (int) clientPoint.y);

				final Point serverPoint = getSampleFromCore();

				setUpPointsToMapper(clientPoint, serverPoint);

				counter++;
			}

			if (!error) {
				super.mServer.send(GlassConfig.TO_CLIENT_CALIBRATE_DISPLAY,
						-2, -2);
				NETCalibrationControllerGlass.this.mCalibrationMapper
				.calibrate();
				if (mCalibrationCallbacks != null)
					mCalibrationCallbacks.onCalibrationFinished();
			} else {
				if (mCalibrationCallbacks != null)
					mCalibrationCallbacks.onCalibrationError();
			}
		} catch (IOException e) {
			if (mCalibrationCallbacks != null)
				mCalibrationCallbacks.onCalibrationError();
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
		return new Point(sumX / GlassConfig.NO_SAMPLES, sumY
				/ GlassConfig.NO_SAMPLES);
	}
}
