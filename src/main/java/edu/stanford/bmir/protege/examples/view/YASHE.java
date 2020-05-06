package edu.stanford.bmir.protege.examples.view;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class YASHE extends JPanel {
	
	private static final Logger log = LoggerFactory.getLogger(ExampleViewComponent.class);

	private static WebView browser;
	private static JFXPanel fxPanel;
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

	private void initAndShowGUI() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX(fxPanel);
			}
		});
	}

	private void initFX(JFXPanel fxPanel) {
		// This method is invoked on the JavaFX thread
		Scene scene = createScene();
		fxPanel.setScene(scene);
	}

	private Scene createScene() {

		StackPane stack = new StackPane();

		Scene scene = new Scene(stack);
		browser = new WebView();
		webEngine = browser.getEngine();
		
	

	
	    InputStream in = getClass().getResourceAsStream("/edu/stanford/bmir/protege/examples/view/index.html"); 
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		StringBuilder content = new StringBuilder();
		String line;
	    try {
			while ((line = reader.readLine()) != null) {
			    content.append(line);
			    content.append(System.lineSeparator());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    
	     webEngine.loadContent(content.toString());
	     
	    

		//webEngine.load(getClass().getResourceAsStream("/edu/stanford/bmir/protege/examples/view/index.html").toString()); //does not work...
		stack.getChildren().add(browser);

		return (scene);
	}

}
