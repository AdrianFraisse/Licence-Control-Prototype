package licencecontrol;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.rmi.CORBA.Util;

import licencecontrol.client.LicenceControl;
import licencecontrol.util.Crypto;

public class RunClient {

	public static void main(String[] args) throws MalformedURLException, RuntimeException, IOException {
//		LicenceControl.getInstance().controlOnServer();
		System.out.println(Crypto.generateToken());
	}

}
