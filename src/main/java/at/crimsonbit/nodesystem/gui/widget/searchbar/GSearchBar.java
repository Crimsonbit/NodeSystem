package at.crimsonbit.nodesystem.gui.widget.searchbar;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * A SearchBar to access the nodes easier.
 * 
 * @author Florian Wagner
 *
 */
public class GSearchBar {

	private static Scene scene;
	private static Stage stage;
	private String search = new String("");
	private GNodeGraph graph;
	private boolean open = false;

	private TextField idd_search_text;
	private ComboBox<String> idd_search_res;

	public GSearchBar() {

	}

	public GSearchBar(GNodeGraph graph) {
		this.graph = graph;
	}

	public String getSearch() {
		return search;
	}

	public void setGraph(GNodeGraph graph) {
		this.graph = graph;
	}

	public void search(Stage sc, GNodeGraph graph) {
		open = true;
		this.graph = graph;
		stage = new Stage();
		stage.initOwner(sc);
		stage.setResizable(false);

		AnchorPane root = new AnchorPane();
		idd_search_res = new ComboBox<String>();

		root.setPrefWidth(350);
		root.setPrefHeight(45);

		idd_search_text = new TextField("Search...");

		root.setBottomAnchor(idd_search_text, 0d);
		root.setTopAnchor(idd_search_text, 0d);
		root.setLeftAnchor(idd_search_text, 0d);
		root.setRightAnchor(idd_search_text, 0d);
		root.getChildren().add(idd_search_text);
		root.getChildren().add(idd_search_res);

		root.setRightAnchor(idd_search_res, 0d);

		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("searchbar.css").toExternalForm());
		// stage.setResizable(false);
		scene.setFill(Color.TRANSPARENT);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.show();

		stage.setX((sc.getX() + sc.getWidth() / 2) - stage.getWidth() / 2);
		stage.setY((sc.getY() + sc.getHeight() / 1.1) - stage.getHeight() / 2);
		idd_search_res.relocate(300, 10);
		idd_search_text.setOnKeyPressed(onKeyPressedEventHandler);
		idd_search_res.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				idd_search_text.setText(newValue);
				idd_search_text.requestFocus();
			}
		});
	}

	private void reFillComboBox() {
		idd_search_res.getItems().removeAll(idd_search_res.getItems());
		for (String s : graph.getGuiMaster().getNodeMaster().getStringToTypeMap().keySet()) {
			if (idd_search_text.getText() != null)
				if (s.toLowerCase().contains(idd_search_text.getText().toLowerCase()))
					idd_search_res.getItems().add(s);
		}
	}

	private EventHandler<KeyEvent> onKeyPressedEventHandler = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {

			if (event.getCode().equals(KeyCode.SPACE) && event.isControlDown()) {
				if (idd_search_res.getItems().size() == 1) {
					idd_search_text.setText(idd_search_res.getItems().get(0));
				}
			}

			if (event.getCode().equals(KeyCode.ESCAPE)) {
				stage.close();
				open = false;
				return;
			}

			if (event.getCode().equals(KeyCode.ENTER)) {

				search = idd_search_text.getText();
				stage.close();
				open = false;
				if (graph != null) {
					// graph.getGuiMaster().addNode();
					INodeType node = graph.getGuiMaster().getNodeMaster().getStringToTypeMap()
							.get(idd_search_text.getText());
					if (node != null) {
						graph.getGuiMaster().addNode(new GNode(idd_search_text.getText(), node, true, graph,
								graph.getCurX(), graph.getCurY()));
						graph.update();
					}
				}
				return;
			}
			reFillComboBox();
		}

	};

	public boolean isOpen() {
		return open;
	}

}
