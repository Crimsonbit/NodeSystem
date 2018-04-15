package nodebackend.test;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeMaster;
import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;

class TestPersistence {

	@Test
	void testBasicSave() throws NoSuchNodeException, IOException {
		assumeTrue(new File("/tmp").exists());
		NodeMaster m = new NodeMaster();
		m.registerNodes("nodebackend.test");
		AbstractNode n = m.createNode(MyNodeTypes.SIMPLE);
		AbstractNode c1 = m.createNode(MyNodeTypes.CONSTANT);
		AbstractNode c2 = m.createNode(MyNodeTypes.CONSTANT);

		m.setConnection(n, "val1", c2, "constant");
		m.setConnection(n, "s1", c1, "constant");
		
		m.save("/tmp/nodebackend.zip", true);
	}
	
	@Test
	void testLoad() throws NoSuchNodeException, IOException {
		assumeTrue(new File("/tmp").exists());
		NodeMaster m = new NodeMaster();
		m.registerNodes("nodebackend.test");
		AbstractNode n = m.createNode(MyNodeTypes.SIMPLE);
		AbstractNode c1 = m.createNode(MyNodeTypes.CONSTANT);
		AbstractNode c2 = m.createNode(MyNodeTypes.CONSTANT);

		m.setConnection(n, "val1", c2, "constant");
		m.setConnection(n, "s1", c1, "constant");
		
		m.save("/tmp/nodebackend.zip", true);
		
		NodeMaster m2 = NodeMaster.load("/tmp/nodebackend.zip");
		for(int i = 0; i < 3; i++) {
			assertEquals(m.getNodeByID(i), m2.getNodeByID(i));
		}
	}

}
