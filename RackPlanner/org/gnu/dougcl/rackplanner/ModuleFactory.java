/**
   org.gnu.java.music.rackplanner.ModuleFactory.java
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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.awt.Image;



/** 
 * @author dclauder
 *
 */
public class ModuleFactory {
		private Hashtable<String, ModuleProperties> mpTable = new Hashtable<String, ModuleProperties>();
		private Rack r;
		private String modulesPath;
		

		public ModuleFactory(Rack r, String modulesPath){
			super();
			this.r = r;
			this.modulesPath = modulesPath;
		}
		

		
		//Rack scaling is supported. This replaces the existing image with a new scaled image.
		//The thought is that most images will tend toward high resolution, and scaling down will
		//be more common than otherwise. It will save space to scale down and trash the original hi res img.
		public void resize(ModuleProperties mp){
     		int w = r.getDimensionFromRowsAndCols(1, mp.getHP()).width;
			Image img = mp.getImg().getScaledInstance(w,-1,Image.SCALE_SMOOTH);
	        int h = img.getHeight(null);
	        BufferedImage scaled = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);            
	        Graphics2D g = scaled.createGraphics();
	        g.drawImage(img,0,0,null);
	        if(g != null) g.dispose();
	        mp.setImg(scaled);
		}
		
		public Module getModule(String name){
			Module m = new Module();
			m.addModuleListener(r);
			ModuleProperties mp = mpTable.get(name);
			if (mp == null) {
				//only load up new module from disk if not already loaded.
				mp = new ModuleProperties(this.modulesPath + "/" + name);
				if (mp.getImg()!= null){
					resize(mp);
				}
				mp.setModuleZipFilename(name);
				mpTable.put(name, mp);	
			} 
			m.setModuleProperties(mp);
			m.setSize(r.getDimensionFromRowsAndCols(1, mp.getHP()));
			return m;
		}	

		
		
}

