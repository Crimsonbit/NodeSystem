package at.crimsonbit.nodesystem.node.arduino.pin.mode;

import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.ArduinoPinMode;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoPinModeOnewireNode extends AbstractNode {

	@NodeType
	public static ArduinoPinMode type = ArduinoPinMode.ONEWIRE;

	@NodeOutput("getMode")
	Pin.Mode mode;

	public ArduinoPinModeOnewireNode() {

	}

	public void getMode() {
		mode = Pin.Mode.ONEWIRE;
	}

}