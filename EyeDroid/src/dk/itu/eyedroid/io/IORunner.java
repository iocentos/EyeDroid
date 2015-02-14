package dk.itu.eyedroid.io;

import java.io.IOException;
import dk.itu.spcl.jlpf.io.IOController;

/**
 * Abstracts reading and writing common thread functionality. If an excpetion is
 * thrown during the initialization of either reader or writer, IO controller is
 * stopped.
 */
public abstract class IORunner extends Thread {

	private final int MAX_TRIALS = 3; // Maximum number of allowed attempts to
										// initialize the RW protocols
	private final int TIMEOUT = 500; // Timeout to retry inititalization
	private int attempt_count = 1; // Fail counter

	protected IOController ioController; // Reference to IO Controller
	private volatile boolean mIsStopped = false; // Running state flag.

	/**
	 * Default constructor.
	 * 
	 * @param controller
	 *            IO controller instance
	 */
	public IORunner(IOController controller) {
		ioController = controller;
	}

	/**
	 * Get IORunner running state
	 * 
	 * @return
	 */
	public boolean isStopped() {
		return mIsStopped;
	}

	/**
	 * Stop IORunner thread
	 */
	public void stopThread() {
		mIsStopped = true;
	}

	/**
	 * Initialize redear and writer, then execute them. If an excpetion is
	 * thrown during the initialization of either reader or writer, IO
	 * controller is stopped.
	 */
	@Override
	public void run() {
		try {
			// Try to initialize
			while (attempt_count <= MAX_TRIALS) {
				try {
					onInit();
					break;
				} catch (IOException e) {
					try {
						attempt_count++;
						Thread.sleep(TIMEOUT);
					} catch (InterruptedException e1) {
					}
				}
			}

			// If initialization attempts exceed the maximum allowd, throw
			// exception.
			if (attempt_count <= MAX_TRIALS) {
				attempt_count = 1;
				while (!isStopped()) {
					onRun();
				}
			} else
				throw new IOException("Unable to initialize IORunner");

		} catch (IOException e) {
			e.printStackTrace();
			ioController.stop();
		}
	}

	/**
	 * IORunner initialization implementation. The method should be used to
	 * initialize RW protocols.
	 * 
	 * @throws IOException
	 */
	public abstract void onInit() throws IOException;

	/**
	 * IORunner execution method. The method should be used to read and write
	 * from/to the IO Controller
	 * 
	 * @throws IOException
	 */
	public abstract void onRun() throws IOException;
}
