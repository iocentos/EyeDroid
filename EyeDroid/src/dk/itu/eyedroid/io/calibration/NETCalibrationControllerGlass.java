package dk.itu.eyedroid.io.calibration;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dk.itu.eyedroid.io.NetClientConfig;
import dk.itu.eyedroid.io.Server;

public class NETCalibrationControllerGlass extends NETCalibrationController{

	/**
	 * Default constructor
	 * @param server Network server
	 */
	public NETCalibrationControllerGlass(Server server){
		super(server);
	}

	/**
	 * Calibration process
	 * 
	 * @param receivePacket Packet receieved from client.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */

	public boolean calibrate() throws IOException{		

		final ExecutorService exec = Executors.newSingleThreadExecutor();
		final Server server = super.mServer;
		
		Future<Boolean> fut = exec.submit(new Callable<Boolean>() {
			public Boolean call() throws IOException, InterruptedException {
				int[] message;
				boolean error = false;
				
				for(int i=0 ; i < NetClientConfig.NO_POINTS ; i++){

					if(i == 0){
						server.send(NetClientConfig.TO_CLIENT_CALIBRATE_DISPLAY, -1, -1);
					}else{
						server.send(NetClientConfig.TO_CLIENT_CALIBRATE_DISPLAY,0,0);
						for(int j = 0; j < NetClientConfig.NO_SAMPLES; j++){
							Thread.currentThread();
							Thread.sleep(NetClientConfig.WAIT_TO_SAMPLE);
							//TODO Calibrate
						}
					}

					message = server.read(true);
					if(NetClientConfig.TO_EYEDROID_READY != message[0]){
						error = true;
						break;
					}
				}
				return error;
			}});
		
		try {
			return !fut.get();
		} catch (ExecutionException | InterruptedException e){
			e.printStackTrace();
			return false;
		}
	}
}
