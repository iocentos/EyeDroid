package dk.itu.eyedroid.io.threads;

import java.io.IOException;

import android.util.Log;
import dk.itu.eyedroid.io.IORunner;
import dk.itu.spcl.jlpf.io.IOController;

public class WritingThread extends IORunner {

	public WritingThread(IOController controller) {
		super(controller);
	}

	private volatile boolean isStopped = false;

	public void stopThread() {
		isStopped = true;
	}

	@Override
	public void onRun() throws IOException{
		ioController.getOutputWriter().initWriter();
		while (!isStopped) {
			try {
				Log.i("---------------","writer thread is fucking running");
				ioController.write();
			} catch (IOException e) {
				ioController.getOutputWriter().cleanup();
				ioController.getOutputWriter().initWriter();
				e.printStackTrace();
				break;
			}
		}
	}
}