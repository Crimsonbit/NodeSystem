package at.crimsonbit.nodesystem.node.arduino;

import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.ArduinoPin;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoPinOutputNode extends AbstractNode {
	@NodeType
	public static ArduinoPin type = ArduinoPin.OUTPUT;

	@NodeOutput("getMode")
	Pin.Mode mode;
	
	public void getMode() {
		mode = Pin.Mode.OUTPUT;
		
	}

}
