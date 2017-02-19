package bramblepi;

import java.io.IOException;

public class NodeDiagnostics {

	private final DiagnosticFileReader temperatureFileReader;
	private final DiagnosticFileReader clockSpeedFileReader;

	public NodeDiagnostics(){
		this("/sys/class/thermal/thermal_zone0/temp", 
				"/sys/devices/system/cpu/cpufreq/policy0/scaling_cur_freq");
	}

	/**
	 * Constructor.
	 * @param temperatureFile the location of the temperature file
	 * @param clockSpeedFile the location of the clock speed file
	 */
	public NodeDiagnostics(String temperatureFile, String clockSpeedFile){
		this.temperatureFileReader = new DiagnosticFileReader(temperatureFile);
		this.clockSpeedFileReader = new DiagnosticFileReader(clockSpeedFile);
	}

	/**
	 * Measures the temperature from file.
	 * @return the temperature
	 */
	public Double measureTemperature(){	
		try{
			return (new Double(temperatureFileReader.read()))/1000.0;
		} catch (IOException e){
			return new Double(0.0);
		}
	}

	/**
	 * Measures the clock speed from file.
	 * @return the clock speed
	 */
	public Double measureClockSpeed(){
		try{
			return new Double(clockSpeedFileReader.read());
		} catch (IOException e){
			return new Double(0.0);
		}
	}
}
