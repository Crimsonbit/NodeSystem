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

# About

## What is NodeSystem?
NodeSystem a javaFX based Node-Graph. It is not meant to be a standalone program, but rather a framework which people can use to implement a Node-Graph into their programs.

## How does it work?
The NodeSystem is very easy to use and to extend. We are currently working on making the whole NodeSystem modular. Meaning, every Node-Group can be added as sort of plugin to the NodeSystem. This allows easy extension of Nodes if needed. For more information look at the example section below.


# What we currently have
The NodeSystem currently features a lot of different Nodes which we started to implement.

## Nodes
Some of the NodeGroups that we currently work on are:
<ul>
  <li>Base Nodes -> Path and Output Node</li>
  <li>Image Nodes -> Load, Save, Display Images</li>
  <li>Image Filter Nodes -> Grayscale, Add, Multiply, Sobel and more</li>
  <li>Constant Nodes -> Constants for every base type</li>
  <li>Terrain Nodes -> Displacement, Dilate, Erode (OpenCL)</li>
  <li>Math Nodes -> Basic math operators</li>
  <li>Calculation Nodes -> clamp, abs, range-map, negate and more</li>
  <li>Noise Nodes -> currently only Voronoi</li>
  <li>Arduino Nodes (BETA) -> Control and Arduino through the Node-Graph using the Firmata protocol</li>
</ul>
All these Nodes are currently in the works! Some may not work properly!

## Keep in mind
Keep in mind, that we haven't made an official release yet! Every commit is for us to work on different machines etc. 
(I know we should make more branches)
This means, that depending on the current commit, some features may not work. There are still a lot of bugs to fix, so keep that in mind.

# Examples

## Create a NodeGraph

```java
public class SimpleGraph extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeGraph graph = new NodeSystemBuilder(1275, 800, true).registerDefaultNodes(true).build();

		/**
		 * Example of how to change settings used in the node-system
		 * This can change in future updates!
		 */
		graph.addSetting(GraphSettings.SETTING_CURVE_WIDTH, 6d);
		graph.addSetting(GraphSettings.SETTING_CURVE_CURVE, 100d);
		graph.getGeneralColorLookup().put(GraphSettings.COLOR_BACKGROUND_LINES, Color.WHITE);
		graph.updateColors();

		Scene scene = graph.getNodeScene(); //get the node scene
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	/**
	 * This is only needed for arduino nodes.
	 * The Serial connection works on a different thread than the JavaFX Main thread.
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

```

## Create custom nodes
Creating custom nodes, and loading them may change in future updates! This is because we are currently working on making the NodeSystem modular!

To create a custom node you need at least 3 classes!
<ul>
  <li>1. A class which describes a NodeGroup this class needs to implement INodeType. Typically this is an Enum.</li>
  <li>2. A Custom Node class which extends GNode. This is only needed if you want to create custom dialogs on right clicking the node.</li>
  <li>3. A class which represents the node inside the graph. Needs to extend AbstractNode</li>
</ul>

### Ad. 1 Creating a NodeGroup
This example shows how to create a new NodeGroup.
Every Enum constant represents a Node.
```java
public enum CustomNodes implements INodeType {
     
	EXAMPLE("Custom Node");
                       
	private String name;

	private CustomNodes(String s) {
		this.name = s;
	}
    
	@Override
	public String toString() {
		return this.name;
	}

}
```

### Ad. 2 Custom Node Classes.
The custom node classes <b> are only needed if you want to create custom dialog entry on right click or if you want to draw the nodes differently</b>.
If you don't need that, you can simply skip this class.
If you want to make your own draw method, override the public void draw() method!
```java
public class CustomNodeClassExample extends GNode {

	public CustomNodeClassExample() {
		super();
	}

	// This constructor HAS TO BE in your custom node class.
	public CustomNodeClassExample(String name, int id, boolean draw, GNodeGraph graph) {
		super(name, id, draw, graph);
		addPopUpItem(5, "Make Toast"); // Adds a custom pop-up menu item.
		addPopUpItem(6, "Animate"); // Adds a custom pop-up menu item.
		addPopUpItem(7, "Append Log"); // Adds a custom pop-up menu item.
	}

	public CustomNodeClassExample(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);
		
		addPopUpItem(5, "Make Toast"); // Adds a custom pop-up menu item.
		addPopUpItem(6, "Animate"); // Adds a custom pop-up menu item.
		addPopUpItem(7, "Append Log"); // Adds a custom pop-up menu item.
	}

	/**
	 * Every pop-up message id that is above getInternalIDCounter() will be consumed
	 * by the method below. You can react to your custom id's here.
	 */
	@Override
	public void consumeCustomMessage(int id) {
		if (id == 5) {
			//Creates a new Toast Message.
			JFXToast.makeToast((Stage) getScene().getWindow(), "Sample Text!", ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
		}
		if (id == 6) {
			//Animates the opacity property of this node.
			Animator.animateProperty(opacityProperty(), 500, 200, 200, 0, 1);
		}
		if (id == 7) {
			// the log function is located in the nodegraph.
			getNodeGraph().log(Level.WARNING, "This is a custom log warning!");
		}
	}

}
```
### Ad. 3 Create a Node
There are basically four annotations that describe a node:
<ul>
  <li>@NodeType -> Every Node MUST have this annitation. Also every Node MUST have the variable type.</li>
  <li>@NodeField -> Currently not implemented</li>
  <li>@NodeInput -> Tells the NodeSystem that this is an input. The variable can be any Object.</li>
  <li>@NodeOutput("") -> Tells the NodeSystem that this is an output. The String in the annotation MUST be the method name, which calculates the output object. This allows to have multiple outputs which are completly separate.</li>
</ul>

```java
public class CustomNodeExample extends AbstractNode {

	@NodeType
	private static final CustomNodes type = CustomNodes.EXAMPLE;

	@NodeField
	@NodeInput
	Object input;

	@NodeOutput("compute")
	Object output;


	public void compute() {
		if (input != null)
			output = input;
	}

}
```

The GUI is automatically generated!

### Tell the NodeSystem to use add this node.
This is done in the NodeSystemBuilder.

```java
GNodeGraph graph = new NodeSystemBuilder(1275, 800, true)
				.registerCustomNodes("at.crimsonbit.nodesystem.examples.customnode")
				.registerColors(Color.SANDYBROWN, CustomNodes.values()).registerDefaultNodes(true).build();
                
//only needed if you have a custom node class which extends GNode
graph.addCustomNode(CustomNodes.EXAMPLE, new CustomNodeClassExample().getClass());
```
<ul>
<li>registerCustomNodes("") -> The package to the NodeGroup</li>
<li>registerColors("") -> Sets the color of the node inside of the Graph.</li>
</ul>

# Help us
You are always welcome to work with us on this project! 
