package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum ArduinoPin implements INodeType {

	PIN("Arduino-Pin Node"), MODE("Arudino-Pin-Mode Node"),

	INPUT("PinMode-INPUT Node"),
	/**
	 * Digital pin in output mode
	 */
	OUTPUT("PinMode-OUTPUT Node"),
	/**
	 * Analog pin in analog input mode
	 */
	ANALOG("PinMode-ANALOG Node"),
	/**
	 * Digital pin in PWM output mode
	 */
	PWM("PinMode-PWM Node"),
	/**
	 * Digital pin in Servo output mode
	 */
	SERVO("PinMode-SERVO Node"),
	/**
	 * shiftIn/shiftOut mode
	 */
	SHIFT("PinMode-SHIFT Node"),
	/**
	 * Pin included in I2C setup
	 */
	I2C("PinMode-I2C Node"),
	/**
	 * Pin configured for 1-wire
	 */
	ONEWIRE("PinMode-ONEWIRE Node"),
	/**
	 * Pin configured for stepper motor
	 */
	STEPPER("PinMode-STEPPER Node"),
	/**
	 * Pin configured for rotary encoders
	 */
	ENCODER("PinMode-ENCODER Node"),
	/**
	 * Pin configured for serial communication
	 */
	SERIAL("PinMode-SERIAL Node"),
	/**
	 * Enable internal pull-up resistor for pin
	 */
	PULLUP("PinMode-PULLUP Node"),

	// add new modes here

	/**
	 * Indicates a mode that this client library doesn't support
	 */
	UNSUPPORTED("PinMode-UNSUPPORTED Node"),
	/**
	 * Pin configured to be ignored by digitalWrite and capabilityResponse
	 */
	IGNORED("PinMode-IGNORED Node");

	private String name;

	ArduinoPin(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
