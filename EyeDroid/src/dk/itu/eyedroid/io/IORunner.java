package dk.itu.eyedroid.io;

import java.io.IOException;
import dk.itu.spcl.jlpf.io.IOController;

public abstract class IORunner extends Thread{

	private final int MAX_TRIALS = 3;
	private final int TIMEOUT = 500;
	private int attempt_count = 1;

	protected IOController ioController;
	private volatile boolean mIsStopped = false;

	public IORunner(IOController controller){
		ioController = controller;
	}

	public  boolean isStopped(){
		return mIsStopped;
	}

	public void stopThread() {
		mIsStopped = true;
	}

	@Override
	public void run(){
		try {
			while(attempt_count <= MAX_TRIALS){
				try{
					onInit();
					break;
				}catch(IOException e){
					try {
						attempt_count++;
						Thread.sleep(TIMEOUT);
					} catch (InterruptedException e1) {}
				}
			}

			if(attempt_count <= MAX_TRIALS){
				attempt_count = 1;
				while (!isStopped()) {
					onRun();
				}
			}else
				throw new IOException("Unable to initialize IORunner");

		} catch (IOException e) {
			e.printStackTrace();
			ioController.stop();
		}
	}

	public abstract void onInit() throws IOException;

	public abstract void onRun() throws IOException;
}
