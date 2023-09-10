package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class ComputerRoomController {
    
    @FXML private Rectangle toLockedRoom;

    @FXML
    public void enterLockedRoom(MouseEvent event) {
        App.setUi("lockedroom");
    }

    @FXML
    public void highlight(){
        toLockedRoom.setOpacity(0.7);
    }

    @FXML 
    public void removeHighlight(){
        toLockedRoom.setOpacity(0.4);
    }
}
