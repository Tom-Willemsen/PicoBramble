package bramblepi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

	static String readFile(String path) throws IOException {

		String output = "";
		
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		
		try {
			bufferedReader = new BufferedReader(new FileReader(path));

			output = bufferedReader.readLine();
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
			if (fileReader != null)
				fileReader.close();
		}
		return output;
	}
}
