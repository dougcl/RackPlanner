/**
   org.gnu.java.music.rackplanner.Module.java
   [Class description.  The first sentence should be a meaningful summary of the class since it
   will be displayed as the class summary on the Javadoc package page.]
   
   [Other notes, including guaranteed invariants, usage instructions and/or examples, reminders
   about desired improvements, etc.]
   
      Copyright 2009 Doug Clauder. 
	This file is part of RackPlanner.

    RackPlanner is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, version 3 of the License.

    RackPlanner is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RackPlanner.  If not, see <http://www.gnu.org/licenses/>.
   
   @author <A HREF="mailto:dougcl@gmail.com">Doug Clauder</A>
   @version $Revision: 1.0 $ $Date: 2009/09/09 15:15:25 $
   @see [String]
   @see [URL]
**/
package org.gnu.dougcl.rackplanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.util.Vector;
import java.awt.event.ComponentEvent;




public class Module extends JPanel implements FocusListener {
	
	private static final long serialVersionUID = 1L;
	private Module m = this;
	private int row = 1;
	private int col = 1;
	private boolean focus = false;
	private ModuleProperties mp = new ModuleProperties();
	private Vector <ModuleListener> moduleListeners = new Vector<ModuleListener>();
	
	public void addModuleListener(ModuleListener ml){
		this.moduleListeners.add(ml);
	}
	
	public void setModuleProperties(ModuleProperties mp){
		this.mp = mp;
		/* This disables module selection for some reason, might be annoying anyway.
		String tip = "<html>" + 
					"Manuf:" + this.mp.getManuf() + "<br>" +
					"Model:" + this.mp.getModel() + "<br>" +
					"HP:" + this.mp.getHP() +
					"</html>";
		this.setToolTipText(tip);
		*/
	}
	
	public ModuleProperties getModuleProperties(){
		return this.mp;
	}
		
	public void focusGained(FocusEvent e) {
			this.focus = true;
			ComponentEvent ce = new ComponentEvent(this, ComponentEvent.COMPONENT_FIRST);			
			for (int i = 0; i < this.moduleListeners.size(); i++){
				this.moduleListeners.get(i).moduleFocusGained(ce);
			}
			this.repaint();
	    }

	public void focusLost(FocusEvent e) {
	    	this.focus = false;
			ComponentEvent ce = new ComponentEvent(this, ComponentEvent.COMPONENT_LAST);			
			for (int i = 0; i < this.moduleListeners.size(); i++){
				this.moduleListeners.get(i).moduleFocusLost(ce);
			}
	    	this.repaint();
	}
	
