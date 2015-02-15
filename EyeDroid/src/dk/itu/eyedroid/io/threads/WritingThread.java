package dk.itu.eyedroid.io.threads;

import java.io.IOException;

import dk.itu.eyedroid.io.IORunner;
import dk.itu.spcl.jlpf.io.IOController;

/**
 * Writting thread implementation. Implements writer initialization and
 * execution from IORunner.
 */

public class WritingThread extends IORunner {

	/**
	 * Default constructor.
	 * 
	 * @param controller
	 *            IO controller instance
	 */
	public WritingThread(IOController controller) {
		super(controller);
	}

	/**
	 * Initialize writer
	 */
	@Override
	public void onInit() throws IOException {
		ioController.getOutputWriter().initWriter();
	}

	/**
	 * Write into protocol output. In case of excemption is thrown, cleanup and
	 * initialize again.
	 */
	@Override
	public void onRun() throws IOException {
		try {
			ioController.write();
		} catch (IOException e) {
			e.printStackTrace();
			ioController.getOutputWriter().cleanupWriter();
			ioController.getOutputWriter().initWriter();
		}
	}
}