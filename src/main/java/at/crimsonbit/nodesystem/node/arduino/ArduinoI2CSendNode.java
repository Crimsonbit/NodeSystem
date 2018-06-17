package at.crimsonbit.nodesystem.node.arduino;

import java.io.IOException;

import org.firmata4j.I2CDevice;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoI2CSendNode extends AbstractNode {

	@NodeType
	private static final Arduino type = Arduino.I2CTRANSMIT;

	@NodeInput
	I2CDevice i2cDevice;

	@NodeInput
	byte data;

	@NodeOutput("getOutput")
	I2CDevice i2c;

	public void getOutput() {
		if (i2cDevice != null) {
			try {
				i2cDevice.tell(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}