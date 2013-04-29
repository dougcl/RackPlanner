/**
   org.gnu.java.music.rackplanner.Rack.java
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


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class Rack extends JLayeredPane implements MouseListener, MouseMotionListener, ModuleListener {

	private static final long serialVersionUID = 1L;
	
	private BufferedImage tileImage; //the rack background image.
	private  String rackFilename;
	private ModuleFactory mf;
	private RackProperties rp;
	
	private int img_height = 219; //height in pixels of the background image.
	private int img_width = 714; //width in pixels of the backgound image.
	private Point mousePoint = new Point(0,0); //Stores the last mouse position during dragging.
	private Module selectedModule;
	private boolean dragging = false;
	
	public void clearAll() {
		this.mf = null;
	}
	
	public RackProperties getRackProperties() {
		return this.rp;
	}
	
	
	public String getModulesPath(){
		return this.rp.getModulesPath();
	}
	
	public void save() {
		this.save(this.rackFilename);
		
	}
    public void save(String filename){
    	try {
    			this.rackFilename = filename;
    			FileWriter outFile = new FileWriter(filename);
    			PrintWriter out = new PrintWriter(outFile);
    			//Create the file
    			out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
    			out.println("<rack>");
    			//Save the properties.
    			out.println("\t<properties>");
    			out.println("\t\t<scale>" + this.rp.getScale() + "</scale>");
    			out.println("\t\t<rows>" + this.rp.getRows() + "</rows>");
    			out.println("\t\t<cols>" + this.rp.getCols() + "</cols>");
    			out.println("\t\t<imagesPath>" + this.rp.getImagesPath() + "</imagesPath>");
    			out.println("\t\t<modulesPath>" + this.rp.getModulesPath() + "</modulesPath>");
    			out.println("\t\t<rackImageFilename>" + this.rp.getRackImageFilename() + "</rackImageFilename>");
    			out.println("\t\t<rackHP>" + this.rp.getRackHP() + "</rackHP>");
    			out.println("\t</properties>");
    			out.println("\t<modules>");
    			String zipname;
    			int row;
    			int col;
    			//Save the modules.
    			for(int i = 0; i < this.getComponentCount(); i++){
    				zipname = ((Module)this.getComponent(i)).getModuleProperties().getModuleZipFilename();
    				row = ((Module)this.getComponent(i)).getRow();
    				col = ((Module)this.getComponent(i)).getCol();
    				out.println("\t\t<module>");
    				out.println("\t\t\t<zipFilename>" + zipname + "</zipFilename>");
    				out.println("\t\t\t<row>" + row + "</row>");
    				out.println("\t\t\t<col>" + col + "</col>");
    				out.println("\t\t</module>");
    			}
    			out.println("\t</modules>");
    			out.println("</rack>");
    			out.close();
    	} catch (IOException e){
    		System.out.println(e);;
    	}
   	
    }
	
	
	private void readModulesFromXML(File file){
		try {
	    	  String zipFilename = ""; //null module filename is okay. Module comes up blank.
	    	  String val = "";
	    	  int row = 1; //default
	    	  int col = 1; //default
	    	  
	    	  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    	  DocumentBuilder db = dbf.newDocumentBuilder();
	    	  Document doc = db.parse(file);
	    	  doc.getDocumentElement().normalize();
	    	  NodeList modules = doc.getElementsByTagName("modules");
	    	  if (modules.getLength() == 0) return;
	    	  modules = modules.item(0).getChildNodes();
	    	  //Loop through all the children of "modules" and add them to the rack.
	    	  for (int i = 0; i < modules.getLength(); i++){
	    		  if (modules.item(i).getNodeType() == Node.ELEMENT_NODE) {
	    			 NodeList module = modules.item(i).getChildNodes();
	    			 for (int j = 0; j < module.getLength(); j++){
	    				 if (module.item(j).getNodeType() == Node.ELEMENT_NODE) {
	    					 if (module.item(j).getNodeName() == "zipFilename"){
	    						 val = module.item(j).getFirstChild().getNodeValue();
	    						 zipFilename = val;
	    					 } else if (module.item(j).getNodeName() == "row") {
	    						 val = module.item(j).getFirstChild().getNodeValue();
	    						 row = Integer.valueOf(val);
	    					 } else if (module.item(j).getNodeName() == "col") {
	    						 val = module.item(j).getFirstChild().getNodeValue();
	    						 col = Integer.valueOf(val);	    					 }		  
	    				 }
	    			 }
	    		     Module m = this.addModule(zipFilename);
	    		     this.moveModule(m, row, col);
	    			
	    		  }  
	    	  }	    				  
	    } catch (Exception e) {
  	  		String msg = "Rack Planner has a problem with the rack xml file: " + file + "\n" +
			"\n" +
			e.getMessage();
	  			JOptionPane.showMessageDialog(this, msg, "Rack XML Problem", JOptionPane.ERROR_MESSAGE);
	  			System.exit(0);
	    }	
		
	}
	
	public Module getSelectedModule(){
		return this.selectedModule;
	}
	
	
	public Module addModule(String moduleZipFileName){
		Module m = this.mf.getModule(moduleZipFileName);
		this.add(m);
		int col = this.getColFromX(this.getVisibleRect().x);
		int row = this.getRowFromY(this.getVisibleRect().y);
		this.moveModule(m, row, col);
		m.requestFocusInWindow();
		return m;
	}
	
	public void moveModule(Module m, int row, int col){
		row = Math.max(row, 1);
		row = Math.min(row, this.rp.getRows());
		col = Math.max(col, 1);
		col = Math.min(col,this.rp.getCols() * this.rp.getRackHP() + 1 - m.getModuleProperties().getHP());
		m.setLocation(this.getPointFromRowAndCol(row, col));
		m.setRow(row);
		m.setCol(col);
	}
	
	
	public void moduleFocusGained(ComponentEvent e){
		Module m = (Module)e.getComponent();
		this.moveToFront(m);	
		this.selectedModule = m;
	}
	
	public void moduleFocusLost(ComponentEvent e){
		
	}
	
	public void moduleMoveUp(ComponentEvent e){
		Module m = (Module)e.getComponent();
		this.moveModule(m, m.getRow() - 1, m.getCol());	
	}
	
	public void moduleMoveDown(ComponentEvent e){
		Module m = (Module)e.getComponent();
		this.moveModule(m, m.getRow() + 1, m.getCol());	
	}
	
	public void moduleMoveLeft(ComponentEvent e){
		Module m = (Module)e.getComponent();
		this.moveModule(m, m.getRow(), m.getCol() - 1);
	}
	
	public void moduleMoveRight(ComponentEvent e){
		Module m = (Module)e.getComponent();
		this.moveModule(m, m.getRow(), m.getCol() + 1);
	}
	
	public void moduleDelete(ComponentEvent e){
		Module m = (Module)e.getComponent();
		this.remove(m);
		m = null;
		this.repaint();
	}
	
	
	public void mousePressed(MouseEvent e) {
		this.mousePoint = e.getPoint();
		Component c = this.getComponentAt(e.getPoint());
		if (c instanceof Module){
			this.selectedModule = (Module)c;
			this.selectedModule.requestFocusInWindow();			
		} else {
			this.selectedModule = null;
			this.requestFocusInWindow();			
		}
	}
	
	public void mouseEntered(MouseEvent e) {
		//this.requestFocusInWindow();
	}
	
	public void mouseExited(MouseEvent e) {
		//this.requestFocusInWindow();
	}
	
	//Find out whether a module has been selected. If so, find out which one and give it focus.
	//Otherwise, set the focus to the rack (defocus the modules).
	public void mouseClicked(MouseEvent e) {
		this.mousePoint = e.getPoint();
		if (e.getButton() == MouseEvent.BUTTON1) {
			Component c = this.getComponentAt(e.getPoint());
			if (c instanceof Module){
				this.selectedModule = (Module)c;
				this.selectedModule.requestFocusInWindow();			
			} else {
				this.selectedModule = null;
				this.requestFocusInWindow();			
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			//Fire event to parent frame to show menu. Need to build listener.
		}
	}
	
	//If there is a module selected and the mouse is being released, assume it is being dropped
	//at the end of a drag. Find the row and column and move the module (snap to grid).
	public void mouseReleased(MouseEvent e) {
		if (dragging && this.selectedModule != null) {
			int col = this.getColFromX(this.selectedModule.getX());
			int row = this.getRowFromY(e.getPoint().y);
			this.moveModule(this.selectedModule, row, col);	
		}
		dragging = false;
	}
	
	public void mouseMoved(MouseEvent e) {
    	//this.requestFocusInWindow();
	}
	
	//Find out if a module is being dragged. If so, which one, and make it move with the mouse.
	public void mouseDragged(MouseEvent e) {
		if (this.selectedModule != null) {
			int dx = e.getX() - this.mousePoint.x;
			int dy = e.getY() - this.mousePoint.y;
			this.mousePoint = e.getPoint(); //Save the current mouse location for next time.
			this.selectedModule.setLocation(this.selectedModule.getX() + dx, this.selectedModule.getY() + dy);
			this.dragging = true;
		}
	}

	
	//This returns a dimension from the number of coumns in HP and the number of rows in U.
	public Dimension getDimensionFromRowsAndCols(int rows, int cols){
		return new Dimension(Math.round(cols * this.img_width/this.rp.getRackHP()),rows * this.img_height);	
	}
	
	public Point getPointFromRowAndCol(int row, int col){
		return new Point(Math.round((col - 1) * this.img_width/this.rp.getRackHP()),(row - 1)* this.img_height);
	}
	
	//Round off the pixel x coordinate to the nearest column
	//This is where snap to grid accuracy is determined.
	public int getColFromX(int x){
		return Math.round(this.rp.getRackHP() * x/this.img_width) + 1;
	}
	
	//Round off the pixel y coordinate to the nearest row.
	//This is where snap to grid accuracy is determined.
	public int getRowFromY(int y){
		return Math.round(y/this.img_height) + 1;
	}
	
	public Rack(String filename){
		super ();
		this.rackFilename = filename;
		File file = new File(filename);
		try {
			//Read the rack properties from the XML file
  	  		this.rp = new RackProperties(file);
  	  		//Read in the rack background image
			BufferedImage tileImage = ImageIO.read(new File(this.rp.getImagesPath() + "/" + this.rp.getRackImageFilename()));
				
//			Scale the rack image
			int scaled_width = (int)Math.round(this.rp.getScale() * tileImage.getWidth());
			Image img = tileImage.getScaledInstance(scaled_width,-1,Image.SCALE_SMOOTH);
	        int w = img.getWidth(null);
	        int h = img.getHeight(null);
	        BufferedImage scaled = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);            
	        Graphics2D g = scaled.createGraphics();
	        g.drawImage(img,0,0,null);
	        if(g != null) g.dispose();
	        tileImage = scaled;

	        this.tileImage = tileImage;
			this.img_height = this.tileImage.getHeight();
			this.img_width = this.tileImage.getWidth();
			this.setLayout(null); //use absolute coordinates.
			this.setSize(new Dimension(this.img_width * this.rp.getCols(),this.img_height * this.rp.getRows()));
			this.setPreferredSize(new Dimension(this.img_width * this.rp.getCols(),this.img_height * this.rp.getRows()));
			
	        this.mf = new ModuleFactory(this, this.rp.getModulesPath());
	        
	        this.readModulesFromXML(file);
			
	        
	        this.addMouseListener(this);
	        this.addMouseMotionListener(this);  
  	  		
  	  	} catch (Exception e) {
  	  		String msg = "Rack Planner has a problem with the rack image file: " + this.rp.getImagesPath() + "/" + this.rp.getRackImageFilename() + "\n" +
  	  					"\n" +
  	  					e.getMessage() + "\n" +
  	  					"\n" +
  	  					"Check the filename and make sure the file exists or fix the filename in your rack xml file."; 
  	  		JOptionPane.showMessageDialog(this, msg, "Rack Image Problem", JOptionPane.ERROR_MESSAGE);
  	  		System.exit(0);
  	  	}		 		
	}
	
	
	public void paint(Graphics g) {
		//override paint to get the tiled background image.
		if (tileImage != null){
		    int width = getWidth();
		    int height = getHeight();
		    int imageW = this.img_width;
		    int imageH = this.img_height;
	
		    // Tile the image to fill our area.
		    for (int x = 0; x < width; x += imageW) {
		        for (int y = 0; y < height; y += imageH) {
		            g.drawImage(tileImage, x, y, this);
		        }
		    }
		}
		super.paint(g);
	}
}
