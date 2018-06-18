package at.crimsonbit.nodesystem.nodebackend.api;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
import at.crimsonbit.nodesystem.nodebackend.testnodes.MyNodeTypes;

class TestPersistence {

	@Test
	void testBasicSave() throws NoSuchNodeException, IOException {
		assumeTrue(new File("/tmp").exists());
		NodeMaster m = new NodeMaster();
		m.registerNodes("at.crimsonbit.nodesystem.nodebackend.testnodes");
		AbstractNode n = m.createNode(MyNodeTypes.SIMPLE);
		AbstractNode c1 = m.createNode(MyNodeTypes.CONSTANT);
		AbstractNode c2 = m.createNode(MyNodeTypes.CONSTANT);

		m.setConnection(n, "val1", c2, "constant");
		m.setConnection(n, "s1", c1, "constant");

		m.save("/tmp/nodebackend.zip", true);
	}

	@Test
	void testLoad() throws NoSuchNodeException, IOException, ClassNotFoundException {
		assumeTrue(new File("/tmp").exists());
		NodeMaster m = new NodeMaster();
		m.registerNodes("at.crimsonbit.nodesystem.nodebackend.testnodes");
		AbstractNode n = m.createNode(MyNodeTypes.SIMPLE);
		AbstractNode c1 = m.createNode(MyNodeTypes.CONSTANT);
		AbstractNode c2 = m.createNode(MyNodeTypes.CONSTANT);
		assertTrue(c1.set("constant", "val"));
		assertTrue(c2.set("constant", 32));
		m.setConnection(n, "val1", c2, "constant");
		m.setConnection(n, "s1", c1, "constant");

		m.save("/tmp/nodebackend.zip", true);

		NodeMaster m2 = NodeMaster.load("/tmp/nodebackend.zip").a;
		for (int i = 0; i < 3; i++) {
			assertEquals(m.getNodeByID(i).getClass(), m2.getNodeByID(i).getClass());
		}
		AbstractNode newN = m2.getNodeByID(m.getIdOfNode(n));

		assertEquals("val32", newN.get("concat"));

	}

}
