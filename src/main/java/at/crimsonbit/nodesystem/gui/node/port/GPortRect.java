package at.crimsonbit.nodesystem.gui.node.port;

import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Florian Wagner
 *
 */

public class GPortRect extends Rectangle {

	private GNode node;
	private double x;
	private double y;
	private boolean input;
	private Color inputColor;
	private Color outputColor;

	public void redraw() {

		setX(x);
		setY(y);
		setWidth(6);
		setHeight(6);
		if (input)
			setFill(this.inputColor);
		else
			setFill(this.outputColor);

		// setStroke(Color.LIGHTSKYBLUE);
		setArcWidth(20.0);
		setArcHeight(20.0);
		setStrokeWidth(1);
	}

	public GPortRect(double x, double y, boolean input, GNode node) {
		this.node = node;
		this.x = x;
		this.y = y;
		this.input = input;
		this.inputColor = node.getNodeGraph().getGeneralColorLookup().get(GraphSettings.COLOR_PORT_INPUT);
		this.outputColor = node.getNodeGraph().getGeneralColorLookup().get(GraphSettings.COLOR_PORT_OUTPUT);
		redraw();
	}

	public Color getInputColor() {
		return inputColor;
	}

	public void setInputColor(Color inputColor) {
		this.inputColor = inputColor;
	}

	public Color getOutputColor() {
		return outputColor;
	}

	public void setOutputColor(Color outputColor) {
		this.outputColor = outputColor;
	}

	public GNode getNode() {
		return node;
	}

	public void setNode(GNode node) {
		this.node = node;
	}

	public double getRX() {
		return x;
	}

	public void setRX(double x) {
		this.x = x;
	}

	public double getRY() {
		return y;
	}

	public void setRY(double y) {
		this.y = y;
	}

	public boolean isInput() {
		return input;
	}

	public void setInput(boolean input) {
		this.input = input;
	}

}
