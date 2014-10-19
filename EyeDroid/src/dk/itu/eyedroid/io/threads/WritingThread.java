package dk.itu.eyedroid.io.threads;

import java.io.IOException;

import dk.itu.eyedroid.io.IORunner;
import dk.itu.spcl.jlpf.io.IOController;

public class WritingThread extends IORunner {

	public WritingThread(IOController controller) {
		super(controller);
	}

	@Override
	public void onInit() throws IOException{
		ioController.getOutputWriter().initWriter();
	}
	
	@Override
	public void onRun() throws IOException{
		try {
			ioController.write();
		} catch (IOException e) {
			e.printStackTrace();
			ioController.getOutputWriter().cleanupWriter();
			ioController.getOutputWriter().initWriter();
		}
	}
}