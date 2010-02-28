/*
 * Copyright (c) JForum Team
 * All rights reserved.

 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:

 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following  disclaimer.
 * 2)  Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 * 
 * This file creation date: Mar 3, 2003 / 11:28:25 AM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.TreeGroupDAO;

/** 
 * Implements a tree hierarchy of groups.
 * This class process all group hierarchy, and each group may have unlimited sub groups.
 * Each group is called <code>node</code> ( <code>net.jforum.model.GroupNode</code> object ), and
 * each node may have sub-nodes. For example, given a table like the folowing:  
 * 
 * <pre>
 * <code>
* +----+----------------+--------+
 * | id | name          | parent |
 * +----+---------------+--------+
 * |  6 | Parent 1      |      0 |
 * |  7 | Sub 1.1       |      6 |
 * |  8 | Sub 1.2       |      6 |
 * |  9 | SubSub 1.2.1  |      8 |
 * | 10 | SubSub 1.2.2  |      8 |
 * | 11 | Parent 2      |      0 |
 * | 12 | Parent 3      |      0 |
 * | 13 | Sub 3.1       |     12 |
 * | 14 | SubSub 3.1.1  |     13 |
 * | 15 | Sub 3.2       |     12 |
 * | 16 | Parent 4      |      0 |
 * +----+---------------+--------+
 * </code>
 * </pre>
 * 
 * results on the folowing hierarchy 
 * <pre>
 * <code>
 * Parent 1
 * ------
 * 	|
 *     Sub 1.1
 * 	----------
 * 	|
 * 	Sub 1.2
 * 	----------
 * 		|
 * 		SubSub 1.2.1
 * 		------------
 * 		|
 * 		SubSub 1.2.2
 * Parent 2
 * -----
 * Parent 3
 * -----
 * 	|
 * 	Sub 3.1
 * 	---------
 * 		|
 * 		SubSub 3.1.1
 * 		------------
 * 	|
 * 	Sub 3.2
 * 	---------
 * Parent 4
 * ------
 * </code>
 * </pre>
 *  
 * As is possible to see, we have 4 parent groups, called <code>Parent 1</code>, <code>Parent 2</code>, 
 * <code>Parent 3</code> and <code>Parent 4</code>. <code>Parent 1</code> has 2 sub groups: <code>Sub 1.1</code>
 * and <code>Sub 1.2</code>. <code>Sub 1.2</code> contains 2 subgroups, <code>SubSub 1.2.1</code> and 
 * <code>SubSub 1.2.2</code>. As every group is a node, ( <code>GroupNode</code> object ), and as each node
 * may have sub-nodes, the processing would be as:
 * <p>
 * <li> When the method <code>size()</code> of the <code>Parent 1</code> object is called,  the number 2 will
 * be retorned, because <code>Parent 1</code> has 2 sub groups;
 * <li> when the <code>size()</code> method is called on the object of <code>Sub 1.1</code>, will be returned 0, because
 * <code>Sub 1.1</code> does not have any sub groups;
 * <li> On the other hand, then we call the <code>size()</code> method of the object represented by <code>Sub 1.2</code> object,
 * we wil have a return value of 2, because <code>Sub 1.2</code> has 2 sub groups.
 * <br>
 * The same operation is done to all other groups and its sub groups. 
 * 
 * @author Rafael Steil
 * @version $Id: TreeGroup.java,v 1.10 2006/08/20 22:47:42 rafaelsteil Exp $
 */
public class TreeGroup 
{
	/**
	 * Default Constructor
	 */
	public TreeGroup() { }

	
	/**
	 * Process the group hierarchy.
	 * 
	 * @return <code>List</code> containing the complete group hierarchy. Each element
	 * from the list represents a single <code>GroupNode<code> object.	 
	 * */
	public List getNodes()
	{
		List nodes = new ArrayList();
		
		TreeGroupDAO tgm = DataAccessDriver.getInstance().newTreeGroupDAO();

		List rootGroups = tgm.selectGroups(0);	
				
		for (Iterator iter = rootGroups.iterator(); iter.hasNext();) {
			GroupNode n = (GroupNode)iter.next();
						
			this.checkExtraNodes(n);
			
			nodes.add(n);
		}
		
		return nodes;
	}
	
	/**
	 * Searchs for subgroups of a determined group
	 *
     * @param n  GroupNode
     */
	private void checkExtraNodes(GroupNode n)
	{
		TreeGroupDAO tgm = DataAccessDriver.getInstance().newTreeGroupDAO();

		List childGroups = tgm.selectGroups(n.getId());	
				
		for (Iterator iter = childGroups.iterator(); iter.hasNext();) {
			GroupNode f = (GroupNode)iter.next();
			
			this.checkExtraNodes(f);
			
			n.addNode(f);
		}
	}
}
