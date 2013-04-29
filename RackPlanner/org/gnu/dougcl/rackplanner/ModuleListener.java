package org.gnu.dougcl.rackplanner;

import java.awt.event.ComponentEvent;

public interface ModuleListener {
	
	public void moduleFocusGained(ComponentEvent e);
	
	public void moduleFocusLost(ComponentEvent e);
	
	public void moduleMoveLeft(ComponentEvent e);
	
	public void moduleMoveRight(ComponentEvent e);
	
	public void moduleMoveUp(ComponentEvent e);
	
	public void moduleMoveDown(ComponentEvent e);
	
	public void moduleDelete(ComponentEvent e);

}
