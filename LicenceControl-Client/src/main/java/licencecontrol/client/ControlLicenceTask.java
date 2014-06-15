package licencecontrol.client;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Simple thread permettant la vérification périodique de la licence.
 */
public class ControlLicenceTask extends TimerTask {
	
	@Override
	public void run() {
		try {
			LicenceControl.getInstance().controlOnServer();
		} catch (IOException e) {
			System.err.println("Erreur lors du contrôle de licence");
			System.exit(0);
		}
	}

}
