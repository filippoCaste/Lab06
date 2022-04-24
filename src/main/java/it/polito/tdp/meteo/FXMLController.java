/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	this.txtResult.clear();
    	int mese = this.boxMese.getValue();
    	// controllo che abbia selezionato un valore
    	if(!this.isMeseSelezionato(mese))
    		return;
    	txtResult.appendText(this.model.trovaSequenza(mese));
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	this.txtResult.clear();
    	int mese = this.boxMese.getValue();
    	// controllo che abbia selezionato un valore
    	if(!this.isMeseSelezionato(mese))
    		return;
    	
    	txtResult.appendText("Di seguito viene indicata l'umidità media per le citta' presenti nel db:\n" +
    			this.model.getUmiditaMedia(mese));

    }
    private boolean isMeseSelezionato(Integer i) {
    	if(i==null) {
    		txtResult.appendText("Devi prima selezionare un mese!");
    		return false;
    	}
    	return true;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        boxMese.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12);
    }

	public void setModel(Model m) {
		this.model = m;		
	}
}

