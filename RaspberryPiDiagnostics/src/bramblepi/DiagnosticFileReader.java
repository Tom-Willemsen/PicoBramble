package bramblepi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DiagnosticFileReader {

	private final String path;

	public DiagnosticFileReader(String path){
		this.path = path;
	}

	/**
	 * Reads a string from the specified file.
	 * @return the contents of the file
	 * @throws IOException if the file couldn't be read
	 */
	public String read() throws IOException {

		String output = "";

		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(path));

			output = bufferedReader.readLine();
		} finally {
			if (bufferedReader != null){
				bufferedReader.close();
			}
		}
		return output;
	}
}
