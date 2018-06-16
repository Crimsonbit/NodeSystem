package at.crimsonbit.nodesystem.examples;

import java.net.URL;
import java.util.ResourceBundle;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GraphFromFXMLExample extends Application implements Initializable {

	/**
	 * Globals
	 */
	private Scene scene;
	private AnchorPane root = null;

	/**
	 * Controller Part
	 */
	@FXML
	private GNodeGraph id_nodeView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id_nodeView.initGraph(true);

	}

	/**
	 * Application Part
	 */
	private double width = 540;
	private double height = 430;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Node Graph");
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/at/crimsonbit/nodesystem/examples/fxmlexample.fxml"));
		root = loader.load();
		scene = new Scene(root, width, height);
		
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
