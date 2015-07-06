package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiPartFileUploader {

	File file1;
	List<File> files;

	public MultiPartFileUploader(String path) {
		// TODO Auto-generated constructor stub

		file1 = new File(path);
files=new ArrayList<File>();
		String charset = "UTF-8";
		File uploadFile1 = file1;
		// File uploadFile2 = new File("e:/Test/PIC2.JPG");
		String requestURL = "http://192.1.168.12:8080/BAZR/saveProductInfoAndImage";

		try {
			MultiPartUtility multipart = new MultiPartUtility(requestURL,
					charset);

			multipart.addHeaderField("User-Agent", "CodeJava");
			multipart.addHeaderField("Test-Header", "Header-Value");

			multipart.addFormField("description", "Cool Pictures");
			multipart.addFormField("keywords", "Java,upload,Spring");

			multipart.addFilePart("files", file1);
			// multipart.addFilePart("fileUpload", uploadFile2);

			List<String> response = multipart.finish();

			System.out.println("SERVER REPLIED:");

			for (String line : response) {
				System.out.println(line);
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

}
