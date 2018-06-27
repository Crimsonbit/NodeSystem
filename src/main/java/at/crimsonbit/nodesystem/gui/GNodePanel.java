package at.crimsonbit.nodesystem.gui;

import at.crimsonbit.nodesystem.gui.animation.Animator;
import at.crimsonbit.nodesystem.gui.color.GColors;
import at.crimsonbit.nodesystem.gui.color.GTheme;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class GNodePanel extends AnchorPane {

	private GNodeGraph graph;
	private Rectangle nodePane;
	private int state = 0;
	private final double CIRCLE_SIZE = 30d;
	private final double RADIUS_DEFAULT = 40;
	private final double RADIUS_OUT = 70;
	private final double CIRCLE_POS_X = 0;

	public GNodePanel(GNodeGraph graph) {
		this.graph = graph;
		draw();
	}

	public void draw() {
		nodePane = new Rectangle(500, 0);
		nodePane.setTranslateX(-500);
		nodePane.heightProperty().bind(graph.heightProperty());
		nodePane.widthProperty().bind(graph.widthProperty().divide(3));
		Color c = GTheme.getInstance().getColor(GColors.COLOR_BACKGROUND).darker();
		c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 0.5);
		nodePane.setFill(c);
		Arc arc = new Arc(CIRCLE_POS_X, 0, RADIUS_DEFAULT, RADIUS_DEFAULT, 0, 180);
		arc.setRotate(90);
		arc.centerYProperty().bind(graph.heightProperty().divide(2));
		arc.setType(ArcType.OPEN);
		arc.setStrokeWidth(10);
		arc.setStrokeType(StrokeType.INSIDE);

		arc.setOnMouseEntered(event -> {
			if (graph.getState().equals(GState.DEFAULT)) {
				if (this.state == 0) {
					Animator.animateProperty(arc.radiusXProperty(), 0, 0, 20, RADIUS_DEFAULT, RADIUS_OUT);
					Animator.animateProperty(arc.radiusYProperty(), 0, 0, 20, RADIUS_DEFAULT, RADIUS_OUT);

				}
				if (event.isPrimaryButtonDown()) {
					removeView(arc);
					nodePane.setTranslateX(0);
					this.state = 1;
				}
			}
		});
		
		arc.setOnMouseExited(event -> {
			if (graph.getState().equals(GState.DEFAULT)) {
				if (this.state == 0) {
					Animator.animateProperty(arc.radiusXProperty(), 0, 0, 20, RADIUS_OUT, RADIUS_DEFAULT);
					Animator.animateProperty(arc.radiusYProperty(), 0, 0, 20, RADIUS_DEFAULT, RADIUS_OUT);

				}

			}
		});
		addView(nodePane);
		addView(arc);

	}

	public void addView(Node n) {
		getChildren().add(n);
	}

	public void removeView(Node n) {
		getChildren().remove(n);
	}

}
