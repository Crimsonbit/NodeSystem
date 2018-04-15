package at.crimsonbit.nodesystem.gui.node;

import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;

/**
 * 
 * @author NeonArtworks
 *
 */
public class GNodeConnection extends Group {

	protected GPort sourcePort;
	protected GPort targetPort;
	protected GNode source;
	protected GNode target;
	private CubicCurve line;

	private double width;

	public GNodeConnection(GPort sourcePort, GPort targetPort) {

		this.sourcePort = sourcePort;
		this.targetPort = targetPort;
		this.source = sourcePort.getNode();
		this.target = targetPort.getNode();

		source.addCellChild(target);
		target.addCellParent(source);

		line = new CubicCurve();

		line.startXProperty().bind(source.layoutXProperty().add(sourcePort.getX()));
		line.startYProperty().bind(source.layoutYProperty().add(sourcePort.getY()));
		line.setControlX1(sourcePort.getX() + 50);
		line.setControlY1(sourcePort.getY());
		line.setControlX2(targetPort.getY() - 50);
		line.setControlY2(targetPort.getY());
		line.controlX1Property().bind(source.layoutXProperty().add(sourcePort.getX()
				+ (double) source.getNodeGraph().getSettings().get(GraphSettings.SETTING_CURVE_CURVE)));
		line.controlY1Property().bind(source.layoutYProperty().add(sourcePort.getY()));
		line.controlX2Property().bind(target.layoutXProperty().add(targetPort.getX()
				- (double) source.getNodeGraph().getSettings().get(GraphSettings.SETTING_CURVE_CURVE)));
		line.controlY2Property().bind(target.layoutYProperty().add(targetPort.getY()));

		line.endXProperty().bind(target.layoutXProperty().add(targetPort.getX()));
		line.endYProperty().bind(target.layoutYProperty().add(targetPort.getY()));
		line.setStroke(source.getNodeGraph().getColorLookup().get(source.getNodeType()));
		line.setStrokeWidth((double) source.getNodeGraph().getSettings().get(GraphSettings.SETTING_CURVE_WIDTH));
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setFill(Color.TRANSPARENT);
		// line.setStrokeWidth(2);

		DropShadow e = new DropShadow();
		e.setBlurType(BlurType.GAUSSIAN);
		e.setBlurType(BlurType.GAUSSIAN);
		double col = (double) source.getNodeGraph().getSettings().get(GraphSettings.COLOR_SHADOW_COLOR);
		e.setColor(new Color(col, col, col, 1));
		e.setWidth((double) source.getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_WIDTH));
		e.setHeight((double) source.getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_HEIGHT));
		e.setOffsetX((double) source.getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_WIDTH));
		e.setOffsetY((double) source.getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_HEIGHT));
		e.setRadius((double) source.getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_RADIUS));
		line.setEffect(e);

		getChildren().add(line);

	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public GNode getSource() {
		return source;
	}

	public GNode getTarget() {
		return target;
	}

	public GPort getSourcePort() {
		return sourcePort;
	}

	public GPort getTargetPort() {
		return targetPort;
	}

}