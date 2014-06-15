package licencecontrol;

import java.io.IOException;
import java.net.MalformedURLException;

import licencecontrol.client.LicenceControl;

public class RunClient {

	public static void main(String[] args) throws MalformedURLException, RuntimeException, IOException {
		LicenceControl.getInstance();
		System.in.read();
		LicenceControl.getInstance().controlOnServer();
		System.in.read();
	}

}
