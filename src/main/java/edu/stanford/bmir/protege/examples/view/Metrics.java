package edu.stanford.bmir.protege.examples.view;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;


import javafx.embed.swing.JFXPanel;


public class Metrics extends JPanel {

    private JButton refreshButton = new JButton("Refresh");

    private JLabel textComponent = new JLabel();

    private OWLModelManager modelManager;
    


    private ActionListener refreshAction = e -> recalculate();
    
    private OWLModelManagerListener modelListener = event -> {
        if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
            recalculate();
        }
    };
    
    public Metrics(OWLModelManager modelManager) {
    	this.modelManager = modelManager;
        recalculate();
        
        modelManager.addListener(modelListener);
        refreshButton.addActionListener(refreshAction);
        
        add(textComponent);
        add(refreshButton);

        JFXPanel jfxPanel = new JFXPanel();
//        jfxPanel.setBorder(new LineBorder(new Color(0, 0, 0), 6));
//        createScene();  
//         
//        setLayout(new BorderLayout());  
        //add(jfxPanel, BorderLayout.CENTER);
        

    }
    
//    private void createScene() {  
//        PlatformImpl.startup(new Runnable() {  
//            @Override
//            public void run() {  
//                 
//                stage = new Stage();  
//                 
//                stage.setTitle("Hello Java FX");  
//                stage.setResizable(true);  
//   
//                Group root = new Group();  
//                Scene scene = new Scene(root,80,20);  
//                stage.setScene(scene);  
//                 
//                // Set up the embedded browser:
//                browser = new WebView();
//                URL url = this.getClass().getResource("./index.html");
//                browser.getEngine().load(url.toString());
//
//                
//                ObservableList<Node> children = root.getChildren();
//                children.add(browser);                     
//                 
//                jfxPanel.setScene(scene);  
//            }  
//        });  
//    }
    
    public void dispose() {
        modelManager.removeListener(modelListener);
        refreshButton.removeActionListener(refreshAction);
    }
    
    private void recalculate() {
        int count = modelManager.getActiveOntology().getClassesInSignature().size();
        if (count == 0) {
            count = 1;  // owl:Thing is always there.
        }
        textComponent.setText("Total classes = " + count);
    }
}
