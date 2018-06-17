package at.crimsonbit.nodesystem.node.arduino;

import java.io.IOException;

import org.firmata4j.I2CDevice;
import org.firmata4j.I2CEvent;
import org.firmata4j.I2CListener;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoI2CAskNode extends AbstractNode {

	@NodeType
	private static final Arduino type = Arduino.I2CRECEIVE;

	@NodeInput
	I2CDevice i2cDevice;

	@NodeInput
	byte length;

	@NodeInput
	int register;

	@NodeOutput("getOutput")
	I2CDevice i2c;

	public void getOutput() {
		if (i2cDevice != null) {
			try {
				i2cDevice.ask(register, length, new I2CListener() {

					@Override
					public void onReceive(I2CEvent event) {
						System.out.println("Device: " + event.getDevice());
						System.out.println("Register: " + event.getRegister());
						System.out.println("Data: " + event.getData());

					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}