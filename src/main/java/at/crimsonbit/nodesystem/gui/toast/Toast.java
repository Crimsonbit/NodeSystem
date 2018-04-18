package at.crimsonbit.nodesystem.gui.toast;

import at.crimsonbit.nodesystem.gui.animation.Animator;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * @author Florian Wagner
 *
 */
public class Toast {

	private static Text tx_msg;
	private static Scene scene;

	public static void makeToast(Stage sc, String msg, ToastTime time) {
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

		root.getStylesheets().add(Toast.class.getResource("toaststyle.css").toExternalForm());

		root.setOpacity(0);

		stage.setScene(scene);
		stage.setX((sc.getX() + sc.getWidth() / 2));
		stage.setY((sc.getY() + sc.getHeight() / 2));
		stage.show();

		Animator.animateToast(stage, delay, inDelay, outDelay);
	}

}