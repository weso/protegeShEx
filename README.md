# protegeShEx - Protégé plugin for ShEx
<img src="https://img.shields.io/badge/work-inProgress-green" alt="size"/>

This plugin adds [ShEx](http://shex.io/) features to the [Protégé](https://protege.stanford.edu/) editor in order to allow the users to validate their ontologies using ShEx. To do that, this plugin incorporate [YASHE](http://www.weso.es/YASHE/) editor, offering a better experience to the users.

<p align="center">
  <img src="https://github.com/weso/protegeShEx/blob/master/docs/protegeShExPlugin.png" alt="size"/>
</p>


## Use the plugin
1. Get a copy of the example code:

        git clone https://github.com/weso/protegeShEx.git
        
2. Change into the protegeShEX directory.

3. Type mvn clean package.  On build completion, the "target" directory will contain a protege.plugin.examples-${version}.jar file.

4. Copy the JAR file from the target directory to the "plugins" subdirectory of your Protege distribution.

As YASHE is built in JavaScript  we use the JavaFX library in order to re-use YASHE code. This library doesn´t work at all with protege at the moment ([more info](http://protege-project.136.n4.nabble.com/ShEx-Editor-Plugin-td4673267.html)) and while we look for another more elegant solution we need to do the following steps:

 5. Change into Protege/conf directory
 
 6. Open in a text editor the config.xml file
 
 7. Add the following line in the frameworkProperties:
  
        <property name="org.osgi.framework.system.packages.extra" 
        value="javax.xml.parsers,org.xml.sax,org.xml.sax.ext,org.xml.sax.helpers,javafx.collections,javafx.embed.swing,javafx.geometry,javafx.scene,javafx.scene.text,javafx.scene.layout,javafx.scene.paint,javafx.scene.web,javafx.application,javafx.beans.property,javafx.beans.value"/> 

