package at.crimsonbit.nodesystem.node.arduino.pin.mode;

import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.ArduinoPinMode;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoPinModeShiftNode extends AbstractNode {

	@NodeType
	public static ArduinoPinMode type = ArduinoPinMode.SHIFT;

	@NodeOutput("getMode")
	Pin.Mode mode;

	public ArduinoPinModeShiftNode() {

	}

	public void getMode() {
		mode = Pin.Mode.SHIFT;
	}

}