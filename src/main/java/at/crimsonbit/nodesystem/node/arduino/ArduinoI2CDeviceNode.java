package at.crimsonbit.nodesystem.node.arduino;

import java.io.IOException;

import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoI2CDeviceNode extends AbstractNode {

	@NodeType
	private static final Arduino type = Arduino.I2CDevice;

	@NodeInput
	IODevice arduino;

	@NodeInput
	byte address;

	@NodeOutput("getOutput")
	I2CDevice i2c;

	public void getOutput() {
		if (arduino != null) {
			try {
				i2c = arduino.getI2CDevice(address);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
