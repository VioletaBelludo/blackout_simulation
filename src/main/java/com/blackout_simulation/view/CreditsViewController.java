package com.blackout_simulation.view;

import com.blackout_simulation.SimTron;
import javafx.fxml.FXML;

import java.io.IOException;

public class CreditsViewController {

    /**
     * It goes to the "main" scene.
     */
    @FXML
    public void backMain(){
        try{
            SimTron.main.goScene("main");
        }catch(IOException e){
            System.exit(1);
        }
    }

}
