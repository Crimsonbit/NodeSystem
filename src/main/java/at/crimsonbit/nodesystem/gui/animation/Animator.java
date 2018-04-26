package at.crimsonbit.nodesystem.gui.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Helper class to animate properties.
 * 
 * @author Florian Wagner
 *
 */
public class Animator {
	private static Timeline intTimeline = new Timeline();
	private static Timeline outTImeline = new Timeline();
	private static boolean isFinished = true;

	private static void resetTimeline() {
		intTimeline = new Timeline();
		outTImeline = new Timeline();
		isFinished = false;
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
				isFinished = true;
			}).start();
		});

		intTimeline.play();

	}

	/**
	 * Animates any double property.
	 * 
	 * @param prop
	 *            the property you want to animate
	 * @param holdTime
	 *            the duration after startDuration before stopDuration.
	 * @param startDuration
	 *            the duration to reach the first value
	 * @param endDuration
	 *            the duration after the holdTime.
	 * @param firstVal
	 *            the first keyframe value
	 * @param secondVal
	 *            the second keyframe value
	 */
	public static void animateProperty(DoubleProperty prop, int holdTime, int startDuration, int endDuration,
			double firstVal, double secondVal) {

		if (isFinished) {
			resetTimeline();
			KeyFrame inKey = new KeyFrame(Duration.millis(startDuration), new KeyValue(prop, firstVal));

			intTimeline.getKeyFrames().add(inKey);
			intTimeline.setOnFinished((actionEvent) -> {
				new Thread(() -> {
					try {
						Thread.sleep(holdTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					KeyFrame outKey = new KeyFrame(Duration.millis(endDuration), new KeyValue(prop, secondVal));
					outTImeline.getKeyFrames().add(outKey);
					outTImeline.play();
					isFinished = true;
				}).start();
			});

			intTimeline.play();
		}
	}

	public static boolean isFinished() {
		return isFinished;
	}

}
