package edu.stanford.bmir.protege.examples.view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Metrics extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(Metrics.class);
	
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
    	setBackground(java.awt.Color.ORANGE);
    	this.modelManager = modelManager;
        recalculate();
        
        modelManager.addListener(modelListener);
        refreshButton.addActionListener(refreshAction);
        setLayout(new GridLayout(0, 1, 0, 0));
        
        add(new Yashe());  
 
    }
 
    
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
