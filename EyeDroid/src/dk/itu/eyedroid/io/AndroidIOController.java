package dk.itu.eyedroid.io;

import dk.itu.eyedroid.io.threads.ReadingThread;
import dk.itu.eyedroid.io.threads.WritingThread;
import dk.itu.spcl.jlpf.core.ProcessingCore;
import dk.itu.spcl.jlpf.io.IOController;
import dk.itu.spcl.jlpf.io.InputReader;
import dk.itu.spcl.jlpf.io.OutputWriter;

public class AndroidIOController extends IOController {

	private ReadingThread readerThread;
	private WritingThread writerThread;

	public AndroidIOController(ProcessingCore core, InputReader reader, OutputWriter writer) {
		super(core, reader, writer);
	}

	@Override
	public void setupController() {
		readerThread = new ReadingThread(this);
		writerThread = new WritingThread(this);
	}

	@Override
	protected void onExecute() {
		readerThread.start();
		writerThread.start();
	}

	@Override
	protected void onStop() {
		readerThread.stopThread();
		writerThread.stopThread();
	}
}