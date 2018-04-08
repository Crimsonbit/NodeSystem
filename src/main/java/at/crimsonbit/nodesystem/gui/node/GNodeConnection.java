package at.crimsonbit.nodesystem.gui.node;

import at.crimsonbit.nodesystem.gui.node.port.GPort;
import javafx.scene.Group;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
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
	private Line line;

	public GNodeConnection(GPort sourcePort, GPort targetPort) {

		this.sourcePort = sourcePort;
		this.targetPort = targetPort;
		this.source = sourcePort.getNode();
		this.target = targetPort.getNode();

		source.addCellChild(target);
		target.addCellParent(source);

		line = new Line();

		line.startXProperty().bind(source.layoutXProperty().add(sourcePort.getX()));
		line.startYProperty().bind(source.layoutYProperty().add(sourcePort.getY()));

		line.endXProperty().bind(target.layoutXProperty().add(targetPort.getX()));
		line.endYProperty().bind(target.layoutYProperty().add(targetPort.getY()));

		// line.setStartX(sourcePort.getX());
		// line.setStartY(sourcePort.getY());
		// line.setEndX(targetPort.getX());
		// line.setEndY(targetPort.getY());
		line.setStrokeWidth(2);
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