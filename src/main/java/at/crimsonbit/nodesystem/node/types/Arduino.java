package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum Arduino implements INodeType {

	DEVICE("Device Node"), PORT("Port Node"), CLOSER("Device-Closer Node"), LISTENER("Arduino-Listener Node"), PIN(
			"Pin-Modifier Node"), READ_PIN("Read-Pin Node"), PIN_GET("Get-Pin Node"), PIN_SET_MODE(
					"Pin-Mode Node"), PIN_SET_VALUE("Pin-Value Node"), I2CDevice(
							"I2C Device Node"), I2CTRANSMIT("I2C Send Node"), I2CRECEIVE("I2C Receive Node");
	private String name;

	Arduino(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
