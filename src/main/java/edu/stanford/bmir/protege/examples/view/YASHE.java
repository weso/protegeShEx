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


import es.weso.rdf.Prefix;

public class YASHE extends JPanel {
	

	//Prefix p = new Prefix("xsd");
	
	private static WebView browser;
	private static JFXPanel fxPanel;
	private JButton swingButton;
	private static WebEngine webEngine;

	/** for communication to the Javascript engine. */
//	private JSObject javascriptConnector;
//
//	/** for communication from the Javascript engine. */
//	private JavaConnector javaConnector = new JavaConnector();;

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
		// webEngine.load("http://www.weso.es/YASHE/");

		// set up the listener
//		webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//			if (Worker.State.SUCCEEDED == newValue) {
//				// set an interface object named 'javaConnector' in the web engine's page
//				JSObject window = (JSObject) webEngine.executeScript("window");
//				window.setMember("javaConnector", javaConnector);
//
//				// get the Javascript connector object.
//				javascriptConnector = (JSObject) webEngine.executeScript("getJsConnector()");
//			}
//		});

		webEngine.load(
				"file:/C:/Users/pablo/OneDrive/Escritorio/Plugin/protege-plugin-examples/src/main/java/edu/stanford/bmir/protege/examples/view/index.html");
		// URL url = Metrics.class.getResource("./index.html");
		// webEngine.load(url.toString());
		stack.getChildren().add(browser);

		return (scene);
	}

//	public class JavaConnector {
//
//		public void validate(String rdfData, String schema, String shapeMap) {
//
//			Validate v = new Validate();
//
//			IO<ResultShapeMap> validation = v.validateStr(rdfData, "", schema, shapeMap);
//
//			try {
//				ResultShapeMap result = validation.unsafeRunSync();
//				System.out.println("ResultShapeMap: " + result.toJson().spaces2());
//				javascriptConnector.call("showResult", result.toJson().spaces2());
//			} catch (Exception e) {
//				System.out.println("error");
//			}
//
//		}
//
//		public void log(String json) {
//
//			System.out.println("visualize");
//			System.out.println(json);
//
//		}
//	}

}
