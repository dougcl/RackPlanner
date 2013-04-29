############# Installation #############

1) Unzip the file into a new local directory preserving subfolders. 
2) Double click on RackPlanner.jar... if that doesn't work (it should), then 
open a command prompt in the directory where RackPlanner.jar is and type: 

java -jar RackPlanner.jar 

3) If that doesn't work, then you need to install the Java runtime standard edition here: 
http://java.sun.com/javase/downloads/index.jsp 

############# Operation #############

rack.xml: 
Describes the rack, including the modules in the rack when the rack was last saved.
By default RackPlanner runs using rack.xml in the root directory. You can specify an 
alternative as a command line parameter at startup.If you're on Windows, you can set 
up a shortcut with target: "java.exe" -jar RackPlanner.jar myrack.xml 
to load up a given rack configuration (myrack.xml for example) automatically. 
The properties section of this file can be edited prior to startup:
<scale> a decimal number that scales (zooms) the size of the entire rack and its modules. 1.0 is normal. 0.5 is half size, 2.0 is double, and so on.
<rows> An integer that determines how many times to repeat the rack vertically. Note the background image will extend beyond this, but it can't be populated. Choose small numbers to avoid memory problems.
<cols> An integer that determines how many times to repeat the rack horizontally. Note the background image will extend beyond this, but it can't be populated. Choose small numbers to avoid memory problems.
<imagesPath> A string representing the directory where the rack background image is (or are).
<modulesPath> A string representing the directory where the module zip files live.
<rackImageFilename> A string indicating the filename of the rack background image in the <images> directory.
<rackHP> This is the number of possible module positions in a single rack. Examples:
Let's say there are 20 possible positions in a row of a Frac rack and the smallest Frac module consumes 2 positions. Then HP for the rack = 20, and the smallest Frac module has an HP of 2. 
Let's say one Serge panel consumes an entire row of a Serge rack. Then the HP for the rack = 1, and every module has an HP of 1. 
Let's say Modcan has 7 possible positions and the smallest module consumes a full position. Then the rack HP is 7 and the smallest module HP is 1. Double modules would have an HP of 2. 
In the case of Euro, there are 84 possible module positions and the smallest module consumes 4 positions. The HP for the rack = 84 and the smallest module has an HP = 4. 
Let's say you figure your rack has 10 possible positions and some of your modules consume 1/2 a position. Your rack HP needs to be increased to 20 so your smallest module HP becomes 1. 
<modules> The modules section of this file is probably best left to RackPlanner, but you can edit it yourself too. Just don't leave a <modules></modules> empty pair because the program is currently sensitive to that (soon to be fixed). If you don't want any modules, delete the entire <modules> structure. 

Example of rack.xml:
<?xml version="1.0" encoding="ISO-8859-1"?>
<rack>
	<properties>
		<scale>1.0</scale>
		<rows>8</rows>
		<cols>4</cols>
		<imagesPath>./images</imagesPath>
		<modulesPath>./modules</modulesPath>
		<rackImageFilename>euro_84HP.jpg</rackImageFilename>
		<rackHP>84</rackHP>
	</properties>
</rack>

Modules:
Each module is a zip file in the <modulesPath> (see rack.xml) directory. There are two files that must be in each zip:
module.xml, and the module image. 

module.xml file: 
Describes the module. Must be named module.xml
<manuf> A string representing the module manufacturer
<model> A string representing the module model number or other vendor identifier.
<HP> An integer representing the width of the module. Note that the meaning of HP depends on the <rackHP> setting in rack.xml. 
<mA> An integer representing the expected current consumption
<priceUSD> An integer (no decimal places). Representing the expected module price in USD.
<priceEUR> An integer (no decimal places). Representing the expected module price in EUR.
<priceYEN> An integer (no decimal places). Representing the expected module price in Japanese YEN.
<moduleImageFilename> A string representing the name of the module image file. It can be any filename, but has to match what is loaded in the zip.

Example of module.xml:
<?xml version="1.0" encoding="ISO-8859-1"?> 
<module>
	<properties>
		<manuf>Cwejman</manuf>
		<model>MX-4S</model>
		<HP>20</HP>
		<mA>40</mA>
		<priceUSD>775</priceUSD>
		<priceEUR>600</priceEUR>
		<moduleImageFilename>Cwejman_MX-4S.jpg</moduleImageFilename>
	</properties>
	<forum_url>www.muffwiggler.com/forum</forum_url>	
</module>

Module image file: Can be jpg, gif (tested) and possibly others (png probably works).

Note, there a reserved characters in XML: 
Character	Meaning		Use instead
----------------------------------------------
>		Greater than	&gt;
<		Less than	&lt;
&		Ampersand	&amp;
%		Percent		&#37; 
Example: instead of <model>A-148 S&H</model>, 
use <model>A-148 S&amp;H</model> 


############# Known Issues #############
Rack Planner may require a lot of memory if your rack is huge and/or if you want to use a large scale. So you may get an error message about the rack being too large. If this happens, you can either reduce the scale, split your rack into smaller chunks, or increase the amount of memory available to java. To increase the amount of memory, you must get "-Xmx256m" on to the command line. This probably means nothing to most of you, but here are several options (in order of ease):
1) Create a shortcut to RackPlanner.jar. Edit the shortcut properties and add -Xmx256m to the command line. On Windows this should look something like this:
C:\WINDOWS\system32\javaw.exe -Xmx256m -jar RackPlanner.jar 
On a Mac, this is apparently a matter of creating an Apple Script, because you can't edit the command line in aliases. Mac users have to go to the next option.
2) Create a batch file or script (Apple Script on a Mac) to call RackPlanner.jar and run it instead of RackPlanner.jar. In Windows, use notepad to create a text file called RackPlanner.bat in the same directory as RackPlanner.jar. The file should contain the following text:
javaw.exe -Xmx256m -jar RackPlanner.jar 
3) Always use a command line terminal to launch RackPlanner.jar with the -Xmx256m option.
4) There should be a way to make Java always start up using the -Xmx256m option, but I'm not sure how. It looks like the Java control panel (under Control Panel on Windows) supports it, but I haven't had luck making he change permanent.

Some modules will load up blank because RackPlanner uses the javax.imageio class
which can be really picky about JPG encoding. If you are having trouble getting 
your module to appear, and you have looked at everything else (usually it's a
problem in the module.xml file) you might consider loading the jpg into a paint 
program, resizing it and saving it to get it re-encoded. In the meantime I will 
look into a more robust solution
