package at.crimsonbit.nodesystem.gui.node;

import at.crimsonbit.nodesystem.gui.color.GColors;
import at.crimsonbit.nodesystem.gui.color.GTheme;
import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.gui.settings.GGraphSettings;
import at.crimsonbit.nodesystem.gui.settings.GSettings;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;

/**
 * <h1>GNodeConnection extends {@link Group}</h1>
 * <p>
 * This class represents a connection between two ports.
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class GNodeConnection extends Group {

	protected GPort sourcePort;
	protected GPort targetPort;
	protected GNode source;
	protected GNode target;
	private CubicCurve line;
	private Tooltip tTip = new Tooltip();
	private double width;
	private final int MAGIC_OFFSET = 3;
	private GGraphSettings inst = GGraphSettings.getInstance();
	/**
	 * Initializes a new connecten
	 * 
	 * @param sourcePort
	 *            should be the ouput of a node
	 * @param targetPort
	 *            should be the input of a node
	 */
	public GNodeConnection(GPort sourcePort, GPort targetPort) {

		this.sourcePort = sourcePort;
		this.targetPort = targetPort;
		this.source = sourcePort.getNode();
		this.target = targetPort.getNode();
		source.addNodeChildren(target);
		target.addNodeParent(source);
		tTip.setText("Source: " + sourcePort.getStringID() + "\nTarge: " + targetPort.getStringID());
		draw();
		getChildren().add(line);
	//	Tooltip.install(this, tTip);
	}

	public void resetSourcePort(GPort source) {
		this.sourcePort = source;
	}

	/**
	 * Draws the connection to the graph
	 */
	public void draw() {
		line = new CubicCurve();
		line.startXProperty().bind(source.layoutXProperty().add(sourcePort.getPortX() + MAGIC_OFFSET));
		line.startYProperty().bind(source.layoutYProperty().add(sourcePort.getY() + MAGIC_OFFSET));
		line.setControlX1(sourcePort.getPortX() + 50);
		line.setControlY1(sourcePort.getY());
		line.setControlX2(targetPort.getPortX() - 50);
		line.setControlY2(targetPort.getY());
		line.controlX1Property().bind(source.layoutXProperty().add(sourcePort.getPortX()
				+ (double) inst.getSetting(GSettings.SETTING_CURVE_CURVE)));
		line.controlY1Property().bind(source.layoutYProperty().add(sourcePort.getY()));
		line.controlX2Property().bind(target.layoutXProperty().add(targetPort.getPortX()
				- (double) inst.getSetting(GSettings.SETTING_CURVE_CURVE)));
		line.controlY2Property().bind(target.layoutYProperty().add(targetPort.getY()));

		line.endXProperty().bind(target.layoutXProperty().add(targetPort.getPortX() + MAGIC_OFFSET));
		line.endYProperty().bind(target.layoutYProperty().add(targetPort.getY() + MAGIC_OFFSET));
		line.setStroke(source.getNodeType().getColor());
		line.setStrokeWidth((double) inst.getSetting(GSettings.SETTING_CURVE_WIDTH));
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setFill(Color.TRANSPARENT);

		DropShadow e = new DropShadow();
		e.setBlurType(BlurType.GAUSSIAN);
		e.setBlurType(BlurType.GAUSSIAN);
	
		e.setColor(GTheme.getInstance().getColor(GColors.COLOR_SHADOW_COLOR));
		e.setWidth((double) inst.getSetting(GSettings.SETTING_SHADOW_WIDTH));
		e.setHeight((double) inst.getSetting(GSettings.SETTING_SHADOW_HEIGHT));
		e.setOffsetX((double) inst.getSetting(GSettings.SETTING_SHADOW_WIDTH));
		e.setOffsetY((double) inst.getSetting(GSettings.SETTING_SHADOW_HEIGHT));
		e.setRadius((double) inst.getSetting(GSettings.SETTING_SHADOW_RADIUS));
		line.setEffect(e);
	}

	/**
	 * Draws the connection on a specific y position. This is needed for the node
	 * toggling.
	 * 
	 * @param y
	 *            the y position
	 */
	public void draw(double y) {
		getChildren().remove(line);
		line = new CubicCurve();

		line.startXProperty().bind(source.layoutXProperty().add(sourcePort.getPortX() + MAGIC_OFFSET));
		line.startYProperty().set(y);

		line.setControlX1(sourcePort.getPortX() + 50);
		line.setControlY1(y);

		line.setControlX2(targetPort.getPortX() - 50);
		line.setControlY2(targetPort.getY());

		line.controlX1Property().bind(source.layoutXProperty().add(sourcePort.getPortX()
				+ (double) inst.getSetting(GSettings.SETTING_CURVE_CURVE)));
		line.controlY1Property().set(y);

		line.controlX2Property().bind(target.layoutXProperty().add(targetPort.getPortX()
				- (double) inst.getSetting(GSettings.SETTING_CURVE_CURVE)));
		line.controlY2Property().bind(target.layoutYProperty().add(targetPort.getY()));

		line.endXProperty().bind(target.layoutXProperty().add(targetPort.getPortX() + MAGIC_OFFSET));
		line.endYProperty().bind(target.layoutYProperty().add(targetPort.getY() + MAGIC_OFFSET));

		line.setStroke(source.getNodeType().getColor());
		line.setStrokeWidth((double) inst.getSetting(GSettings.SETTING_CURVE_WIDTH));
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setFill(Color.TRANSPARENT);

		DropShadow e = new DropShadow();
		e.setBlurType(BlurType.GAUSSIAN);
		e.setBlurType(BlurType.GAUSSIAN);
		e.setColor(GTheme.getInstance().getColor(GColors.COLOR_SHADOW_COLOR));
		e.setWidth((double) inst.getSetting(GSettings.SETTING_SHADOW_WIDTH));
		e.setHeight((double) inst.getSetting(GSettings.SETTING_SHADOW_HEIGHT));
		e.setOffsetX((double) inst.getSetting(GSettings.SETTING_SHADOW_WIDTH));
		e.setOffsetY((double) inst.getSetting(GSettings.SETTING_SHADOW_HEIGHT));
		e.setRadius((double) inst.getSetting(GSettings.SETTING_SHADOW_RADIUS));
		line.setEffect(e);
		// getChildren().add(line);
		//System.out.println(getChildren());
	}

	/**
	 * Updates the connection ports
	 * 
	 * @param p1
	 *            output port
	 * @param p2
	 *            input port
	 */
	public void update(GPort p1, GPort p2) {
		this.sourcePort = p1;
		this.targetPort = p2;
		getChildren().remove(line);
		draw();
		getChildren().add(line);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Returns the source node of the connection (output)
	 * 
	 * @return the source node
	 */
	public GNode getSource() {
		return source;
	}

	/**
	 * Returns the target node of the connection (input)
	 * 
	 * @return the target node
	 */
	public GNode getTarget() {
		return target;
	}
	/**
	 * Returns the source port of the connections source node (output)
	 * 
	 * @return the source port
	 */
	public GPort getSourcePort() {
		return sourcePort;
	}

	/**
	 * Returns the target port of the connections target node (input)
	 * 
	 * @return the target port
	 */
	public GPort getTargetPort() {
		return targetPort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((sourcePort == null) ? 0 : sourcePort.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((targetPort == null) ? 0 : targetPort.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GNodeConnection other = (GNodeConnection) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (sourcePort == null) {
			if (other.sourcePort != null)
				return false;
		} else if (!sourcePort.equals(other.sourcePort))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (targetPort == null) {
			if (other.targetPort != null)
				return false;
		} else if (!targetPort.equals(other.targetPort))
			return false;
		return true;
	}

}