package at.crimsonbit.nodesystem.gui.node.port;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * 
 * @author Florian Wagner
 *
 */

public class GPortLabel extends Text {
	private boolean input;
	private String label;
	private double ix;
	private double iy;

	public GPortLabel(double x, double y, String label, boolean input) {
		setText(label);
		setFill(Color.WHITE);
		this.label = label;
		this.input = input;
		this.ix = x;
		this.iy = y;
		draw();
	}

	public void draw() {
		int off = getText().length();

		if (!input)
			setX(ix - (off * 7));
		else
			setX(ix + (off + 6));
		setY(iy + 6);
	}

	public double getIx() {
		return ix;
	}

	public void setIx(double ix) {
		this.ix = ix;
		draw();
	}

	public double getIy() {
		return iy;
	}

	public void setIy(double iy) {
		this.iy = iy;

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
