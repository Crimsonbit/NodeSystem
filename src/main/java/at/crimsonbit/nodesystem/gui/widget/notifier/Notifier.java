package at.crimsonbit.nodesystem.gui.widget.notifier;

import at.crimsonbit.nodesystem.gui.animation.Animator;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Notifier {

	private static Text tx_msg;
	private static Scene scene;

	public static void notify(Stage sc, String msg) {
		int delay = 1500, inDelay = 200, outDelay = 200;
		Stage stage = new Stage();
		// stage.initOwner(sc);
		stage.setResizable(false);

		tx_msg = new Text(msg);
		StackPane root = new StackPane(tx_msg);
		scene = new Scene(root, 400, 125);

		stage.setResizable(false);

		stage.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(Color.TRANSPARENT);
		tx_msg.setFill(Color.WHITE);

		root.setOpacity(0);
		stage.setScene(scene);
		stage.show();

		// stage.setX(stage.getWidth());
		// stage.setY(stage.getHeight());

		Animator.animateToast(stage, delay, inDelay, outDelay);
	}
}
