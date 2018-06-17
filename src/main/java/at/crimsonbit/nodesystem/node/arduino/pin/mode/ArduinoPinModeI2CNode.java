package at.crimsonbit.nodesystem.node.arduino.pin.mode;

import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.ArduinoPinMode;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoPinModeI2CNode extends AbstractNode {

	@NodeType
	public static ArduinoPinMode type = ArduinoPinMode.I2C;

	@NodeOutput("getMode")
	Pin.Mode mode;

	public ArduinoPinModeI2CNode() {

	}

	public void getMode() {
		mode = Pin.Mode.I2C;
	}

}