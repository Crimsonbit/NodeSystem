package at.crimsonbit.nodesystem.gui.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 * @author NeonArtworks
 *
 */
public class Animator {
	private static Timeline intTimeline = new Timeline();
	private static Timeline outTImeline = new Timeline();

	private static void resetTimeline() {
		intTimeline = new Timeline();
		outTImeline = new Timeline();
	}

	public static void animateToast(Stage stage, int duration, int startDuration, int endDuration) {
		resetTimeline();
		KeyFrame inKey = new KeyFrame(Duration.millis(startDuration),
				new KeyValue(stage.getScene().getRoot().opacityProperty(), 1));

		intTimeline.getKeyFrames().add(inKey);
		intTimeline.setOnFinished((actionEvent) -> {
			new Thread(() -> {
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				KeyFrame outKey = new KeyFrame(Duration.millis(endDuration),
						new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));

				outTImeline.getKeyFrames().add(outKey);
				outTImeline.setOnFinished((finishActionEvent) -> stage.close());
				outTImeline.play();

			}).start();
		});

		intTimeline.play();

	}

	public static void animateProperty(DoubleProperty prop, int duartion, int startDuration, int endDuration,
			double firstVal, double secondVal) {
		resetTimeline();
		KeyFrame inKey = new KeyFrame(Duration.millis(startDuration), new KeyValue(prop, firstVal));

		intTimeline.getKeyFrames().add(inKey);
		intTimeline.setOnFinished((actionEvent) -> {
			new Thread(() -> {
				try {
					Thread.sleep(duartion);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				KeyFrame outKey = new KeyFrame(Duration.millis(endDuration), new KeyValue(prop, secondVal));
				outTImeline.getKeyFrames().add(outKey);
				outTImeline.play();

			}).start();
		});

		intTimeline.play();
	}
}
