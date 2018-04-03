package nodebackend.test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import at.crimsonbit.nodebackend.api.AbstractNode;
import at.crimsonbit.nodebackend.api.NodeMaster;
import at.crimsonbit.nodebackend.misc.NoSuchNodeException;

class TestSimpleNode {

	@Test
	void test() throws NoSuchNodeException {
		NodeMaster m = new NodeMaster();
		m.registerNodes("nodebackend.test");
		AbstractNode n = m.createNode(MyNodeTypes.SIMPLE);
		AbstractNode c1 = m.createNode(MyNodeTypes.CONSTANT);
		AbstractNode c2 = m.createNode(MyNodeTypes.CONSTANT);

		c1.set("constant", "val");
		c2.set("constant", 32);

		m.setConnection(n, "val1", c2, "constant");
		m.setConnection(n, "s1", c1, "constant");

		assertEquals(32, c2.get("constant"));

		assertEquals("val32", n.get("concat"));

		Object w = n.get("wrongval");
		assertNull(w);

	}

}
