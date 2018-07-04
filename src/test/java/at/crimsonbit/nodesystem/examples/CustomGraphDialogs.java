package at.crimsonbit.nodesystem.examples;

import java.util.function.BiConsumer;

import at.crimsonbit.nodesystem.application.NodeGraphBuilder;
import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.dialog.GEntry;
import at.crimsonbit.nodesystem.gui.dialog.GSubMenu;
import at.crimsonbit.nodesystem.gui.settings.GGraphSettings;
import at.crimsonbit.nodesystem.gui.settings.GSettings;
import at.crimsonbit.nodesystem.gui.widget.toast.JFXToast;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastPosition;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastTime;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This example shows how to create custom graph dialogs.
 * 
 * @author Florian Wagner
 *
 */

public class CustomGraphDialogs extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeGraph graph = new NodeGraphBuilder(1275, 800, true, "English").registerAllModules("Modules/").build();

		/**
		 * Example of how to add custom node-classes to specific node types
		 */

		/**
		 * Example of how to change settings used in the node-system
		 * 
		 */
		GGraphSettings.getInstance().set(GSettings.SETTING_CURVE_WIDTH, 6d);
		GGraphSettings.getInstance().set(GSettings.SETTING_CURVE_CURVE, 100d);

		Scene scene = graph.getNodeScene();
		primaryStage.setScene(scene);
		primaryStage.show();

		/**
		 * Adding custom Menus to the graph.
		 */
		GSubMenu menu = new GSubMenu(3, "Example Menu");
		menu.addItem(1, "Example entry"); // adds an entry to the dialog

		BiConsumer<Integer, GEntry> consumer = (id, entry) -> {
			if (id == 1) {
				JFXToast.makeToast(primaryStage, "Example message!", ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
			}
		};

		graph.addCustomDialogEntry(menu, consumer);
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
