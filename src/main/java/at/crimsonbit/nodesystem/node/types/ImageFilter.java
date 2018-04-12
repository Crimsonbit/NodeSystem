package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum ImageFilter implements INodeType {

	IMAGE_ADD, IMAGE_SUBTRACT, IMAGE_MULTIPLY, 
	IMAGE_NEGATE, IMAGE_DIVIDE, IMAGE_SOBEL, IMAGE_SMOOTHEN, 
	IMAGE_NORMALIZE, IMAGE_BLUR, IMAGE_GRAYSCALE

}
