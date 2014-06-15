package licencecontrol;

import licencecontrol.client.LicenceControl;

public class TestSecurityManager {

	public static void desactivateExitCalls() {
		// Desactive les appels a System.exit(...)
		final SecurityManager securityManager = new SecurityManager() {
			public void checkPermission(java.security.Permission permission) {
				if (permission.getName().contains("exitVM")) {
					throw new SecurityException();
				}
			}
		};
		System.setSecurityManager(securityManager);
	}
	
	public static void main(String[] args) {
		desactivateExitCalls();
		try {
			// ne fonctionnera pas
			System.exit(0);
		} catch (SecurityException e) {
			System.out.println("System.exit desactive");
		}
		
		try {
			// cette fois l'appel de System.exit(0) fonctionnera
			LicenceControl.getInstance().exit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
