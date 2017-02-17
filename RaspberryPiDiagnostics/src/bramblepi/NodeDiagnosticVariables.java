package bramblepi;

import java.io.Serializable;

public class NodeDiagnosticVariables implements Serializable {

	private static final long serialVersionUID = -6200224416413123416L;
	
	private Double temperature;
	private Double clockSpeed;
	
	public NodeDiagnosticVariables(){
		this(new NodeDiagnostics());
	}
	
	public NodeDiagnosticVariables(NodeDiagnostics nodeDiagnostics){
		this.temperature = nodeDiagnostics.measureTemperature();
		this.clockSpeed = nodeDiagnostics.measureClockSpeed();
	}
	
	public Double getTemperature() {
		return temperature;
	}
	
	public Double getClockSpeed() {
		return clockSpeed;
	}
	
}
