package org.gnu.dougcl.rackplanner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.awt.Color;


public class RackPropertiesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private RackProperties rp;
	private JTable propertiesTable = null ;
	private DefaultTableModel propertiesTableModel = null;
	private boolean exitWithOK = false;

	
public RackPropertiesDialog(RackProperties rp, JFrame parent, String title, String message) {
    super(parent, title, true);
    this.rp = rp;
    int w = 600;
    int h = 300;
    
    if (parent != null) {
      Dimension parentSize = parent.getSize(); 
      Point p = parent.getLocation(); 
      setLocation(p.x + parentSize.width/2 - w/2 , p.y + parentSize.height /2  - h/2);
    }
    this.setPreferredSize(new Dimension(w,h));

    JScrollPane scrollPane = new JScrollPane(getPropertiesTable());
    getContentPane().add(scrollPane);
    JPanel buttonPane = new JPanel();
    JButton button = new JButton("OK"); 
    buttonPane.add(button); 
    button.addActionListener(this);
    button = new JButton("Cancel"); 
    buttonPane.add(button); 
    button.addActionListener(this);
    getContentPane().add(buttonPane, BorderLayout.SOUTH);
    setDefaultCloseOperation(HIDE_ON_CLOSE);
    pack(); 
    setVisible(true);

  }
  
  public boolean getExitWithOK(){
	  return exitWithOK;
  }

  public void actionPerformed(ActionEvent e) {
	if (getPropertiesTable().isEditing()) {
			getPropertiesTable().getCellEditor().stopCellEditing();	
	}
	if (e.getActionCommand().compareTo("OK") == 0){
		exitWithOK = true;
	    setVisible(false); 	
	    //	  dispose();
	} else if (e.getActionCommand().compareTo("Cancel") == 0) {
		setVisible(false);
	}
  }
  
	private JTable getPropertiesTable(){
		if (propertiesTable == null) {
			propertiesTable = new JTable();
			propertiesTable.setModel(getPropertiesTableModel());
			propertiesTable.setGridColor(Color.LIGHT_GRAY);
			propertiesTable.setEnabled(true);
			propertiesTable.setVisible(true);
			//propertiesTable.setAutoCreateRowSorter(true);
			/*
			try {
				//this doesn't work on a mac?
				propertiesTable.setFillsViewportHeight(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this.myParent, e, "debug",JOptionPane.PLAIN_MESSAGE);
			}
			*/
		}
		
		return propertiesTable;
	}
	
	private DefaultTableModel getPropertiesTableModel() {
		if (propertiesTableModel == null) {
			propertiesTableModel = new DefaultTableModel();
			propertiesTableModel.addColumn("Property");
			propertiesTableModel.addColumn("Value");
			Vector<String> row = new Vector<String>(); row.add("rows"); row.add(Integer.toString(this.rp.getRows()));	
			propertiesTableModel.addRow(row);
			row = new Vector<String>(); row.add("cols"); row.add(Integer.toString(this.rp.getCols()));
			propertiesTableModel.addRow(row);
			row = new Vector<String>(); row.add("scale"); row.add(Double.toString(this.rp.getScale()));
			propertiesTableModel.addRow(row);
			row = new Vector<String>(); row.add("imagesPath"); row.add(this.rp.getImagesPath());
			propertiesTableModel.addRow(row);
			row = new Vector<String>(); row.add("modulesPath"); row.add(this.rp.getModulesPath());
			propertiesTableModel.addRow(row);
			row = new Vector<String>(); row.add("rackImageFilename"); row.add(this.rp.getRackImageFilename());
			propertiesTableModel.addRow(row);
			row = new Vector<String>(); row.add("rackHP"); row.add(Integer.toString(this.rp.getRackHP()));
			propertiesTableModel.addRow(row);
		}
		return propertiesTableModel;
	}
	
	public String getProperty(String prop) {
		String retVal = null;
		Vector table = new Vector();
		table = getPropertiesTableModel().getDataVector();
		Vector row = new Vector(); 
		int i = 0;
		boolean found = false;
		while (i<table.size() && !found){
			row = (Vector)table.elementAt(i);
			if (((String)row.elementAt(0)).compareTo(prop) == 0) {
				retVal = (String)row.elementAt(1);
				found = true;
			}
			i = i + 1;
		}
		if (!found){
			retVal = null;
		//	throw new Exception("Property \"" + prop + "\" not found in table.");
		}
		return retVal;
	}
	
}