	private class LeftAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public LeftAction(String text, String desc, Integer mnemonic) {
	        super(text);
	        putValue(SHORT_DESCRIPTION, desc);
	        putValue(MNEMONIC_KEY, mnemonic);
	    }
	    public void actionPerformed(ActionEvent e) {
			ComponentEvent ce = new ComponentEvent(m, ComponentEvent.COMPONENT_MOVED);			
			for (int i = 0; i < m.moduleListeners.size(); i++){
				m.moduleListeners.get(i).moduleMoveLeft(ce);
			}
	    }
	}
	
	private class RightAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public RightAction(String text, String desc, Integer mnemonic) {
	        super(text);
	        putValue(SHORT_DESCRIPTION, desc);
	        putValue(MNEMONIC_KEY, mnemonic);
	    }
	    public void actionPerformed(ActionEvent e) {
			ComponentEvent ce = new ComponentEvent(m, ComponentEvent.COMPONENT_MOVED);			
			for (int i = 0; i < m.moduleListeners.size(); i++){
				m.moduleListeners.get(i).moduleMoveRight(ce);
			}
	    }
	}
	
	private class UpAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public UpAction(String text, String desc, Integer mnemonic) {
	        super(text);
	        putValue(SHORT_DESCRIPTION, desc);
	        putValue(MNEMONIC_KEY, mnemonic);
	    }
	    public void actionPerformed(ActionEvent e) {
			ComponentEvent ce = new ComponentEvent(m, ComponentEvent.COMPONENT_MOVED);			
			for (int i = 0; i < m.moduleListeners.size(); i++){
				m.moduleListeners.get(i).moduleMoveUp(ce);
			}
	    }
	}
	
	private class DownAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public DownAction(String text, String desc, Integer mnemonic) {
	        super(text);
	        putValue(SHORT_DESCRIPTION, desc);
	        putValue(MNEMONIC_KEY, mnemonic);
	    }
	    public void actionPerformed(ActionEvent e) {
			ComponentEvent ce = new ComponentEvent(m, ComponentEvent.COMPONENT_MOVED);			
			for (int i = 0; i < m.moduleListeners.size(); i++){
				m.moduleListeners.get(i).moduleMoveDown(ce);
			}
	    }
	}
	
	private class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public DeleteAction(String text, String desc, Integer mnemonic) {
	        super(text);
	        putValue(SHORT_DESCRIPTION, desc);
	        putValue(MNEMONIC_KEY, mnemonic);
	    }
	    public void actionPerformed(ActionEvent e) {
			ComponentEvent ce = new ComponentEvent(m, ComponentEvent.COMPONENT_MOVED);			
			for (int i = 0; i < m.moduleListeners.size(); i++){
				m.moduleListeners.get(i).moduleDelete(ce);
			}
	    }
	}

	public int getRow(){
		return this.row;
	}
	
	//Set the row position of the module
	public void setRow(int row){
		this.row = row;
	}
	
	public int getCol(){
		return this.col;
	}
	
	//Set the column position of the module
	public void setCol(int col){
		this.col = col;
	}
	
	public Module() {
		super();

        //Create keyboard actions
        Action action = new LeftAction("Go left", "This is the left button.", new Integer(KeyEvent.VK_L));
        this.getActionMap().put(KeyStroke.getKeyStroke("LEFT"),action);
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"),"goLeft");
        this.getActionMap().put("goLeft",action);
        
        action = new RightAction("Go right", "This is the right button.", new Integer(KeyEvent.VK_R));
        this.getActionMap().put(KeyStroke.getKeyStroke("RIGHT"),action);
        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"),"goRight");
        this.getActionMap().put("goRight",action);
        
        action = new UpAction("Go up", "This is the up button.", new Integer(KeyEvent.VK_U));
        this.getActionMap().put(KeyStroke.getKeyStroke("UP"),action);
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"),"goUp");
        this.getActionMap().put("goUp",action);
        
        action = new DownAction("Go down", "This is the down button.", new Integer(KeyEvent.VK_D));
        this.getActionMap().put(KeyStroke.getKeyStroke("DOWN"),action);
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"),"goDown");
        this.getActionMap().put("goDown",action);
        
        action = new DeleteAction("Delete", "This is the delete button.", new Integer(KeyEvent.VK_DELETE));
        this.getActionMap().put(KeyStroke.getKeyStroke("DELETE"),action);
        this.getInputMap().put(KeyStroke.getKeyStroke("DELETE"),"delete");
        this.getActionMap().put("delete",action);
        
        this.addFocusListener(this);
        

	}
	
	protected void paintComponent(Graphics g)
	  {
		super.paintComponent(g); 
	    if (mp.getImg() != null)
	      g.drawImage(mp.getImg(), 0,0,this.getWidth(),this.getHeight(),this); //module image 
	    if (this.focus){
	    	//Highlight the selected module somehow.
	    	g.setColor(Color.YELLOW);
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setStroke(new BasicStroke(3)); //thick
	        g2.drawLine(2, 0, 2, this.getHeight() - 2);
	        g2.drawLine(this.getWidth() - 2, 0, this.getWidth() - 2, this.getHeight() - 2);	        
	    } 	    
	  }
}
