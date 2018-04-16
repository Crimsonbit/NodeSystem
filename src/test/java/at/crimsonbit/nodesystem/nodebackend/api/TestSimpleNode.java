package at.crimsonbit.nodesystem.nodebackend.api;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
import at.crimsonbit.nodesystem.nodebackend.testnodes.MyNodeTypes;

class TestSimpleNode {

	@Test
	void testBasicFunction() throws NoSuchNodeException {
		NodeMaster m = new NodeMaster();
		m.registerNodes("at.crimsonbit.nodesystem.nodebackend.testnodes");
		AbstractNode n = m.createNode(MyNodeTypes.SIMPLE);
		AbstractNode c1 = m.createNode(MyNodeTypes.CONSTANT);
		AbstractNode c2 = m.createNode(MyNodeTypes.CONSTANT);

		assertTrue(c1.set("constant", "val"));
		assertTrue(c2.set("constant", 32));

		m.setConnection(n, "val1", c2, "constant");
		m.setConnection(n, "s1", c1, "constant");

		assertEquals(32, c2.get("constant"));

		assertEquals("val32", n.get("concat"));

		Object w = n.get("wrongval");
		assertNull(w);

	}

	@Test
	void testOpenInput() throws NoSuchNodeException {
		NodeMaster m = new NodeMaster();
		m.registerNodes("at.crimsonbit.nodesystem.nodebackend.testnodes");
		AbstractNode n = m.createNode(MyNodeTypes.SIMPLE);
		assertEquals("null", n.get("concat"));

		AbstractNode c1 = m.createNode(MyNodeTypes.CONSTANT);

		c1.set("constant", "val");

		m.setConnection(n, "s1", c1, "constant");

		assertEquals("val0", n.get("concat"));
	}

	@Test
	void failsToModifyInput() {
		NodeMaster m = new NodeMaster();
		m.registerNodes("at.crimsonbit.nodesystem.nodebackend.testnodes");
		AbstractNode n = m.createNode(MyNodeTypes.SIMPLE);
		assertFalse(n.set("s1", "foo"));
		assertEquals("null", n.get("concat"));
	}

	@Test
	void removingConnectionClearsValue() throws NoSuchNodeException {
		NodeMaster m = new NodeMaster();
		m.registerNodes("at.crimsonbit.nodesystem.nodebackend.testnodes");
		AbstractNode n = m.createNode(MyNodeTypes.SIMPLE);
		AbstractNode c1 = m.createNode(MyNodeTypes.CONSTANT);
		AbstractNode c2 = m.createNode(MyNodeTypes.CONSTANT);

		assertTrue(c1.set("constant", "val"));
		assertTrue(c2.set("constant", 42));

		m.setConnection(n, "val1", c2, "constant");
		m.setConnection(n, "s1", c1, "constant");

		assertEquals(42, c2.get("constant"));

		assertEquals("val42", n.get("concat"));

		m.removeConnection(n, "val1");
		assertEquals("val0", n.get("concat"));

		m.removeConnection(n, "s1");
		assertEquals("null", n.get("concat"));

	}

}
