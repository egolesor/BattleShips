package io.github.expansionteam.battleships.gui.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import io.github.expansionteam.battleships.common.annotations.EventConsumer;
import io.github.expansionteam.battleships.common.annotations.EventProducer;
import io.github.expansionteam.battleships.common.events.*;
import io.github.expansionteam.battleships.gui.models.*;
import io.github.expansionteam.battleships.gui.models.Ship;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

@EventProducer
@EventConsumer
public class BattleshipsController implements Initializable {

    private final static Logger log = Logger.getLogger(BattleshipsController.class);

    @Inject
    EventBus eventBus;

    @Inject
    BoardFactory boardFactory;

    @Inject
    EventDataConverter eventDataConverter;

    @FXML
    BorderPane boardArea;

    @FXML
    VBox opponentBoardArea;

    @FXML
    VBox playerBoardArea;

    OpponentBoard opponentBoard;
    PlayerBoard playerBoard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        opponentBoard = boardFactory.createEmptyOpponentBoard();
        opponentBoardArea.getChildren().add(opponentBoard);

        playerBoard = boardFactory.createEmptyPlayerBoard();
        playerBoardArea.getChildren().add(playerBoard);

        boardArea.setVisible(false);

        log.debug("Post StartGameEvent.");
        eventBus.post(new StartGameEvent());
    }

    @Subscribe
    public void handleOpponentArrivedEvent(OpponentArrivedEvent event) {
        log.debug("Handle OpponentArrivedEvent.");

        boardArea.setVisible(true);

        log.debug("Post GenerateShipsEvent.");
        eventBus.post(new GenerateShipsEvent());
    }

    @Subscribe
    public void handleShipsGeneratedEvent(ShipsGeneratedEvent event) {
        log.debug("Handle ShipsGeneratedEvent.");

        event.getShips().stream().forEach(s -> {
            Ship ship = eventDataConverter.convertShipDataToShipGuiModel(s);
            playerBoard.placeShip(ship);
        });
    }

    @Subscribe
    public void handleEmptyFieldHitEvent(EmptyFieldHitEvent event) {
        log.debug("Handle EmptyFieldHitEvent.");
        opponentBoard.fieldWasShotAndMissed(Position.of(event.getPosition().getX(), event.getPosition().getY()));
    }

    @Subscribe
    public void handleShipHitEvent(ShipHitEvent event) {
        log.debug("Handle ShipHitEvent.");
        opponentBoard.fieldWasShotAndHit(Position.of(event.getPosition().getX(), event.getPosition().getY()));
    }

    @Subscribe
    public void handleShipDestroyedEvent(ShipDestroyedEvent event) {
        log.debug("Handle ShipDestroyedEvent.");

        opponentBoard.fieldWasShotAndHit(Position.of(event.getPosition().getX(), event.getPosition().getY()));
        event.getAdjacentPositions().stream().forEach(p -> opponentBoard.fieldWasShotAndMissed(Position.of(p.getX(), p.getY())));
    }

}