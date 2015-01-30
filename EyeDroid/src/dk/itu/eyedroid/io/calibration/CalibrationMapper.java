package dk.itu.eyedroid.io.calibration;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Point;
import org.opencv.core.Rect;

import android.util.SparseArray;

public abstract class CalibrationMapper {

	protected boolean isCalibrated;

	/*
	 * Width of the client screen. In this case is the Google Glass screen
	 */
	protected final int PRESENTATION_SCREEN_WIDTH;// = 640;

	/*
	 * Height of the client screen. In this case is the Google Glass screen
	 */
	protected final int PRESENTATION_SCREEN_HEIGHT; // = 360;

	/*
	 * Calibration points sent to the client. The client should present these
	 * points one by one to the user for a few seconds so that the server can
	 * sample the coordinates.
	 */
	protected SparseArray<Point> calibPoints;

	/*
	 * The rectangle of the screen on the client.
	 */
	protected Rect presentationScreen;

	protected final int numberOfCalibrationPoints;

	/*
	 * List of points to create the source MatOfPoints
	 */
	protected List<Point> sourcePoints = new LinkedList<Point>();

	/*
	 * List of points to create the destination MatOfPoints
	 */
	protected List<Point> destinationPoints = new LinkedList<Point>();

	public CalibrationMapper(int n, int m, int presentationScreenWidth,
			int presentationScreenHeight) {
		PRESENTATION_SCREEN_HEIGHT = presentationScreenHeight;
		PRESENTATION_SCREEN_WIDTH = presentationScreenWidth;
		numberOfCalibrationPoints = n * m;
	
		calibPoints = new SparseArray<Point>();

		computeCalibrationPoints(n, m);

		isCalibrated = false;

	}

	public void addSourcePoint(Point point) {
		sourcePoints.add(point);
	}

	public void addDestinationPoint(Point point) {
		destinationPoints.add(point);
	}

	public final void calibrate() {
		doCalibration();
		isCalibrated = true;
	}

	/*
	 * Actual mapping method. Arguments are the input coordinates taken from
	 * server. The Absolute coordinates given by the core. Any previous errors
	 * are also passed in order to be subtracted.
	 */
	public abstract int[] map(float inputX, float inputY);

	/*
	 * After all the source and destination points have been set call this
	 * method will perform the calibration.
	 */
	protected abstract void doCalibration();

	/*
	 * Creates the calibration points and saves them in the array
	 */
	protected abstract void computeCalibrationPoints(int n, int m);

	/*
	 * Calculates the error point from the center of the screen. Updates the
	 * error values so that the next time the map method is called the errors
	 * are subtracted.
	 */
	public abstract void correctError(int inputX, int inputY);

	public boolean isCalibrated() {
		return isCalibrated;
	}

	public int getTotalCalibrationPoints() {
		return numberOfCalibrationPoints;
	}

	public Point getCalibrationPoint(int index) {
		return calibPoints.get(index);
	}
	
	public abstract void clean();
}
