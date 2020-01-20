package main;

import com.sun.javafx.application.PlatformImpl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class EmbeddedYASHE extends JPanel {
	
	private Stage stage;  
    private WebView browser;  
    private JFXPanel jfxPanel;  
    private JButton swingButton;  
    private WebEngine webEngine;  
  

    public EmbeddedYASHE(){  
        initComponents();  
    }  
  
    public static void main(String ...args){  
        // Run this later:
     
        final JFrame frame = new JFrame();  
         
        frame.getContentPane().add(new EmbeddedYASHE());  
                 
        frame.setMinimumSize(new Dimension(640, 480));  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setVisible(true);  
           
         
    } 
     
    private void initComponents(){  
         
        jfxPanel = new JFXPanel();  
        createScene();  
         
        setLayout(new BorderLayout());  
        add(jfxPanel, BorderLayout.CENTER);  
         
        
    }     
     
    /** 
     * createScene 
     * 
     * Note: Key is that Scene needs to be created and run on "FX user thread" 
     *       NOT on the AWT-EventQueue Thread 
     * 
     */  
    private void createScene() {  
        PlatformImpl.startup(new Runnable() {  
            @Override
            public void run() {  
                 
                stage = new Stage();  
                 
                stage.setTitle("Hello Java FX");  
                stage.setResizable(true);  
   
                Group root = new Group();  
                Scene scene = new Scene(root,80,20);  
                stage.setScene(scene);  
                 
                // Set up the embedded browser:
                browser = new WebView();
                webEngine = browser.getEngine();
                webEngine.load("http://www.weso.es/YASHE/");
                
                ObservableList<Node> children = root.getChildren();
                children.add(browser);                     
                 
                jfxPanel.setScene(scene);  
            }  
        });  
    }
    
   
}