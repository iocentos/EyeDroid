package dk.itu.eyedroid.io;

import dk.itu.eyedroid.io.threads.ReadingThread;
import dk.itu.eyedroid.io.threads.WritingThread;
import dk.itu.spcl.jlpf.core.ProcessingCore;
import dk.itu.spcl.jlpf.io.IOController;
import dk.itu.spcl.jlpf.io.InputReader;
import dk.itu.spcl.jlpf.io.OutputWriter;

/** 
 * Main IO controller implementation. Two threads are required in order to read and write.
 * Read: read from input protocol and queue a bundle into the processing core.
 * Write: pop a bundle from the processing core and write it to the output protocol.
 */

public class IOAndroidController extends IOController {

	private ReadingThread readerThread;	//Reading thread
	private WritingThread writerThread; //Writing thread

	/**
	 * Default constructor.
	 * @param core Processing core instance
	 * @param reader Input reader
	 * @param writer Output writer.
	 */
	public IOAndroidController(ProcessingCore core, InputReader reader, OutputWriter writer) {
		super(core, reader, writer);
	}
	
	/**
	 * Setup read and write threads
	 */
	@Override
	public void setupController() {
		readerThread = new ReadingThread(this);
		writerThread = new WritingThread(this);
	}

	/**
	 * Execute read and write threads
	 */
	@Override
	protected void onExecute() {
		readerThread.start();
		writerThread.start();
	}

	/**
	 * Stop read and write threads.
	 */
	@Override
	protected void onStop() {
		readerThread.stopThread();
		writerThread.stopThread();
	}
}