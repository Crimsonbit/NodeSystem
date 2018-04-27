package at.crimsonbit.nodesystem.gui;

import java.util.Timer;
import java.util.TimerTask;

import at.crimsonbit.nodesystem.gui.animation.Animator;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * <h1>GLogPane</h1>
 * <p>
 * This is the log pane of the nodegraph. This pane is in the nodegraph by
 * default and is located at the top right hand corner of the nodegrpah.
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class GLogPane extends Pane {
	private Text logText;

	public GLogPane() {
		logText = new Text();
		logText.setFill(Color.WHITE);
		setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		// logText.relocate(getWidth(), getHeight());
		// setContent(logText);
		// setBlendMode(BlendMode.OVERLAY);
		this.getChildren().add(logText);

	}

	/**
	 * Returns the {@link Text} object of the LogPane.
	 * 
	 * @return the logging text object
	 */
	public Text getLogText() {
		return logText;
	}

	/**
	 * <h1>appendLog({@link String})</h1>
	 * <p>
	 * This method appends a log msg to the LogPane. It does not append but rather
	 * replace the message and animates it.
	 * </p>
	 * 
	 * @param msg
	 */
	public void appendLog(String msg) {
		Timer timer = new Timer(false);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Animator.animateProperty(getLogText().opacityProperty(), 1000, 50, 500, 1, 0);
				timer.cancel();
			}
		};

		timer.schedule(task, 1);
		logText.setText("\n" + msg);
	}
}