package licencecontrol;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Timer;

import licencecontrol.client.ControlLicenceTask;
import licencecontrol.client.LicenceControl;

public class RunClient {

	public static void main(String[] args) throws MalformedURLException, RuntimeException, IOException {
		LicenceControl.getInstance().controlOnServer();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(new ControlLicenceTask(), new Date(), 10);
		System.out.println("Launched");
		System.out.println("Terminated");
	}

}
