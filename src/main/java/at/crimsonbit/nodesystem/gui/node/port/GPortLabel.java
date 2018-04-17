package at.crimsonbit.nodesystem.gui.node.port;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * 
 * @author Florian Wagner
 *
 */

public class GPortLabel extends Text {

	public GPortLabel(double x, double y, String label, boolean input) {
		setText(label);
		setFill(Color.WHITE);
		int off = getText().length();

		if (!input)
			setX(x - (off * 7));
		else
			setX(x + (off + 6));
		setY(y + 6);

	}

}
