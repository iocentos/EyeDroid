package dk.itu.eyedroid.io.calibration;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Point;
import org.opencv.core.Rect;

import android.util.SparseArray;

/**
 * Calibration mapper is used to translate pupil coordinates using an
 * homography. I.e. translate coordinates to fit into google glass display.
 */
public abstract class CalibrationMapper {

	/*
	 * Calibration points sent to the client. The client should present these
	 * points one by one to the user for a few seconds so that the server can
	 * sample the coordinates.
	 */
	protected SparseArray<Point> calibPoints;
	protected boolean isCalibrated; // Is EyeDroid calibrated
	protected int presentationScreenHeight; // Client screen height
	protected int presentationScreenWidth; // Client screen widht
	protected Rect presentationScreen; // Display rectangle (screen)
	protected final int numberOfCalibrationPoints; // No. of calibration points
	protected List<Point> sourcePoints = new LinkedList<Point>(); // Source
																	// MatOfPoints
																	// list
	protected List<Point> destinationPoints = new LinkedList<Point>(); // Destination
																		// MatOfPoints
																		// list

	/**
	 * @return Is calibrated?
	 */
	public boolean isCalibrated() {
		return isCalibrated;
	}

	/**
	 * Default constructor
	 * 
	 * @param n
	 * @param m
	 * @param presentationScreenWidth
	 * @param presentationScreenHeight
	 */
	public CalibrationMapper(int n, int m, int presentationScreenWidth,
			int presentationScreenHeight) {
		this.presentationScreenHeight = presentationScreenHeight;
		this.presentationScreenWidth = presentationScreenWidth;
		numberOfCalibrationPoints = n * m;
		calibPoints = new SparseArray<Point>();
		computeCalibrationPoints(n, m);
		isCalibrated = false;
	}

	/**
	 * Add source point to mapper
	 * 
	 * @param point
	 *            Point to be added
	 */
	public void addSourcePoint(Point point) {
		sourcePoints.add(point);
	}

	/**
	 * Add destination point
	 * 
	 * @param point
	 *            Point to be added
	 */
	public void addDestinationPoint(Point point) {
		destinationPoints.add(point);
	}

	/**
	 * Main calibration method
	 */
	public final void calibrate() {
		doCalibration();
		isCalibrated = true;
	}

	/*
	 * Calculates the error point from the center of the screen. Updates the
	 * error values so that the next time the map method is called the errors
	 * are subtracted.
	 */
	public abstract void correctError(int inputX, int inputY);

	/**
	 * Get calibration points
	 * 
	 * @return Calibration points
	 */
	public int getTotalCalibrationPoints() {
		return numberOfCalibrationPoints;
	}

	/**
	 * Get calibration point
	 * 
	 * @param index
	 *            Point index
	 * @return Point
	 */
	public Point getCalibrationPoint(int index) {
		return calibPoints.get(index);
	}

	/**
	 * Clean mapper;
	 */
	public abstract void clean();

	// *************Abstract methods*********************

	/**
	 * Actual mapping method. Arguments are the input coordinates taken from
	 * server. The Absolute coordinates given by the core. Any previous errors
	 * are also passed in order to be subtracted.
	 * 
	 * @param inputX
	 *            X coordinate to be mapped
	 * @param inputY
	 *            Y coordinate to be mapped
	 * @return Mapped coordinate
	 */
	public abstract int[] map(float inputX, float inputY);

	/*
	 * After all the source and destination points have been set call this
	 * method will perform the calibration.
	 */
	protected abstract void doCalibration();

	/*
	 * Creates the calibration points and saves them in the array
	 * 
	 * @param n
	 * 
	 * @param m
	 */
	protected abstract void computeCalibrationPoints(int n, int m);

}
