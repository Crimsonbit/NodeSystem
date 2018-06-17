package at.crimsonbit.nodesystem.node.arduino;

import java.io.IOException;

import org.firmata4j.IODevice;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoDeviceCloseNode extends AbstractNode {

	@NodeType
	private static final Arduino type = Arduino.CLOSER;
	boolean stopped = false;

	@NodeInput
	IODevice arduino;

	@NodeOutput("closeArduino")
	IODevice forward;

	public void closeArduino() {
		if (arduino != null) {
			if (arduino.isReady() && !stopped) {
				try {
					arduino.stop();
					stopped = true;
					forward = arduino;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
