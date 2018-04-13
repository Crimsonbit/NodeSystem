package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum ImageFilter implements INodeType {

	IMAGE_ADD("Add-Filter Node"), IMAGE_SUBTRACT("Subtract-Filer Node"), IMAGE_MULTIPLY(
			"Multiply-Filter Node"), IMAGE_NEGATE("Negate-Filter Node"), IMAGE_DIVIDE(
					"Divide-Filter Node"), IMAGE_SOBEL(
							"Sobel-Filter Node"), IMAGE_SMOOTHEN("Smoothen-Filter Node"), IMAGE_NORMALIZE(
									"Normalize-Filter Node"), IMAGE_BLUR("Blur-Filter Node"), IMAGE_GRAYSCALE(
											"Grayscale-Filter Node"), IMAGE_RESIZE("Resize-Filter Node");

	private String name;

	private ImageFilter(String s) {

		this.name = s;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
