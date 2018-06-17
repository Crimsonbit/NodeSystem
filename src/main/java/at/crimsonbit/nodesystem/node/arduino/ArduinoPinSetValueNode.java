package at.crimsonbit.nodesystem.node.arduino;

import java.io.IOException;

import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoPinSetValueNode extends AbstractNode {

	@NodeType
	private static Arduino type = Arduino.PIN_SET_VALUE;

	@NodeInput
	Pin pin;

	@NodeInput
	long value;

	@NodeOutput("getOutput")
	Pin forward;

	public void getOutput() {
		if (pin != null) {
			try {

				if (pin.getMode() != Pin.Mode.ANALOG && pin.getMode() != Pin.Mode.INPUT)
					pin.setValue(value);
				forward = pin;
			} catch (IllegalArgumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}
}