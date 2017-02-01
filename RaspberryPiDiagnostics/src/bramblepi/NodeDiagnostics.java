package bramblepi;

import java.io.IOException;

public class NodeDiagnostics {
	
	public static final Double measureTemperature(){
		
		final String file_location = "/sys/class/thermal/thermal_zone0/temp";
		
		try{
			return (new Double(ReadFile.readFile(file_location)))/1000.0;
		} catch (IOException e){
			return new Double(0.0);
		}
		
	}
	
	public static final Double measureClockSpeed(){
		
		final String file_location = "/sys/devices/system/cpu/cpufreq/policy0/scaling_cur_freq";
		
		try{
			return new Double(ReadFile.readFile(file_location));
		} catch (IOException e){
			e.printStackTrace();
			return new Double(0.0);
		}
		
	}

}
