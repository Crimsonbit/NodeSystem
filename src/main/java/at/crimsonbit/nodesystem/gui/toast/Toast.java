package at.crimsonbit.nodesystem.gui.toast;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Toast {

	private static Text tx_msg;
	private static Scene scene;
	private static Timeline intTimeline = new Timeline();
	private static Timeline outTImeline = new Timeline();

	public static void makeToast(String msg, ToastTime time) {
		int delay = 0, inDelay = 0, outDelay = 0;

		if (time.equals(ToastTime.TIME_LONG)) {
			delay = 4000;
			inDelay = 500;
			outDelay = 500;
		} else if (time.equals(ToastTime.TIME_SHORT)) {
			delay = 1500;
			inDelay = 250;
			outDelay = 250;
		}

		Stage stage = new Stage();
		tx_msg = new Text(msg);
		StackPane root = new StackPane(tx_msg);
		scene = new Scene(root);

		stage.setResizable(false);

		stage.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(Color.TRANSPARENT);
		tx_msg.setFill(Color.WHITE);

		root.setStyle(Toast.class.getResource("toaststyle.css").toExternalForm());

		root.setOpacity(0);

		stage.setScene(scene);
		stage.show();

		animate(stage, delay, inDelay, outDelay);
	}

	private static void animate(Stage stage, int delay, int inDelay, int outDelay) {
		KeyFrame inKey = new KeyFrame(Duration.millis(inDelay),
				new KeyValue(stage.getScene().getRoot().opacityProperty(), 1));

		intTimeline.getKeyFrames().add(inKey);
		intTimeline.setOnFinished((actionEvent) -> {
			new Thread(() -> {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				KeyFrame outKey = new KeyFrame(Duration.millis(outDelay),
						new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));

				outTImeline.getKeyFrames().add(outKey);
				outTImeline.setOnFinished((finishActionEvent) -> stage.close());
				outTImeline.play();

			}).start();
		});

		intTimeline.play();
	}
}