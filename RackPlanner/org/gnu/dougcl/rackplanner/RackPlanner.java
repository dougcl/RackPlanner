/**
   org.gnu.java.music.rackplanner.MainFrame.java
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
   @version $Revision: 1.3   $ $Date: 2012/05/22 15:15:25 $
   @version $Revision: 1.2   $ $Date: 2010/11/21 15:15:25 $
   @version $Revision: 1.1   $ $Date: 2010/11/20 15:15:25 $
   @version $Revision: 1.0.9 $ $Date: 2009/11/01 15:15:25 $
   @version $Revision: 1.0.4 $ $Date: 2009/01/20 15:15:25 $
   @version $Revision: 1.0 $ $Date: 2009/01/09 15:15:25 $
   @see [String]
   @see [URL]
**/
package org.gnu.dougcl.rackplanner;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.Toolkit;




import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.io.FileNotFoundException;



public class RackPlanner extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static final String version = "1.3";
	private static final String title = "Rack Planner v" + version;
	
	private Rack r;
	private RackPlanner frame = this;
	private Dimension dialogSize; //so people don't have to keep resizing
	private String copiedModuleZipName;
	private File jpgDirectory; //so people don't have to keep choosing.


	class ZipFilter extends javax.swing.filechooser.FileFilter {
	    public boolean accept(File file) {
	        String filename = file.getName();
	        return filename.endsWith(".zip");
	    }
	    public String getDescription() {
	        return "*.zip";
	    }
	}
	
	class JpgFilter extends javax.swing.filechooser.FileFilter {
	    public boolean accept(File file) {
	        String filename = file.getName();
	        return filename.endsWith(".jpg") || file.isDirectory();
	    }
	    public String getDescription() {
	        return "*.jpg";
	    }
	}
	
	class XMLFilter extends javax.swing.filechooser.FileFilter {
	    public boolean accept(File file) {
	        String filename = file.getName();
	        return filename.endsWith(".xml");
	    }
	    public String getDescription() {
	        return "*.xml";
	    }
	}

	
    private void openAddModuleDialog() {
    	JFileChooser fc = new JFileChooser(new File(this.r.getModulesPath()));
    	fc.setMultiSelectionEnabled(true);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);     
        fc.addChoosableFileFilter(new ZipFilter());
        if (this.dialogSize != null) {
        	fc.setPreferredSize(this.dialogSize);
        }
        int result = fc.showDialog(this, "Select Module ZIP Files");   
        this.dialogSize = fc.getSize();
        if(result == JFileChooser.CANCEL_OPTION) return; 
        //this.r.addModule(fc.getSelectedFile().getName());
        File[] files = fc.getSelectedFiles();
        for (int i=0;i<files.length;i++){
        	Module m = this.r.addModule(files[i].getName());
        	this.r.moveToFront(m);
        }   
     }
    
    private void saveAs() {
    	JFileChooser fc = new JFileChooser(new File("."));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	fc.addChoosableFileFilter(new XMLFilter());
        if (this.dialogSize != null) {
        	fc.setPreferredSize(this.dialogSize);
        }
    	int result = fc.showDialog(frame, "Save As");    
    	this.dialogSize = fc.getSize();
    	if(result == JFileChooser.CANCEL_OPTION) return;
    	String fname = fc.getSelectedFile().getName();
    	if (!fname.endsWith(".xml")) fname = fname + ".xml";
    	this.r.save(fname);
    	frame.setTitle(title + " (" + fname + ")");
    	
    }
    
    private void saveJPG() {
    	JFileChooser fc = new JFileChooser(new File("."));
    	fc.setAcceptAllFileFilterUsed(false);
    	fc.addChoosableFileFilter(new JpgFilter());
        if (this.dialogSize != null) {
        	fc.setPreferredSize(this.dialogSize);
        }
		if (this.jpgDirectory != null){
			fc.setCurrentDirectory(this.jpgDirectory);
		}
    	int result = fc.showSaveDialog(frame);    
    	this.dialogSize = fc.getSize();
    	if(result == JFileChooser.CANCEL_OPTION) return;
    	
    	try {
    		BufferedImage img = new BufferedImage(this.r.getWidth(), this.r.getHeight(), BufferedImage.TYPE_INT_RGB);
    		this.r.paint(img.getGraphics());
    		File selFile = fc.getSelectedFile();
    		File selDir = fc.getCurrentDirectory();
    		this.jpgDirectory = selDir;
        	String fname = selFile.getName();
        	String dirName = selDir.getAbsolutePath();
        	if (!fname.endsWith(".jpg")) {
        		fname = fname + ".jpg";
        		selFile = new File(dirName + '\\' +  fname);
        	} 

    		//ImageIO.setUseCache(true);
    		//ImageIO.setCacheDirectory(null);
			ImageIO.write(img, "jpg", selFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			JOptionPane.showMessageDialog(this,Runtime.getRuntime().freeMemory(), "DEBUG",JOptionPane.PLAIN_MESSAGE);
			String msg = 	"Rack image is too large to create. Either use a smaller scale or increase the available memory.\n" +
							"\n" +
							"To increase the available memory:\n" +
							"Use -Xmx256m on the command line (increases available memory to 256MB).\n" +
							"Example:\n" +
							"C:\\WINNT\\system32\\java.exe -Xmx256m -jar RackPlanner.jar rack.xml";
			JOptionPane.showMessageDialog(this, msg, "Java Memory Limit Exceeded", JOptionPane.ERROR_MESSAGE);
		}	
    }
    
    
    private void openDialog() {
    	JFileChooser fc = new JFileChooser(new File("."));
    	fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);  
    	fc.addChoosableFileFilter(new XMLFilter());
        if (this.dialogSize != null) {
        	fc.setPreferredSize(this.dialogSize);
        }
        int result = fc.showDialog(this, "Open Rack XML File");   
    	this.dialogSize = fc.getSize();
    	if(result == JFileChooser.CANCEL_OPTION) return;
    	this.addRack(fc.getSelectedFile().getName());
	
    }
    
    
    private void copyModule(){
    	Module m = this.r.getSelectedModule();
    	if (m != null){
    		this.copiedModuleZipName = m.getModuleProperties().getModuleZipFilename();
    	}
    }
    
    private void pasteModule(){
    	String zipFilename = this.copiedModuleZipName;
    	if (zipFilename != null){
    		this.r.addModule(zipFilename);
    	}
    }
    
    private void deleteModule(){
    	Module m = this.r.getSelectedModule();
    	if (m != null){
    		this.r.remove(m);
    		m = null;
    		this.r.repaint();
    	}
    }
    
    private void editRackProperties(){
    	

   		RackPropertiesDialog dlg = new RackPropertiesDialog(this.r.getRackProperties(),this, "Edit Rack Properties", "table goes here...");    		
    	if (dlg.getExitWithOK()){
    		String propName = "rows";
    		String str = "";
    		try {
    			this.r.getRackProperties().setRows(Integer.parseInt(dlg.getProperty(propName).trim()));
    			propName = "cols";
    			this.r.getRackProperties().setCols(Integer.parseInt(dlg.getProperty(propName).trim()));
    			propName = "scale";
    			this.r.getRackProperties().setScale(Double.parseDouble(dlg.getProperty(propName).trim()));
    			propName = "imagesPath";
    			str = dlg.getProperty(propName).trim();
    			if (new File(str).exists()) {
    				this.r.getRackProperties().setImagesPath(str);
    			} else {
    				throw new FileNotFoundException("The directory '" + str + "' does not exist.");
    			}
    			propName = "modulesPath";
    			str = dlg.getProperty(propName).trim();
    			if (new File(str).exists()) {
    				this.r.getRackProperties().setModulesPath(str);
    			} else {
    				throw new FileNotFoundException("The directory '" + str + "' does not exist.");
    			}
    			propName = "rackImageFilename";
    			str = dlg.getProperty(propName).trim();
    			if (new File(this.r.getRackProperties().getImagesPath() + "/" + str).exists()) {
    				this.r.getRackProperties().setRackImageFilename(str);
    			} else {
    				throw new FileNotFoundException("The file '" + this.r.getRackProperties().getImagesPath() + "/" + str + "' does not exist.");
    			}
    			propName = "rackHP";
    			this.r.getRackProperties().setRackHP(Integer.parseInt(dlg.getProperty(propName).trim()));
    			this.r.save();
    			JOptionPane.showMessageDialog(this, "Changes will take effect the next time the rack is loaded.", "Rack Properties Saved",JOptionPane.PLAIN_MESSAGE);

    		} catch (FileNotFoundException e){
    			JOptionPane.showMessageDialog(frame, e.getMessage() + " Changes to rack properties not saved.", "Problem Saving Rack Properties",JOptionPane.ERROR_MESSAGE);  			
    		} catch (NullPointerException e){
    			JOptionPane.showMessageDialog(frame, "Property '" + propName + "' not found. Changes to rack properties not saved.", "Problem Saving Rack Properties",JOptionPane.ERROR_MESSAGE);
    		} catch (NumberFormatException e) {
    			JOptionPane.showMessageDialog(frame, "Property '" + propName + "' is not in the correct format. Changes to rack properties not saved.", "Problem Saving Rack Properties",JOptionPane.ERROR_MESSAGE);
    		} catch (Exception e) {
    			JOptionPane.showMessageDialog(frame, "Problem saving rack property '" + propName + ".' Changes to rack properties not saved.\n\nError details:.\n" + e, "Problem Saving Rack Properties",JOptionPane.ERROR_MESSAGE);
    		}	
    	}
    	dlg.dispose();
    	
    }
    
    
    private void addRack(String filename){
    	try {
	    	frame.r = new Rack(filename);  
	    	//Get the screen size  
	    	Toolkit toolkit = Toolkit.getDefaultToolkit();  
	    	Dimension screenSize = toolkit.getScreenSize(); 	    	
	    	int h = Math.min(screenSize.height - 50, frame.r.getHeight() + 60);
	    	int w;
	    	//Try to adjust for the presence of the vertical scrollbar.
	    	if (h == screenSize.height - 50){
	    		//The vertical scrollbar is visible.
	    		w = Math.min(screenSize.width - 50, frame.r.getWidth() + 30);
	    	} else {
	    		//The vertical scrollbar is not visible.
	    		w = Math.min(screenSize.width - 50, frame.r.getWidth() + 13);
	    	}
	  	    frame.setPreferredSize(new Dimension(w,h));
	  	    frame.getContentPane().removeAll();
	  	    frame.getContentPane().add( new JScrollPane(frame.r));  
	  	    frame.pack();
	  	    frame.setTitle(title + " (" + filename + ")");
    	} catch (OutOfMemoryError e) {
    		String msg = 	"Rack image is too large to create. Either use a smaller scale or increase the available memory.\n" +
							"\n" +
							"To increase the available memory:\n" +
							"Use -Xmx256m on the command line (increases available memory to 256MB).\n" +
							"Example:\n" +
							"C:\\WINNT\\system32\\java.exe -Xmx256m -jar RackPlanner.jar rack.xml";
    		JOptionPane.showMessageDialog(this, msg, "Java Memory Limit Exceeded", JOptionPane.ERROR_MESSAGE);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void main(String[] args) {
    	
    	String filename = "rack.xml"; //default
    	if (args.length > 0) {
    		filename = args[0];
    	}
	    //Make sure we have nice window decorations.
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    JDialog.setDefaultLookAndFeelDecorated(true);
    	RackPlanner frame = new RackPlanner(title);  	
    	frame.addRack(filename);
  	    frame.setVisible(true);
    }
	

	public RackPlanner(String title){
		super(title);		
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//       this.rackFilename = rackFilename;
        
        //Construct menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem mi = new JMenuItem("Add Module");
        mi.setAccelerator(KeyStroke.getKeyStroke('M',ActionEvent.ALT_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
               openAddModuleDialog();
            }
         });
        fileMenu.add(mi);
        
        fileMenu.add(new JSeparator());
        
        mi = new JMenuItem("Open");
        mi.setAccelerator(KeyStroke.getKeyStroke('O',ActionEvent.ALT_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
               openDialog();
               frame.repaint(); //some problems refreshing after reload, will this fix it?
            }
         });
        fileMenu.add(mi);
        fileMenu.add(new JSeparator());
        
        
        mi = new JMenuItem("Save");
        mi.setAccelerator(KeyStroke.getKeyStroke('S',ActionEvent.ALT_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
          
               frame.r.save();
            }
         });
        fileMenu.add(mi);
        fileMenu.add(new JSeparator());
        
        mi = new JMenuItem("Save As");
        mi.setAccelerator(KeyStroke.getKeyStroke('A',ActionEvent.ALT_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
            	saveAs();
               //frame.r.save();
            }
         });
        fileMenu.add(mi);
        fileMenu.add(new JSeparator());
        
        mi = new JMenuItem("Save As JPG");
        mi.setAccelerator(KeyStroke.getKeyStroke('J',ActionEvent.ALT_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
               saveJPG();
            }
         });
        fileMenu.add(mi);
        fileMenu.add(new JSeparator());
        mi = new JMenuItem("Exit");
        mi.setAccelerator(KeyStroke.getKeyStroke('X',ActionEvent.ALT_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
               System.exit(0);
            }
         });
        fileMenu.add(mi);
        JMenu editMenu = new JMenu("Edit");
        mi = new JMenuItem("Copy");
        mi.setAccelerator(KeyStroke.getKeyStroke('C',ActionEvent.CTRL_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
               
               copyModule();
            }
         });
        editMenu.add(mi);
        editMenu.add(new JSeparator());
        mi = new JMenuItem("Paste");
        mi.setAccelerator(KeyStroke.getKeyStroke('V',ActionEvent.CTRL_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {              
               pasteModule();
            }
         });
        editMenu.add(mi);
        
        editMenu.add(new JSeparator());
        mi = new JMenuItem("Delete");
        mi.setAccelerator(KeyStroke.getKeyStroke('D',ActionEvent.CTRL_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {              
               deleteModule();
            }
         });
        editMenu.add(mi);
        
        editMenu.add(new JSeparator());
        mi = new JMenuItem("Rack Properties");
        mi.setAccelerator(KeyStroke.getKeyStroke('R',ActionEvent.CTRL_MASK, false));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {         
               editRackProperties();
            }
         });
        editMenu.add(mi);
        
        JMenu helpMenu = new JMenu("Help");
        mi = new JMenuItem("About...");
        mi.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
            	String msg = "Rack Planner Copyright 2010 Doug Clauder (http://www.hevanet.com/dougcl/rp) \n" +   
            	"\n" +
            	"Rack Planner is free software: you can redistribute it and/or modify\n" + 
            	"it under the terms of the GNU General Public License as published by\n" +
            	"the Free Software Foundation, version 3 of the License.\n" + 
            	"\n" + 
            	"Rack Planner is distributed in the hope that it will be useful,\n" + 
            	"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" + 
            	"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" + 
            	"GNU General Public License for more details.\n" + 
            	"\n" + 
            	"You should have received a copy of the GNU General Public License\n" + 
            	"along with Rack Planner.  If not, see http://www.gnu.org/licenses\n";
            	JOptionPane.showMessageDialog(frame, msg, "About Rack Planner v" + RackPlanner.version,JOptionPane.PLAIN_MESSAGE);
            }
         });
        helpMenu.add(mi);
        JMenuBar bar = new JMenuBar();
        bar.add(fileMenu);
        bar.add(editMenu);
        bar.add(helpMenu);       
        setJMenuBar(bar);
	}
}
