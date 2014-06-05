package com.emn.prototype.licenceControl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LicenceControler {
	
	public String checkLicence() throws RuntimeException, IOException {
		String response = checkSum();
		return response = response + "-" + getLicence();
	}

	private String checkSum() {
		String filepath = "bin"+File.separator+"classes"+File.separator+"licenceControl"+File.separator+"LicenceControler.class";
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");// MD5
            fis = new FileInputStream(filepath);
            byte[] dataBytes = new byte[1024];
            int nread = 0; 

            while((nread = fis.read(dataBytes)) != -1)
                 md.update(dataBytes, 0, nread);

            byte[] mdbytes = md.digest();

            for(int i=0; i<mdbytes.length; i++)
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100 , 16).substring(1));
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally {
        	if (fis != null) {
        		try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }

        System.out.println("Checksum: "+sb);
        return sb.toString();
	}
	
	private String getLicence() throws IOException {
		String fileName = "licence.txt";
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		BufferedReader stream = new BufferedReader(new InputStreamReader(inputStream));

		String licence = "";
		String ligne;
		while ((ligne=stream.readLine())!=null){
			System.out.println(ligne);
			licence+=ligne+"\n";
		}
		stream.close();
		return licence;
	}
}
