package at.crimsonbit.nodesystem.node.arduino;

import java.io.IOException;

import org.firmata4j.IODevice;
import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.ArduinoPin;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoPinNode extends AbstractNode {

	@NodeType
	public static final ArduinoPin type = ArduinoPin.PIN;

	@NodeInput
	IODevice arduino;

	@NodeInput
	int pin;

	@NodeInput
	Pin.Mode pin_mode;

	@NodeInput
	long pin_value;

	@NodeOutput("getOutput")
	IODevice forward;

	public void getOutput() {
		if (arduino != null && pin_mode != null) {
			Pin p = arduino.getPin(pin);
			try {
				p.setMode(pin_mode);
				p.setValue(pin_value);
			} catch (IllegalArgumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			forward = arduino;
		}

	}

}
