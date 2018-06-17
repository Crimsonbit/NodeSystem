package at.crimsonbit.nodesystem.node.arduino;

import java.io.IOException;

import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoPinSetModeNode extends AbstractNode {

	@NodeType
	private static Arduino type = Arduino.PIN_SET_MODE;

	@NodeInput
	Pin pin;

	@NodeInput
	Pin.Mode mode;

	@NodeOutput("getOutput")
	Pin forward;

	public void getOutput() {
		if (pin != null) {
			if (mode != null) {
				try {
					pin.setMode(mode);
					forward = pin;
				} catch (IllegalArgumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}
