/*
 * Created on 02/12/2005 19:23:32
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import junit.framework.TestCase;

/**
 * @author Rafael Steil
 * @version $Id: PoolChangesTest.java,v 1.2 2006/04/29 14:14:27 rafaelsteil Exp $
 */
public class PoolChangesTest extends TestCase
{
	public void testLabelOnlyShouldHaveChanged()
	{
		Poll p1 = new Poll();
		p1.setLabel("Label");
		p1.addOption(new PollOption(1, "Option 1", 0));
		
		Poll p2 = new Poll();
		p2.setLabel("Label 2");
		p2.addOption(new PollOption(1, "Option 1", 0));
		
		PollChanges changes = new PollChanges(p1, p2);

		assertTrue(changes.hasChanges());
		assertEquals(0, changes.getChangedOptions().size());
		assertEquals(0, changes.getDeletedOptions().size());
		assertEquals(0, changes.getNewOptions().size());
	}
	
	public void testShouldHave1Update()
	{
		Poll p1 = new Poll();
		p1.setLabel("Label");
		p1.addOption(new PollOption(1, "Option 1", 0));
		p1.addOption(new PollOption(2, "Option 2", 0));
		
		Poll p2 = new Poll();
		p2.setLabel("Label");
		p2.addOption(new PollOption(1, "Option 1", 0));
		p2.addOption(new PollOption(2, "Option 2 changed", 0));
		
		PollChanges changes = new PollChanges(p1, p2);

		assertTrue(changes.hasChanges());
		assertEquals(1, changes.getChangedOptions().size());
		assertEquals(new PollOption(2, "Option 2 changed", 0), changes.getChangedOptions().get(0));
	}
	
	public void testShouldHave3NewOptions()
	{
		Poll p1 = new Poll();
		p1.setLabel("Label");
		p1.addOption(new PollOption(1, "Option 1", 0));
		
		Poll p2 = new Poll();
		p2.setLabel("Label");
		p2.addOption(new PollOption(1, "Option 1", 0));
		p2.addOption(new PollOption(2, "Option 2", 0));
		p2.addOption(new PollOption(3, "Option 3", 0));
		p2.addOption(new PollOption(4, "Option 4", 0));
		
		PollChanges changes = new PollChanges(p1, p2);
		
		assertTrue(changes.hasChanges());
		assertEquals(3, changes.getNewOptions().size());
		
		assertEquals(new PollOption(2, "Option 2", 0), changes.getNewOptions().get(0));
		assertEquals(new PollOption(3, "Option 3", 0), changes.getNewOptions().get(1));
		assertEquals(new PollOption(4, "Option 4", 0), changes.getNewOptions().get(2));
	}
	
	public void testShouldHave2DeletedOptions()
	{
		Poll p1 = new Poll();
		p1.setLabel("Label");
		p1.addOption(new PollOption(1, "Option 1", 0));
		p1.addOption(new PollOption(2, "Option 2", 0));
		p1.addOption(new PollOption(3, "Option 3", 0));
		p1.addOption(new PollOption(4, "Option 4", 0));
		
		Poll p2 = new Poll();
		p2.setLabel("Label");
		p2.addOption(new PollOption(1, "Option 1", 0));
		p2.addOption(new PollOption(2, "Option 2", 0));
		
		PollChanges changes = new PollChanges(p1, p2);
		
		assertTrue(changes.hasChanges());
		assertEquals(2, changes.getDeletedOptions().size());
		
		assertEquals(new PollOption(3, "Option 3", 0), changes.getDeletedOptions().get(0));
		assertEquals(new PollOption(4, "Option 4", 0), changes.getDeletedOptions().get(1));
	}
	
	public void testShouldHaveNoChanges()
	{
		Poll p1 = new Poll();
		p1.setLabel("Label");
		p1.addOption(new PollOption(1, "Option 1", 0));
		p1.addOption(new PollOption(2, "Option 2", 0));
		
		Poll p2 = new Poll();
		p2.setLabel("Label");
		p2.addOption(new PollOption(1, "Option 1", 0));
		p2.addOption(new PollOption(2, "Option 2", 0));
		
		PollChanges changes = new PollChanges(p1, p2);
		assertFalse(changes.hasChanges());
	}
}
