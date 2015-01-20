package dk.itu.eyedroid.io.calibration;

import java.io.IOException;

import org.opencv.core.Point;

import android.util.Log;
import dk.itu.eyedroid.io.NetClientConfig;
import dk.itu.eyedroid.io.Server;

public class NETCalibrationControllerGlass extends NETCalibrationController {

	/**
	 * Default constructor
	 * 
	 * @param server
	 * @param mapper
	 * Calibration mapper
	 */
	public NETCalibrationControllerGlass(CalibrationMapper mapper) {
		super(mapper);
	}

	/**
	 * Calibration process
	 * 
	 * @param receivePacket Packet receieved from client.
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public void calibrate() throws IOException {

		int[] message;
		boolean error = false;

		if (mCalibrationCallbacks != null)
			mCalibrationCallbacks.onCalibrationStarted();

		try {
			super. mServer.send(NetClientConfig.TO_CLIENT_CALIBRATE_DISPLAY,
					-1, -1);

			for (int i = 0; i < NetClientConfig.NO_POINTS; i++) {

				message = super. mServer.read();
				while (message[0] == -1) {
					message = super. mServer.read();
				}
				if (NetClientConfig.TO_EYEDROID_READY != message[0]) {
					Log.i(NetClientConfig.TAG, "Mesasge is not TO_EYEDROID_READY " + message[0]);
					error = true;
					break;
				}
				// get the calibration point from the mapper and send it
				// to the cliend
				Point clientPoint = NETCalibrationControllerGlass.this.mCalibrationMapper.getCalibrationPoint(i);

				super. mServer.send(
						NetClientConfig.TO_CLIENT_CALIBRATE_DISPLAY,
						(int) clientPoint.x, (int) clientPoint.y);

				Point serverPoint = getSampleFromCore();

				setUpPointsToMapper(clientPoint, serverPoint);
			}

			if (!error) {
				super. mServer.send(
						NetClientConfig.TO_CLIENT_CALIBRATE_DISPLAY,
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

		for (int j = 0; j < NetClientConfig.NO_SAMPLES; j++) {
			Thread.currentThread();
			try {
				Thread.sleep(NetClientConfig.WAIT_TO_SAMPLE);
				if (mOutputProtocol != null) {
					xy = this.mOutputProtocol.getXY();
					sumX += xy[0];
					sumY += xy[1];
				}
			} catch (InterruptedException e) {
			}

		}
		return new Point(sumX / NetClientConfig.NO_SAMPLES, sumY
				/ NetClientConfig.NO_SAMPLES);
	}
}
