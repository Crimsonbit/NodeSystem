package at.crimsonbit.nodesystem.node.constant;

import java.util.Optional;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.widget.toast.Toast;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastPosition;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastTime;
import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class ConstantNodeClass extends GNode {

	private int ppc;

	public ConstantNodeClass() {
		super();
	}

	public ConstantNodeClass(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);
		ppc = getInternalIDCounter() + 1;
		addPopUpItem(ppc, "set Constant"); // Adds a custom pop-up menu item.
	}

	/**
	 * Every pop-up message id that is above getInternalIDCounter() will be consumed
	 * by the method below. You can react to your custom id's here.
	 */
	@Override
	public void consumeCustomMessage(int id) {
		if (id == ppc) {
			setConstant();
			redraw();
			getNodeGraph().update();
		}
	}

	@Override
	public String toString() {
		String str = name + ", connections=" + connections + ", type=" + type + ", inPortCount=" + inPortCount
				+ ", outPortcount=" + outPortcount + ", ppc=" + ppc;
		if (this.getAbstractNode() != null)
			return str + "\nconstant: " + this.getAbstractNode().get("constant");
		else
			return str;
	}

	public void setConstant() {
		doBlur();
		TextInputDialog dialog = new TextInputDialog(getName());
		dialog.setTitle("Constant");
		dialog.setHeaderText("Set a new constant for the node.");
		dialog.setContentText("Constant: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			// this.nameAddition = result.get();
			// setName(this.nameAddition);
			if (this.type == Constant.STRING) {
				this.getAbstractNode().set("constant", result.get());

			} else if (this.type == Constant.BOOLEAN) {
				try {
					boolean b = Boolean.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a boolean type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.DOUBLE) {
				try {
					double b = Double.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a double type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.FLOAT) {
				try {
					float b = Float.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a float type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.INTEGER) {
				try {
					int b = Integer.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a integer type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.BYTE) {
				try {
					byte b = Byte.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a byte type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}

			} else if (this.type == Constant.LONG) {
				try {
					long b = Long.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a long type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.SHORT) {
				try {
					short b = Short.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a short type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			}
			redraw();
			removeBlur();
		} else {
			removeBlur();

		}
	}
}
