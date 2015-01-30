package dk.itu.eyedroid.io.calibration;

import java.util.LinkedList;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import android.util.Log;

/*
 * The class supposes that is working with a matrix of NxN calibration points.
 */
public class CalibrationMapperGlass extends CalibrationMapper {

	/*
	 * Source rectangle of points. These points match the four points in the
	 * source image taken at the server.
	 */
	private MatOfPoint2f source;

	/*
	 * Destination rectangle of points. These points match the four points in
	 * the client screen on the Glass
	 */
	private MatOfPoint2f destination;

	private Mat homography;

	private double gazeErrorX;

	private double gazeErrorY;

	public CalibrationMapperGlass(int n, int m, int presentationScreenWidth, int presentationScreenHeight){
		super(n, m, presentationScreenWidth, presentationScreenHeight);
		source = new MatOfPoint2f();
		destination = new MatOfPoint2f();
	}

	@Override
	public void addSourcePoint(Point point) {
		super.addSourcePoint(point);
		if (sourcePoints.size() == numberOfCalibrationPoints)
			source.fromList(sourcePoints);
	}

	@Override
	public void addDestinationPoint(Point point) {
		super.addDestinationPoint(point);
		if (destinationPoints.size() == numberOfCalibrationPoints)
			destination.fromList(destinationPoints);
	}

	@Override
	public void doCalibration() {
		if (destinationPoints.size() != numberOfCalibrationPoints
				|| sourcePoints.size() != numberOfCalibrationPoints)
			throw new RuntimeException("Calibration points are not correct. "
					+ "Destination points " + destinationPoints.size()
					+ " Source points " + sourcePoints.size());

		MatOfPoint2f newSource = new MatOfPoint2f(source);
		MatOfPoint2f newDestination = new MatOfPoint2f(destination);

		Log.i("EyeNet", "Performing calibration");

		homography = Calib3d.findHomography(newSource, newDestination, 0, 3);
	}

	@Override
	protected void computeCalibrationPoints(int n, int m) { 
		
		presentationScreen = new Rect(new Point(0, 0), new Point(PRESENTATION_SCREEN_WIDTH, PRESENTATION_SCREEN_HEIGHT));

		int offset = 100;
		int count = 0;

		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++) {
				calibPoints.put(count, new Point(
						((presentationScreen.width - 2 * offset) / (m - 1)) * j
								+ offset,
						((presentationScreen.height - 2 * offset) / (n - 1))
								* i + offset));
				count++;
			}
	}

	@Override
	public int[] map(float inputX, float inputY) {
		 return map(inputX, inputY, gazeErrorX, gazeErrorY);
	}

	private int[] map(float inputX, float inputY, double errorX, double errorY) {
		Log.i("EyeNet", "Requesting for mapping x and y");

		float[][] src = new float[3][1];
		float[][] dst;

		src[0][0] = inputX;
		src[1][0] = inputY;
		src[2][0] = 1;

		float[][] homo = new float[3][3];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				homo[i][j] = (float) homography.get(i, j)[0];
			}

		dst = multiply(homo, src);

		Point output = new Point();
		output.x = (int) (dst[0][0] / dst[2][0]);
		output.y = (int) (dst[1][0] / dst[2][0]);

		output.x -= errorX;
		output.y -= errorY;

		int[] o = new int[2];

		o[0] = (int) output.x;
		o[1] = (int) output.y;

		return o;
	}

	public static float[][] multiply(float[][] m1, float[][] m2) {
		int m1rows = m1.length;
		int m1cols = m1[0].length;
		int m2rows = m2.length;
		int m2cols = m2[0].length;
		
		if (m1cols != m2rows) {
			throw new IllegalArgumentException("matrices don't match: " + m1cols + " != " + m2rows);
		}
		
		float[][] result = new float[m1rows][m2cols];
		for (int i = 0; i < m1rows; i++) {
			for (int j = 0; j < m2cols; j++) {
				for (int k = 0; k < m1cols; k++) {
					result[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
		return result;
	}

	@Override
	public void correctError(int inputX, int inputY) {
		int[] error = map((float) inputX, (float) inputY, 320, 180);
		gazeErrorX = error[0];
		gazeErrorY = error[1];

	}

	@Override
	public void clean() {
		this.isCalibrated = false;
		this.sourcePoints = new LinkedList<>();
		this.destinationPoints = new LinkedList<>();
		this.source = new MatOfPoint2f();
		this.destination = new MatOfPoint2f();
		this.homography = null;
	}
}
