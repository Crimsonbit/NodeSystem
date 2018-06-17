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
	IODevice arduinoDevice;

	public void openDevice() {
		if (arduinoDevice == null) {
			if (port != null) {
				arduinoDevice = new FirmataDevice(port);
				try {
					arduinoDevice.start();
					arduinoDevice.ensureInitializationIsDone();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
