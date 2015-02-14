package dk.itu.eyedroid.io.threads;

import java.io.IOException;

import dk.itu.eyedroid.io.IORunner;
import dk.itu.spcl.jlpf.io.IOController;

/**
 * Reading thread implementation. Implements reader initialization and execution
 * from IORunner.
 */

public class ReadingThread extends IORunner {

	/**
	 * Default constructor.
	 * 
	 * @param controller
	 *            IO controller instance
	 */
	public ReadingThread(IOController controller) {
		super(controller);
	}

	/**
	 * Initialize reader
	 */
	@Override
	public void onInit() throws IOException {
		ioController.getInputReader().initReader();
	}

	/**
	 * Write from input protocol.
	 */
	@Override
	public void onRun() throws IOException {
		ioController.read();
	}
}
