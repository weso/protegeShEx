package edu.stanford.bmir.protege.examples.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.awt.GridLayout;
import javafx.scene.layout.StackPane;

public class YASHE extends JPanel {

    private Stage stage;  
    private static WebView browser;  
    private static JFXPanel  fxPanel;  
    private JButton swingButton;  
    private static WebEngine webEngine;
  

    public YASHE() {
    	setBackground(java.awt.Color.WHITE);
        
        fxPanel = new JFXPanel();
        fxPanel.setBackground(java.awt.Color.WHITE);
        
       
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
        setLayout(new GridLayout(0, 1, 0, 0));
        
        add(fxPanel);  
        
 
    }
    
    private static void initAndShowGUI() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
       });
    }
    
   
    
    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }
    
    private static Scene createScene() {
    	
    	StackPane stack = new StackPane();
       
        Scene  scene  =  new  Scene(stack, Color.ALICEBLUE);
        
        
        browser = new WebView();
        webEngine = browser.getEngine();
        //webEngine.load("http://www.weso.es/YASHE/");
       
		webEngine.load("file:/C:/Users/pablo/OneDrive/Escritorio/Plugin/protege-plugin-examples/src/main/java/edu/stanford/bmir/protege/examples/view/index.html");
		//URL url = Metrics.class.getResource("./index.html");
       // webEngine.load(url.toString());
		stack.getChildren().add(browser);

        return (scene);
    }
    
    /*
     * 
     * private static Scene createScene() {
    	StackPane stack = new StackPane();
        Scene  scene  =  new  Scene(stack);
        
        
        browser = new WebView();
        webEngine = browser.getEngine();
        //webEngine.load("http://www.weso.es/YASHE/");
       
		webEngine.load("file:/C:/Users/pablo/OneDrive/Escritorio/Plugin/protege-plugin-examples/src/main/java/edu/stanford/bmir/protege/examples/view/index.html");
		//URL url = Metrics.class.getResource("./index.html");
       // webEngine.load(url.toString());
		stack.getChildren().add(browser);

        return (scene);
    }*/
     
}
