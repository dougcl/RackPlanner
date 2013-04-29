/**
   org.gnu.java.music.rackplanner.ModuleProperties.java
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

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.zip.*;
import java.util.Enumeration;
import java.lang.reflect.Field;


public class ModuleProperties {
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	
	//Begin XML Properties
	private int HP = 14;
	private String moduleImageFilename; 
	private int mA = 0;
	private String manuf = "None";
	private String model = "None";
	private int priceUSD = 0;
	private int priceEUR = 0;
	private int priceYEN = 0;
	//End XML Properties
	
	private String moduleZipFilename;

	public void setModuleZipFilename(String filename){
		this.moduleZipFilename = filename;
	}
	
	public String getModuleZipFilename(){
		return this.moduleZipFilename;
	}
	
	public BufferedImage getImg(){
		return this.img;
	}
	public void setImg(BufferedImage img){
		this.img = img;
	}
	
	public int getHP(){
		return this.HP;
	}
	
	public void setHP(int HP){
		this.HP = HP;
	}
	
	//Current draw in mA
	public int getMA(){
		return this.mA;
	}
	
	//	Current draw in mA
	public void setMA(int mA){
		this.mA = mA;
	}
	
	public String getManuf(){
		return this.manuf;
	}
	
	public void setManuf(String manuf){
		this.manuf = manuf;
	}
	
	public String getModel(){
		return this.model;
	}
	
	public void setModel(String model){
		this.model = model;
	}
	
	public int getPriceUSD(){
		return this.priceUSD;
	}
	
	public void setPriceUSD(int priceUSD){
		this.priceUSD = priceUSD;
	}
	
	public int getPriceEUR(){
		return this.priceEUR;
	}
	
	public void setPriceEUR(int priceEUR){
		this.priceEUR = priceEUR;
	}
	
	public int getPriceYEN(){
		return this.priceYEN;
	}
	
	public void setPriceYEN(int priceYEN){
		this.priceYEN = priceYEN;
	}
	

	public ModuleProperties(){
		super();

	}
	
	public ModuleProperties(String filename){
		super();
		//Unzip the module zip file and extract module.xml and the jpg/gif.
		try {
  		  	//int count;
  		  	//byte data[] = new byte[BUFFER];
	  		  try {
	  			ZipEntry entry;
				ZipFile zipfile = new ZipFile(filename);
				//Won't have to search through if we can avoid the full path returned by entry.getName();
				//Too late to look further. This works for now.
				Enumeration e = zipfile.entries();
				while(e.hasMoreElements()) {
					entry = (ZipEntry) e.nextElement();
					if ((entry.getName().indexOf("/module.xml")> -1) ||(entry.getName().compareTo("module.xml") == 0)){
						Field fld;
						String val;
						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder db = dbf.newDocumentBuilder();
						Document doc = db.parse(zipfile.getInputStream(entry));
						doc.getDocumentElement().normalize();
						NodeList properties = doc.getElementsByTagName("properties");
						properties = properties.item(0).getChildNodes();
						for (int s = 0; s < properties.getLength(); s++) { 
							if (properties.item(s).getNodeType() == Node.ELEMENT_NODE) {
								//This block is where reflection is used to set the field values.
								try {
									fld = this.getClass().getDeclaredField(properties.item(s).getNodeName());
									if (properties.item(s).hasChildNodes()){
						    			val = properties.item(s).getFirstChild().getNodeValue();
						    			//Only integers and strings are allowed thus far. 
						    			//Easy to add more, just copy the int.class code here to double.class, etc.
						    			if (fld.getType().equals(int.class)){
						    				fld.set(this, Integer.valueOf(val));
						    			} else {
						    				fld.set(this, val);
						    			}
									}
								} catch (NoSuchFieldException e1){
									  //Do not attempt to deal with unknown properties in the xml file.
								}
							}
						}					
					}
				}
				//Now loop through and get the image file.
				e = zipfile.entries();
				while(e.hasMoreElements()) {
					entry = (ZipEntry) e.nextElement();
					if ((entry.getName().indexOf("/" + this.moduleImageFilename)> -1) || (entry.getName().compareTo(this.moduleImageFilename)==0)){
						img = ImageIO.read(zipfile.getInputStream(entry));	
					}
				}
	  	      } catch(Exception e) {
	  	         e.printStackTrace();
	  	      }
		} catch(Exception e) {
	         e.printStackTrace();
	    }
	}
}
		
  	     