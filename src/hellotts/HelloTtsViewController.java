/*
 * Copyright © 2016 Toru Takahahshi. All rights reserved.
 */
package hellotts;

import java.beans.PropertyVetoException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javax.speech.AudioException;
import javax.speech.EngineException;

/**
 *
 * @author toru
 */
public class HelloTtsViewController implements Initializable {
    private SpeechModel speechModel;
    
    @FXML
    private Label statusLabel;
    @FXML
    private TextArea speechTextArea;
    @FXML
    private void handleSpeechAction(ActionEvent event) {
        String textToSpeech = speechTextArea.getText();
        try {
            speechModel.doSpeech(textToSpeech);
        } catch (InterruptedException ex) {
            statusLabel.setText("ERROR: speech error" + ex.getLocalizedMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        speechModel = SpeechModel.getInstance();
        try {
            speechModel.init("kevin16");
        } catch (EngineException | AudioException | PropertyVetoException ex) {
            Logger.getLogger(HelloTtsViewController.class.getName()).log(Level.SEVERE, null, ex);
            statusLabel.setText("ERROR: SpeechEngine initialization:" + ex.getLocalizedMessage());
        }
    }    
    
}
