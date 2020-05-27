package edu.stanford.bmir.protege.examples.view;

import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cats.effect.IO;
import es.weso.rdf.RDFReader;
import es.weso.rdf.jena.RDFAsJenaModel;
import es.weso.rdf.nodes.IRI;
import es.weso.shapeMaps.ResultShapeMap;
import es.weso.shapeMaps.ShapeMap;
import es.weso.shex.ResolvedSchema;
import es.weso.shex.Schema;
import es.weso.shex.validator.Validator;
import es.weso.utils.eitherios.EitherIOUtils;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import scala.Option;


public class YASHE extends JPanel {
	

	 private static final Logger log = LoggerFactory.getLogger(YASHE.class);

	private static WebView browser;
	private static JFXPanel fxPanel;
	private JButton swingButton;
	private static WebEngine webEngine;

	/** for communication to the Javascript engine. */
	private JSObject javascriptConnector;

	/** for communication from the Javascript engine. */
	private JavaConnector javaConnector = new JavaConnector();

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
		
		log.info("create");

		StackPane stack = new StackPane();

		Scene scene = new Scene(stack);
		browser = new WebView();
		webEngine = browser.getEngine();
		// webEngine.load("http://www.weso.es/YASHE/");

		// set up the listener
		webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			if (Worker.State.SUCCEEDED == newValue) {
				// set an interface object named 'javaConnector' in the web engine's page
				JSObject window = (JSObject) webEngine.executeScript("window");
				window.setMember("javaConnector", javaConnector);

				// get the Javascript connector object.
				javascriptConnector = (JSObject) webEngine.executeScript("getJsConnector()");
			}
		});

		webEngine.load(
				"file:/C:/Users/pablo/OneDrive/Escritorio/protege-shex-client/index.html");
		// URL url = Metrics.class.getResource("./index.html");
		// webEngine.load(url.toString());
		stack.getChildren().add(browser);

		return (scene);
	}

	public class JavaConnector {

		public void validate(String rdfData, String schema, String shapeMap) {


			log.info("antes");
			//javascriptConnector.call("showResult", rdfData);
		
			IO<ResultShapeMap> validation = validateStr(rdfData, "", schema, shapeMap);
			
			log.info(""+validation);
			
		
			log.info("después");
			
		//	javascriptConnector.call("showResult", rdfData);
			
			try {
				log.info("try");
				ResultShapeMap result = validation.unsafeRunSync();
				log.info(result.toJson().spaces2());
				javascriptConnector.call("showResult", result.toJson().spaces2());
			} catch (Exception e) {
				log.info("error");
				System.out.println("error");
			}

		}

		public void log(String json) {

			System.out.println("visualize");
			System.out.println(json);

		}
		Option<IRI> none = Option.empty();
		Option<RDFReader> noneRDF = Option.empty();

		public IO<ResultShapeMap> validateStr(String dataStr, String ontologyStr, String schemaStr, String shapeMapStr) {
			log.info("validate");
			return readRDFStr(dataStr, "TURTLE").flatMap(rdfData -> readRDFStr(ontologyStr, "TURTLE")
					.flatMap(ontologyData -> rdfData.merge(ontologyData).flatMap(merged -> Schema
							.fromString(schemaStr, "SHEXC", none, noneRDF)
							.flatMap(schema -> EitherIOUtils
									.eitherStr2IO(ShapeMap.fromString(shapeMapStr, "Compact", none, merged.getPrefixMap(),
											schema.prefixMap()))
									.flatMap(shapeMap -> ShapeMap
											.fixShapeMap(shapeMap, merged, merged.getPrefixMap(), schema.prefixMap())
											.flatMap(fixedShapeMap -> ResolvedSchema.resolve(schema, none)
													.flatMap(resolvedSchema -> Validator
															.validate(resolvedSchema, fixedShapeMap, merged)
															.flatMap(result -> result.toResultShapeMap().flatMap(
																	resultShapeMap -> IO.pure(resultShapeMap))))))))));
		}

		public IO<ShapeMap> getShapeMap(String shapeMapFile, RDFReader rdf, Schema schema) {
			return getContents(shapeMapFile)
					.flatMap(shapeMapStr -> EitherIOUtils.eitherStr2IO(
							ShapeMap.fromString(shapeMapStr, "Compact", none, rdf.getPrefixMap(), schema.prefixMap())))
					.handleErrorWith(e -> IO.raiseError(new RuntimeException(
							"Cannot parse shapeMap from file: " + shapeMapFile + ": " + e.getMessage())));
		}

		public IO<RDFAsJenaModel> readRDF(String fileName, String format) {
			// log.info("Reading data file " + fileName);
			return RDFAsJenaModel.fromFile(Paths.get(fileName).toFile(), format, none).handleErrorWith(e -> IO
					.raiseError(new RuntimeException("Cannot parse RDF from file: " + fileName + ":" + e.getMessage())));
		}

		public IO<RDFAsJenaModel> readRDFStr(String str, String format) {
			log.info("read");
			return RDFAsJenaModel.fromChars(str, format, none).handleErrorWith(
					e -> IO.raiseError(new RuntimeException("Cannot parse RDF from str: " + str + ":" + e.getMessage())));
		}

		public IO<String> getContents(String fileName) {
			try {
				Path path = Paths.get(fileName);
				List<String> lines = Files.readAllLines(path);
				String str = String.join("\n", lines);
				return IO.apply(() -> str);
			} catch (IOException e) {
				return IO.raiseError(e);
			}
		}
		
	}


	
	
}


