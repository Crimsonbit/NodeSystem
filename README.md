# NodeSystem
<p> NodeSystem is a JavaFX based Node-Graph </p>
<h1 align="center">
    <img src="http://www.neonartworks.at/images/github/nodesystem1.png">
</h1>
<p align="center">
<sup>
<b>.</b>
</sup>
</p>

# How to use
Creating a new Node-Graph is really easy!
You only have to create a new GNodeSystem and get its GNodeGraph. Then you can add it wherever you want.
```java
public class SimpleGraph extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeSystem nodeSystem = new GNodeSystem();
		GNodeGraph graph = nodeSystem.getNodeGraph();

		Scene scene = new Scene(graph, 1024, 768);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
```

# Custom Nodes

to register your custom nodes, all you have to do is call registerNodes.

```java
GNodeSystem nodeSystem = new GNodeSystem();
GNodeGraph graph = nodeSystem.getNodeGraph();
graph.getGuiMaster().getNodeMaster().registerNodes(PATH_TO_PACKAGE);
```
PATH_TO_PACKAGE is a String with the full name of the package containing your nodes.

Description on how to add custom nodes follows soon!

