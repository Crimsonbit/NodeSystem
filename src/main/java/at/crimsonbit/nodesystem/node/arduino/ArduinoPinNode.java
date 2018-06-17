package at.crimsonbit.nodesystem.node.arduino;

import java.io.IOException;

import org.firmata4j.IODevice;
import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoPinNode extends AbstractNode {

	@NodeType
	public static final Arduino type = Arduino.PIN;

	@NodeInput
	IODevice arduino;

	@NodeInput
	Pin.Mode pin_mode;

	@NodeInput
	long pin_value;

	@NodeInput
	int pin_number;

	@NodeOutput("getOutput")
	Pin forward;

	public ArduinoPinNode() {
	}

	public void getOutput() {
		if (arduino != null) {
			if (pin_number > 0 && pin_number < arduino.getPinsCount()) {
				Pin p = arduino.getPin(pin_number);
				try {
					if (pin_mode != null) {

						p.setMode(pin_mode);

						if (p.getMode() != Pin.Mode.ANALOG && p.getMode() != Pin.Mode.INPUT)
							p.setValue(pin_value);
					}
				} catch (IllegalArgumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				forward = p;
			}
		}

	}

}
