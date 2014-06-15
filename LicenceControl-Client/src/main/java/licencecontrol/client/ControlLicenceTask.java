package licencecontrol.client;

import java.io.IOException;
import java.util.TimerTask;

public class ControlLicenceTask extends TimerTask {
	
	@Override
	public void run() {
		try {
			System.out.println("Controle via Timer");
			LicenceControl.getInstance().controlOnServer();
		} catch (IOException e) {
			System.err.println("Erreur lors du contrôle de licence");
			System.exit(0);
		}
	}

}
