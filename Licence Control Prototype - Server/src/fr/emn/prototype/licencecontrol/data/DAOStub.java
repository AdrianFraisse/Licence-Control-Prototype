package fr.emn.prototype.licencecontrol.data;

public class DAOStub implements DAO {

	@Override
	public boolean checkLicence(String licence) {
		return true;
	}

	@Override
	public String getChecksum(String licence) {
		return "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	}

}
