package at.crimsonbit.nodesystem.node.arduino;

import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoGetPinValue extends AbstractNode {

	@NodeType
	private static final Arduino type = Arduino.READ_PIN;

	@NodeInput
	Pin pin;

	@NodeOutput("getValue")
	long value;

	public void getValue() {
		if (pin != null) {
			value = pin.getValue();
		}
	}

}
