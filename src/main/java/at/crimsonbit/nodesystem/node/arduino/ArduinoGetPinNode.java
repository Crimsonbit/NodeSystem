package at.crimsonbit.nodesystem.node.arduino;

import org.firmata4j.IODevice;
import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoGetPinNode extends AbstractNode {

	@NodeType
	private static final Arduino type = Arduino.PIN_GET;

	@NodeInput
	IODevice arduino;

	@NodeInput
	int pin_number;

	@NodeOutput("getOutput")
	Pin pin;

	public void getOutput() {
		if (arduino != null) {
			if (pin_number > 0 && pin_number < arduino.getPinsCount()) {
				pin = arduino.getPin(pin_number);
			}
		}
	}

}
