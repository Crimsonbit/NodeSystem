package at.crimsonbit.nodesystem.gui;

import java.time.Instant;
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

public class GLogPane extends Pane {
	private Text logText;
	private Instant last = Instant.now();

	public GLogPane() {
		logText = new Text();
		logText.setFill(Color.WHITE);
		setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		// logText.relocate(getWidth(), getHeight());
		// setContent(logText);
		// setBlendMode(BlendMode.OVERLAY);
		this.getChildren().add(logText);

	}

	public Text getLogText() {
		return logText;
	}

	public void setLogText(Text logText) {
		this.logText = logText;
	}

	public void appendLog(String msg) {
		Instant now = Instant.now();
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
		last = now;

	}

}
