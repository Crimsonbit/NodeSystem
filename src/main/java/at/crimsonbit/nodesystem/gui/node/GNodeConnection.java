package at.crimsonbit.nodesystem.gui.node;

import at.crimsonbit.nodesystem.gui.node.port.GPort;
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
@SuppressWarnings({ "restriction", "unused" })
public class GNodeConnection extends Group {

	protected GPort sourcePort;
	protected GPort targetPort;
	protected GNode source;
	protected GNode target;
	private CubicCurve line;

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
		line.controlX1Property().bind(source.layoutXProperty().add(sourcePort.getX() + 50));
		line.controlY1Property().bind(source.layoutYProperty().add(sourcePort.getY()));
		line.controlX2Property().bind(target.layoutXProperty().add(targetPort.getX() - 50));
		line.controlY2Property().bind(target.layoutYProperty().add(targetPort.getY()));

		line.endXProperty().bind(target.layoutXProperty().add(targetPort.getX()));
		line.endYProperty().bind(target.layoutYProperty().add(targetPort.getY()));
		line.setStroke(source.getNodeGraph().getNodeColorLookup().get("curve"));
		line.setStrokeWidth(4);
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setFill(Color.TRANSPARENT);
		line.setStrokeWidth(2);

		DropShadow e = new DropShadow();
		e.setBlurType(BlurType.GAUSSIAN);
		e.setColor(new Color(0.1, 0.1, 0.1, 1));
		e.setWidth(5);
		e.setHeight(5);
		e.setOffsetX(5);
		e.setOffsetY(5);
		e.setRadius(10);
		line.setEffect(e);

		getChildren().add(line);

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