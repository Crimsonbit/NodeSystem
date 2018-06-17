package at.crimsonbit.nodesystem.node.arduino;

import org.firmata4j.IODevice;
import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;

import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ArduinoListenerNode extends AbstractNode {

	@NodeType
	public static final Arduino type = Arduino.LISTENER;

	@NodeInput
	IODevice dev;

	@NodeOutput("onStart")
	IODevice arduinoDevice;

	public void onStart() {
		if (dev != null) {
			dev.addEventListener(new IODeviceEventListener() {
				@Override
				public void onStart(IOEvent event) {
					// since this moment we are sure that the device is initialized
					// so we can hide initialization spinners and begin doing cool stuff
					System.out.println("Device is ready");
				}

				@Override
				public void onStop(IOEvent event) {
					// since this moment we are sure that the device is properly shut down
					System.out.println("Device has been stopped");
				}

				@Override
				public void onPinChange(IOEvent event) {
					// here we react to changes of pins' state
					Pin pin = event.getPin();
					System.out.println(String.format("Pin %d got a value of %d", pin.getIndex(), pin.getValue()));
				}

				@Override
				public void onMessageReceive(IOEvent event, String message) {
					// here we react to receiving a text message from the device
					System.out.println(message);
				}
			});
		}
	}

}
