package bramblepi;

import java.io.Serializable;

public class NodeDiagnosticVariables implements Serializable {

	private static final long serialVersionUID = -6200224416413123416L;
	
	private Double temperature;
	private Double clockSpeed;
	
	public NodeDiagnosticVariables(){
		this.temperature = NodeDiagnostics.measureTemperature();
		this.clockSpeed = NodeDiagnostics.measureClockSpeed();
	}
	
	public Double getTemperature() {
		return temperature;
	}
	
	public Double getClockSpeed() {
		return clockSpeed;
	}
	
}
