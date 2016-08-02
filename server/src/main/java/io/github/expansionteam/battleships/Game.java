package io.github.expansionteam.battleships;

import io.github.expansionteam.battleships.engine.Board;
import io.github.expansionteam.battleships.engine.Field;
import org.apache.log4j.Logger;

import static io.github.expansionteam.battleships.engine.Board.*;

public class Game {

    private static final Logger log = Logger.getLogger(Game.class.getSimpleName());

    private final Board playerBoard = new BoardBuilder().build();
    private final Board enemyBoard = new BoardBuilder().build();


    public void start() {
        RandomShipGenerator rsg = new RandomShipGenerator();
        rsg.generateRandomShips(playerBoard);
    }

    public static void main(String[] args) {

        log.info("Logger test in server.");
        Game game = new Game();

        game.start();
        printTmp(game.playerBoard);
        game.playerBoard.shootField(new Field(1, 1));
        printTmp(game.playerBoard);
    }
}