package dk.itu.eyedroid;

import dk.itu.eyedroid.statistics.FileStatisticsLogger;
import android.content.Context;
import android.widget.ImageView;
import dk.itu.eyedroid.filters.AfterErodeDilateFilter;
import dk.itu.eyedroid.filters.BeforeErodeDilateFilter;
import dk.itu.eyedroid.filters.BlobDetectionFilter;
import dk.itu.eyedroid.filters.CoordinatesFilter;
import dk.itu.eyedroid.filters.DetectAndDrawPupilFilter;
import dk.itu.eyedroid.filters.PreviewFilter;
import dk.itu.eyedroid.filters.RGB2GRAYFilter;
import dk.itu.eyedroid.filters.ThresholdFilter;
import dk.itu.eyedroid.io.IOAndroidController;
import dk.itu.spcl.jlpf.core.FilterComposite;
import dk.itu.spcl.jlpf.core.ProcessingCore;
import dk.itu.spcl.jlpf.io.IOController;
import dk.itu.spcl.jlpf.io.InputReader;
import dk.itu.spcl.jlpf.io.OutputWriter;

/**
 * EyeDroid core implementation. IO controller is defined to read from source
 * and produce to destination. Core and IO controller are based on JLPF library.
 * PIpes and filters arquitecture
 */
public class EyeDroid {

	private ProcessingCore core;			//Processing core
	private IOController ioController;		//IO controller intsance
	private PreviewFilter mPreviewFilter;	//Display preview filter
	private Context mContext;				//Application context

	private static final int CORE_QUEUE_SIZE = 10;			//Pipe capacity
	private static final int NUM_OF_THREADS = 3;			//No of threads to run the processing filters
	private static final boolean ENABLE_STATISTICS= false;	//Enable/disable statistics

	/**
	 * Deault constructor
	 * @param context Application context
	 */
	public EyeDroid(Context context){
		this.mContext = context;
		core = new ProcessingCore(CORE_QUEUE_SIZE);
	}

	/**
	 * Attach IO protocols to core
	 * @param reader Source
	 * @param writer Sink
	 */
	public void setIOProtocols(InputReader reader, OutputWriter writer) {
		ioController = new IOAndroidController(core, reader, writer);
	}

	/**
	 * Add coordinates result preview to application
	 * @param imageView Preview image view
	 * @return Preview filter
	 */
	public PreviewFilter addAndGetPreview(ImageView imageView){
		if (imageView != null) {
			mPreviewFilter = new PreviewFilter(mContext, imageView);
			mPreviewFilter.setFilterName("Preview filter");
			return mPreviewFilter;
		}
		return null;
	}

	/**
	 * Parallel algorithm execution setup. Processing algorithm is decomposed in 
	 * 3 composites and executed in 3 threads. Each composite a thread.
	 */
	private void setUpParallelAlgorithm() {
		RGB2GRAYFilter rgb2gray = new RGB2GRAYFilter();
		rgb2gray.setFilterName("RGB2Gray");
		FilterComposite compo1 = new FilterComposite();
		FilterComposite compo2 = new FilterComposite();
		FilterComposite compo3 = new FilterComposite();

		BeforeErodeDilateFilter beforeErode = new BeforeErodeDilateFilter();
		beforeErode.setFilterName("Before dilation");

		ThresholdFilter thresholdFilter = new ThresholdFilter();
		thresholdFilter.setFilterName("Threshold");

		AfterErodeDilateFilter afterErode = new AfterErodeDilateFilter();
		afterErode.setFilterName("After dilation");

		BlobDetectionFilter blobDetectionFilter = new BlobDetectionFilter();
		blobDetectionFilter.setFilterName("Blob detection");

		DetectAndDrawPupilFilter detectAndDrawPupilFilter = new DetectAndDrawPupilFilter();
		detectAndDrawPupilFilter.setFilterName("Detect and draw");

		CoordinatesFilter coordinatesFilter = new CoordinatesFilter();
		coordinatesFilter.setFilterName("Coordinates");

		compo1.addFilter(rgb2gray);
		compo1.addFilter(beforeErode);
		compo1.addFilter(thresholdFilter);
		compo2.addFilter(afterErode);

		compo3.addFilter(blobDetectionFilter);
		compo3.addFilter(detectAndDrawPupilFilter);
		compo3.addFilter(coordinatesFilter);
		if (mPreviewFilter != null)
			compo3.addFilter(mPreviewFilter);

		compo1.setFilterName("Composite 1");
		compo2.setFilterName("Composite 2");
		compo3.setFilterName("Composite 3");

		core.addFilter(compo1);
		core.addFilter(compo2);
		core.addFilter(compo3);
	}

	/**
	 * Start EyeDroid
	 */
	public void start() {
		if (core == null || ioController == null)
			throw new RuntimeException(
					"Core and IOController are not set up. Call setIOProtocols first");

		setUpParallelAlgorithm();

		core.start(NUM_OF_THREADS);

		if(ENABLE_STATISTICS)
			core.enableStatistics(new FileStatisticsLogger(FileStatisticsLogger.STATISTICS_FULL_PATH), 5000);
		
		ioController.start();
	}

	/**
	 * Stop EyeDroid
	 */
	public void stop() {
		core.stop();
		ioController.stop();
	}
}
