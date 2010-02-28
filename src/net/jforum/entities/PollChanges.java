package net.jforum.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.jforum.view.forum.common.PostCommon;

/**
 * An helper class that holds changes made to the pool.
 * 
 * @author Rafael Steil
 * @version $Id: PollChanges.java,v 1.4 2007/04/24 02:19:46 rafaelsteil Exp $
 */
public class PollChanges {
	private List deletedOptions = new ArrayList();
	private List newOptions = new ArrayList();
	private List changedOptions = new ArrayList();
	
	private boolean hasChanges;
	
	private Poll first;
	private Poll second;
	
	/**
	 * @param first The "complete", most recent poll version. Usually the one
	 * that's in the database. 
	 * @param second The poll to compare with. It usually will be a poll filled
	 * by {@link PostCommon#fillPostFromRequest()}, so matches will be done againts the 
	 * existing poll and the data comming from the server. 
	 */
	public PollChanges(Poll first, Poll second) {
		this.first = first;
		this.second = second;
	}
	
	public void addChangedOption(PollOption option) {
		this.changedOptions.add(option);
		this.hasChanges = true;
	}
	
	public List getChangedOptions() {
		return this.changedOptions;
	}
	
	public void addDeletedOption(PollOption option) {
		this.deletedOptions.add(option);
		this.hasChanges = true;
	}

	public List getDeletedOptions() {
		return this.deletedOptions;
	}
	
	public void addNewOption(PollOption option) {
		this.newOptions.add(option);
		this.hasChanges = true;
	}

	public List getNewOptions() {
		return this.newOptions;
	}
	
	public boolean hasChanges() {
		this.searchForChanges();
		return this.hasChanges;
	}
	
	private void searchForChanges() {
		if (first == null || second == null) {
			return;
		}
		
		boolean isSame = first.getLabel().equals(second.getLabel());
		isSame &= first.getLength() == second.getLength();
		
		this.hasChanges = !isSame;
		
		List firstOptions = first.getOptions();
		List secondOptions = second.getOptions();
		
		// Search for changes in existing options
		for (Iterator iter = firstOptions.iterator(); iter.hasNext(); ) {
			PollOption option = (PollOption)iter.next();
			PollOption changed = this.findOptionById(option.getId(), secondOptions);
			
			if (changed != null && !option.getText().equals(changed.getText())) {
				this.addChangedOption(changed);
			}
			else if (changed == null) {
				this.addDeletedOption(option);
			}
		}

		// Check if the incoming poll added options
		for (Iterator iter = secondOptions.iterator(); iter.hasNext(); ) {
			PollOption option = (PollOption)iter.next();
			
			if (this.findOptionById(option.getId(), firstOptions) == null) {
				this.addNewOption(option);
			}
		}
	}
	
	private PollOption findOptionById(int id, List options) {
		for (Iterator iter = options.iterator(); iter.hasNext(); ) {
			PollOption o = (PollOption)iter.next();
			
			if (o.getId() == id) {
				return o;
			}
		}
		
		return null;
	}
}