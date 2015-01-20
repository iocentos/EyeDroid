package dk.itu.eyedroid.io.calibration;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opencv.core.Point;

import android.util.Log;
import dk.itu.eyedroid.io.NetClientConfig;
import dk.itu.eyedroid.io.Server;

public class NETCalibrationControllerGlass extends NETCalibrationController {

	/**
	 * Default constructor
	 * 
	 * @param server
	 *            Network server
	 * @param mapper
	 *            Calibration mapper
	 */
	public NETCalibrationControllerGlass(Server server, CalibrationMapper mapper) {
		super(server, mapper);
	}

	/**
	 * Calibration process
	 * 
	 * @param receivePacket
	 *            Packet receieved from client.
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public void calibrate() throws IOException {

		final ExecutorService exec = Executors.newSingleThreadExecutor();
		final Server server = super.mServer;

		exec.submit(new Runnable() {
			@Override
			public void run() {
				int[] message;
				boolean error = false;

				if (mCalibrationCallbacks != null)
					mCalibrationCallbacks.onCalibrationStarted();

				try {
					server.send(NetClientConfig.TO_CLIENT_CALIBRATE_DISPLAY,
							-1, -1);

					for (int i = 0; i < NetClientConfig.NO_POINTS; i++) {

						message = server.read(true);
						while (message[0] == -1) {
							message = server.read(true);
						}
						if (NetClientConfig.TO_EYEDROID_READY != message[0]) {
							Log.i(NetClientConfig.TAG, "Mesasge is not TO_EYEDROID_READY " + message[0]);
							error = true;
							break;
						}
						// get the calibration point from the mapper and send it
						// to the cliend
						Point clientPoint = NETCalibrationControllerGlass.this.mCalibrationMapper
								.getCalibrationPoint(i);

						server.send(
								NetClientConfig.TO_CLIENT_CALIBRATE_DISPLAY,
								(int) clientPoint.x, (int) clientPoint.y);

						Point serverPoint = getSampleFromCore();

						setUpPointsToMapper(clientPoint, serverPoint);
					}

					if (!error) {
						server.send(
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
		});
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
