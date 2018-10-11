package at.crimsonbit.nodesystem.gui.node.port;

import at.crimsonbit.nodesystem.gui.color.GColors;
import at.crimsonbit.nodesystem.gui.color.GStyle;
import at.crimsonbit.nodesystem.gui.color.GTheme;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.settings.GSettings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Florian Wagner
 *
 */

public class GPortRect extends Rectangle
{

	private GNode node;
	private double x;
	private double y;
	private boolean input;
	private Color inputColor;
	private Color outputColor;
	private double size = 3;

	public void redraw()
	{
		setX(x);
		setY(y);
		if (GTheme.getInstance().getStyle().equals(GStyle.DARK))
		{
			size = 10;
			setX(x + size / 2d);
			if (input)
			{
				setX(x - size);
			}
		} else
		{
			size = 6;
		}
		setWidth(size);
		setHeight(size);
		if (input)
			setFill(this.inputColor);
		else
			setFill(this.outputColor);

		setArcWidth(20.0);
		setArcHeight(20.0);
		setStrokeWidth(1);
	}

	public GPortRect(double x, double y, boolean input, GNode node)
	{
		this.node = node;

		this.x = x;
		this.y = y;
		this.input = input;
		this.inputColor = GTheme.getInstance().getColor(GColors.COLOR_PORT_INPUT);
		this.outputColor = GTheme.getInstance().getColor(GColors.COLOR_PORT_OUTPUT);
		redraw();
	}

	public void setSize(double s)
	{
		this.size = s;
	}

	public double getSize()
	{
		return this.size;
	}

	public Color getInputColor()
	{
		return inputColor;
	}

	public void setInputColor(Color inputColor)
	{
		this.inputColor = inputColor;
	}

	public Color getOutputColor()
	{
		return outputColor;
	}

	public void setOutputColor(Color outputColor)
	{
		this.outputColor = outputColor;
	}

	public GNode getNode()
	{
		return node;
	}

	public void setNode(GNode node)
	{
		this.node = node;
	}

	public double getRX()
	{
		return x;
	}

	public void setRX(double x)
	{
		this.x = x;
		redraw();
	}

	public double getRY()
	{
		return y;
	}

	public void setRY(double y)
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

}
