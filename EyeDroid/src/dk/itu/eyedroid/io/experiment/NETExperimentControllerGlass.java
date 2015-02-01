package dk.itu.eyedroid.io.experiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.opencv.core.Point;

import android.os.Environment;
import android.util.Log;
import dk.itu.eyedroid.io.GlassConfig;
import dk.itu.eyedroid.io.calibration.CalibrationMapper;

public class NETExperimentControllerGlass extends NETExperimentController {

	public static final String TAG = "Experiment";			//Log TAG
	public static final String FILE_NAME_PREFIX = "/exp_";	//File prefix	

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

		Point[] destinationPoints = GlassConfig.EXPERIMENT_POINTS;
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
				Point clientPoint = destinationPoints[counter];
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

				saveExperimentFile(createExperimentOutput(destinationPoints, sampledPoints, sampledMappedPoints));

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

	private String createExperimentOutput(Point[] destinationPoints, Point[] sampledPoints, Point[] sampledMappedPoints){

		String content = "Point no.\t\tDestination\t\tSample\t\tMapped sample\t\t|Destination-Mapped sample|\n";

		for (int i = 0; i < destinationPoints.length; i++) {
			content += String.format("%d \t\t (%2f, %2f) \t\t  (%2f, %2f) \t\t  (%2f, %2f) \t\t  (%2f, %2f)\n",
					i,
					destinationPoints[i].x, destinationPoints[i].y,
					sampledPoints[i].x, sampledPoints[i].y,
					sampledMappedPoints[i].x, sampledMappedPoints[i].y,
					Math.abs(destinationPoints[i].x - sampledMappedPoints[i].x),
					Math.abs(destinationPoints[i].y - sampledMappedPoints[i].y)
					);
		}
		return content;
	}

	private void saveExperimentFile(String content){

		String fileName = Environment.getExternalStorageDirectory().getAbsolutePath().concat(FILE_NAME_PREFIX);

		//Add date time to file name		
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
		Date date = new Date();
		fileName = fileName.concat(dateFormat.format(date));

		Log.i(TAG, fileName);
		if(createFile(fileName)){
			Log.i(TAG , "Experiment file was created successully");
			writeExperimentToFile(fileName, content);
		}else
			Log.i(TAG , "Experiment file not created");
	}

	/**
	 * Create new file
	 * @return Is file created?
	 */
	private boolean createFile(String fileName) {
		File file = new File(fileName);
		try {
			if (!file.exists())
				file.createNewFile();
			else{
				file.delete();
				file.createNewFile();
			}

			if (file.exists())
				return true;
			else
				return false;
		} catch (IOException e) {
			return false;
		}
	}

	private void writeExperimentToFile(String fileName, String content) {
		PrintWriter writer = null;
		try {
			File file = new File(fileName);
			writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			writer.print(content);
			writer.close();
		} catch (IOException e) {}
	}
}
