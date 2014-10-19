package dk.itu.eyedroid.io.threads;

import java.io.IOException;

import dk.itu.eyedroid.io.IORunner;
import dk.itu.spcl.jlpf.io.IOController;

public class ReadingThread extends IORunner {

	public ReadingThread(IOController controller) {
		super(controller);
	}

	@Override
	public void onInit() throws IOException{
		ioController.getInputReader().initReader();
	}
	
	@Override
	public void onRun() throws IOException {
		
	}
}
