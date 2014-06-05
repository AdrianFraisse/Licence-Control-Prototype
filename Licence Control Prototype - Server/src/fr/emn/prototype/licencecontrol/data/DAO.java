package fr.emn.prototype.licencecontrol.data;

public interface DAO {

	public boolean checkLicence(String licence);
	public String getChecksum(String licence);
	
}
