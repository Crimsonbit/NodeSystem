package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum ArduinoPinMode implements INodeType {

	INPUT("INPUT Node"),
	/**
	 * Digital pin in output mode
	 */
	OUTPUT("OUTPUT Node"),
	/**
	 * Analog pin in analog input mode
	 */
	ANALOG("ANALOG Node"),
	/**
	 * Digital pin in PWM output mode
	 */
	PWM("PWM Node"),
	/**
	 * Digital pin in Servo output mode
	 */
	SERVO("SERVO Node"),
	/**
	 * shiftIn/shiftOut mode
	 */
	SHIFT("SHIFT Node"),
	/**
	 * Pin included in I2C setup
	 */
	I2C("I2C Node"),
	/**
	 * Pin configured for 1-wire
	 */
	ONEWIRE("ONEWIRE Node"),
	/**
	 * Pin configured for stepper motor
	 */
	STEPPER("STEPPER Node"),
	/**
	 * Pin configured for rotary encoders
	 */
	ENCODER("ENCODER Node"),
	/**
	 * Pin configured for serial communication
	 */
	SERIAL("SERIAL Node"),
	/**
	 * Enable internal pull-up resistor for pin
	 */
	PULLUP("PULLUP Node"),

	// add new modes here

	/**
	 * Indicates a mode that this client library doesn't support
	 */
	UNSUPPORTED("UNSUPPORTED Node"),
	/**
	 * Pin configured to be ignored by digitalWrite and capabilityResponse
	 */
	IGNORED("IGNORED Node");

	private String name;

	ArduinoPinMode(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
