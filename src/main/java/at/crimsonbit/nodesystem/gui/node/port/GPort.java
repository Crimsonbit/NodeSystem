package at.crimsonbit.nodesystem.gui.node.port;

import java.util.Set;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeMaster;
import at.crimsonbit.nodesystem.gui.GState;
import at.crimsonbit.nodesystem.gui.color.GColors;
import at.crimsonbit.nodesystem.gui.color.GTheme;
import at.crimsonbit.nodesystem.gui.dialog.GEntry;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.IGConsumable;
import at.crimsonbit.nodesystem.gui.settings.GGraphSettings;
import at.crimsonbit.nodesystem.gui.settings.GSettings;
import at.crimsonbit.nodesystem.language.LanguageSetup;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * <h1>GPort extends {@link Group} implements {@link IGConsumable}</h1>
 * <p>
 * This class represents one Port in a node.
 * </p>
 * 
 * @author Florian Wagner
 *
 */

public class GPort extends Group implements IGConsumable
{

	private double x;
	private double y;
	private LanguageSetup lang = LanguageSetup.getInstance();
	private int id;
	private GGraphSettings inst = GGraphSettings.getInstance();
	private boolean input;
	private GNode node;
	private CubicCurve line;
	private GPopUp dialog;
	private String stringID;
	private GPortLabel label;
	private GPortRect rect;
	private GPortConHelper conHelper;
	private boolean drawText = true;
	private final Tooltip tooltip = new Tooltip();
	private final int MAGIC_OFFSET = 3;
	private final int SNAP_SIZE_X = 15;
	private final int SNAP_SIZE_Y = 5;
	private boolean isConnected = false;

