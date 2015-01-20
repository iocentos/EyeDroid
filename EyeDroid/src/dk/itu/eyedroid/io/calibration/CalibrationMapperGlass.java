package dk.itu.eyedroid.io.calibration;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import android.util.Log;
import dk.itu.eyedroid.io.NetClientConfig;

/*
 * The class supposes that is working with a matrix of NxN calibration points.
 */
public class CalibrationMapperGlass extends CalibrationMapper{

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

	public CalibrationMapperGlass(int n, int m, int presentationScreenWidth,
			int presentationScreenHeight) {
		super(n , m , presentationScreenWidth , presentationScreenHeight);
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
		
		homography = Calib3d.findHomography(newSource, newDestination, 0, 5);
		
		if(homography != null){
			Log.i(NetClientConfig.TAG, "Homography is not null" + homography.rows() + " " + homography.cols());
		}
	}

	@Override
	protected void computeCalibrationPoints(int n, int m) {
		presentationScreen = new Rect(new Point(0, 0), new Point(
				PRESENTATION_SCREEN_WIDTH, PRESENTATION_SCREEN_HEIGHT));

		int offset = 100;

		int count = 0;

		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++) {
				calibPoints.put(count, new Point(
						((presentationScreen.width - 2 * offset) / (m - 1)) * j
								+ offset,
						((presentationScreen.height - 2 * offset) / (n - 1))
								* i + offset));

				// TODO missing code
				// WTF
				// calibPoints[count] = Point.Add(calibPoints[count], new Size(
				// PresentationScreen.Left, PresentationScreen.Top));

				count++;
			}
	}

	@Override
	public int[] map(float inputX, float inputY) {
		return map(inputX, inputY, gazeErrorX, gazeErrorY);
	}

	private int[] map(float inputX, float inputY, double errorX, double errorY) {

		Point output = new Point();
		Mat src = new Mat(1, 3, CvType.CV_32F);
		Mat dst = new Mat(1, 3, CvType.CV_32F);

		src.put(0, 0, inputX);
		src.put(0, 1, inputY);
		src.put(0, 2, 1);

		// TODO check also the homography * src
//		Mat newHomo = homography.col(0);
		
		dst = homography.mul(src);

		float[] in = new float[1];

		dst.get(0, 0, in);
		float dstX = in[0];
		dst.get(0, 1, in);
		float dstY = in[0];
		dst.get(0, 2, in);
		float dstZ = in[0];

		output.x = dstX / dstZ;
		output.y = dstY / dstZ;

		output.x -= errorX;
		output.y -= errorY;
		
		int[] o = new int[2];
		
		o[0] = (int) output.x;
		o[1] = (int) output.y;

		return o;
	}

	@Override
	public void correctError(int inputX , int inputY) {

		int[] error = map((float) inputX, (float) inputY, 320, 180);

		gazeErrorX = error[0];
		gazeErrorY = error[1];

	}
}
