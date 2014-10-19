package dk.itu.eyedroid.io;

import java.io.IOException;

import dk.itu.spcl.jlpf.io.IOController;

public abstract class IORunner extends Thread{

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
			onRun();
		} catch (IOException e) {
			e.printStackTrace();
			ioController.stop();
		}
	}
	
	public abstract void onRun() throws IOException;
}
