package at.crimsonbit.nodesystem.node.arduino;

import java.io.IOException;

import org.firmata4j.IODevice;
import org.firmata4j.firmata.FirmataDevice;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoDeviceNode extends AbstractNode {

	@NodeType
	public static final Arduino type = Arduino.DEVICE;

	@NodeField
	@NodeInput
	String port;

	@NodeOutput("openDevice")
	IODevice arduino;

	public ArduinoDeviceNode() {

	}	
	
	public void openDevice() {
		if (arduino == null) {
			if (port != null) {
				arduino = new FirmataDevice(port);
				try {
					arduino.start();
					arduino.ensureInitializationIsDone();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