	public GPort(int id, boolean input, String labels, double x, double y, GNode node)
	{
		this.node = node;
		this.id = id;
		this.stringID = labels;
		this.input = input;
		this.x = x;
		this.y = y;
		this.label = new GPortLabel(x, y, labels, input);
		this.rect = new GPortRect(x, y, input, node);
		this.conHelper = new GPortConHelper(x, y, input, this);
		GPopUp pop = new GPopUp();
		pop.addItem(-1, labels, true);
		// pop.addItem(0, "Connect");
		pop.addItem(1, lang.getString("core", "portMenuDisconnect"));
		this.dialog = pop;

		// mouseHandler();
		draw();

		for (MenuItem item : this.dialog.getItems())
		{
			int idd = Integer.valueOf(item.getId());
			item.setOnAction(event ->
				{ //
					// System.out.println(idd);
					consumeMessage(idd, (GEntry) event.getSource());
					event.consume();
				});
		}
		this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event ->
			{
				dialog.show(this, event.getScreenX(), event.getScreenY());
				event.consume();
			});

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, event ->
			{
				dialog.hide();
			});

		tooltip.setText("GPort: " + this.id + "\n" + "Input: " + this.input + "\n" + "Type: "
				+ node.getAbstractNode().get(labels));

		// Tooltip.install(this.getNode(), tooltip);
		// getChildren().add(line);

		setOnMouseReleased(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				if (node.isPortPressed())
				{

					boolean isInput = false;
					boolean isOutput = false;
					GNodeMaster master = node.getNodeGraph().getGuiMaster();
					master.removecurConnectPorts();

					if (!input)
					{
						master.setFirstPort(GPort.this);
						isOutput = true;
					} else
					{
						master.setSecondPort(GPort.this);
						isInput = true;
					}

					Set<GNode> allNodes = node.getNodeGraph().getGuiMaster().getAllNodes();

					outer: for (GNode n : allNodes)
					{
						if (isInput && !isOutput)
						{
							for (GPort p : n.getOutputPorts())
							{
								if (p.getConHelper().isMarked())
								{
									master.setFirstPort(p);
									break outer;
								}

							}
						} else
						{
							for (GPort p : n.getInputPorts())
							{
								if (p.getConHelper().isMarked())
								{
									master.setSecondPort(p);
									break outer;
								}
							}
						}
					}

					master.connectPorts();
					master.removecurConnectPorts();

					/* Remove Temporary Line */
					node.getNodeGraph().getTempLineLayer().getChildren().remove(line);
					node.getNodeGraph().getNodeLayer().toFront();
					node.getNodeGraph().update();

					node.setPortPressed(false);
					node.getNodeGraph().setState(GState.DEFAULT);
				}
			}
		});

		setOnMouseClicked(event ->
			{
				Set<GNode> allNodes = node.getNodeGraph().getGuiMaster().getAllNodes();
				for (GNode n : allNodes)
				{
					for (GPort p : n.getOutputPorts())
					{
						p.getConHelper().setMarked(false);
					}
					for (GPort p : n.getInputPorts())
					{
						p.getConHelper().setMarked(false);
					}
				}

			});
		setOnDragDetected(evt ->
			{
				node.getNodeGraph().getGuiMaster().setSourceConPort(this);
				startFullDrag();
			});
		setOnMouseDragged(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				if (!conHelper.isDoDisconnect())
					if (event.isPrimaryButtonDown() && !event.isSecondaryButtonDown())
					{
						if (!isInput())
						{
							node.setPortPressed(true);
							if (line == null)
								line = new CubicCurve();

							line.controlX1Property().bind(node.layoutXProperty()
									.add(getPortX() + (double) inst.getSetting(GSettings.SETTING_CURVE_CURVE)));
							line.controlY1Property().bind(node.layoutYProperty().add(getY() + 5d));
							line.controlX2Property().bind(node.layoutXProperty()
									.add(event.getX() - (double) inst.getSetting(GSettings.SETTING_CURVE_CURVE)));
							line.controlY2Property().bind(node.layoutYProperty().add(event.getY() + 5d));
							node.getNodeGraph().setState(GState.PORTCON);
							line.setStroke(node.getNodeType().getColor());
							line.setStrokeWidth(
									(double) GGraphSettings.getInstance().getSetting(GSettings.SETTING_CURVE_WIDTH));
							line.startXProperty().bind(node.layoutXProperty().add(getPortX() + MAGIC_OFFSET));
							line.startYProperty().bind(node.layoutYProperty().add(getY() + 5d));

							line.setStrokeLineCap(StrokeLineCap.ROUND);
							line.setFill(Color.TRANSPARENT);

							line.endXProperty().bind(node.layoutXProperty().add(event.getX()));
							line.endYProperty().bind(node.layoutYProperty().add(event.getY()));
							line.toFront();

							DropShadow e = new DropShadow();
							e.setBlurType(BlurType.GAUSSIAN);
							e.setBlurType(BlurType.GAUSSIAN);

							e.setColor(GTheme.getInstance().getColor(GColors.COLOR_SHADOW_COLOR));
							e.setWidth((double) inst.getSetting(GSettings.SETTING_SHADOW_WIDTH));
							e.setHeight((double) inst.getSetting(GSettings.SETTING_SHADOW_HEIGHT));
							e.setOffsetX((double) inst.getSetting(GSettings.SETTING_SHADOW_WIDTH));
							e.setOffsetY((double) inst.getSetting(GSettings.SETTING_SHADOW_HEIGHT));
							e.setRadius((double) inst.getSetting(GSettings.SETTING_SHADOW_RADIUS));
							line.setEffect(e);

							node.getNodeGraph().getTempLineLayer().getChildren().remove(line);
							node.getNodeGraph().getTempLineLayer().getChildren().add(line);
							node.getNodeGraph().getTempLineLayer().toFront();
							node.getNodeGraph().update();
						}
					}
			}
		});
	}

	public boolean isConnected()
	{
		return isConnected;
	}

	public void setConnected(boolean isConnected)
	{
		this.isConnected = isConnected;
	}

	void mouseHandle(MouseEvent event, boolean press)
	{

		GNodeGraph graph = node.getNodeGraph();
		if (isInput())
		{
			graph.getGuiMaster().setSecondPort(GPort.this);
		} else
		{
			graph.getGuiMaster().setFirstPort(GPort.this);
		}
		if (graph.getGuiMaster().connectPorts())
		{
			graph.update();
			graph.getGuiMaster().removecurConnectPorts();
		}
		node.setPortPressed(press);

	}

	@SuppressWarnings("unused")
	private void mouseHandler()
	{
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> mouseHandle(event, false));
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mouseHandle(event, false));
	}

	public void setDrawText(boolean f)
	{
		this.drawText = f;
	}

	public void draw()
	{
		if (drawText)
			getChildren().add(label);
		getChildren().add(rect);
		getChildren().add(conHelper);
	}

	public void redraw()
	{
		getChildren().clear();
		if (drawText)
			getChildren().add(label);
		getChildren().add(rect);
		getChildren().add(conHelper);
	}

	public double getPortX()
	{
		return this.x;
	}

	public void relocatePortX(double x)
	{
		// getChildren().clear();
		label.setIx(x);
		rect.setRX(x);
		this.x = x;
		// relocate(x);
		// getChildren().add(label);
		// getChildren().add(rect);
		// draw();
	}

	public void relocatePortY(double y)
	{
		label.setIy(y);
		rect.setRY(y);
		this.y = y;
	}

	public GPortRect getPortRectangle()
	{
		return this.rect;
	}

	public String getStringID()
	{
		return stringID;
	}

	public void setStringID(String stringID)
	{
		this.stringID = stringID;
	}

	public GPopUp getPopUpDialog()
	{
		return this.dialog;
	}

	public GNode getNode()
	{
		return this.node;
	}

	public int getID()
	{
		return this.id;
	}

	public GPortConHelper getConHelper()
	{
		return conHelper;
	}

	public void setConHelper(GPortConHelper conHelper)
	{
		this.conHelper = conHelper;
	}

	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public boolean isInput()
	{
		return input;
	}

	public void setInput(boolean input)
	{
		this.input = input;
	}

	@Override
	public void consumeMessage(int id, GEntry source)
	{
		if (id == 0)
		{
			GNodeGraph graph = node.getNodeGraph();
			// graph.getGuiMaster().addConnection(node1, node2);
			if (isInput())
				graph.getGuiMaster().setSecondPort(this);
			else
				graph.getGuiMaster().setFirstPort(this);
			graph.getGuiMaster().connectPorts();
			graph.update();
		} else if (id == 1)
		{
			GNodeGraph graph = node.getNodeGraph();
			graph.getGuiMaster().removeConnection(this);
			graph.update();
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ((stringID == null) ? 0 : stringID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GPort other = (GPort) obj;
		if (id != other.id)
			return false;
		if (node == null)
		{
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (stringID == null)
		{
			if (other.stringID != null)
				return false;
		} else if (!stringID.equals(other.stringID))
			return false;
		return true;
	}

}
