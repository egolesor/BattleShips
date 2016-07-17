package io.github.expansionteam.battleships.engine;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Board implements Iterable<Field> {
    private static final int X = 10;    // constraint for x
    private static final int Y = 10;    // constraint for y
    private final Set<Field> board;   // board representation
    private final Set<Ship> ships = new HashSet<>();

    Board() {
        board = initializeSet();
    }

    private static Set<Field> initializeSet() {
        Set<Field> initialSet = new TreeSet<>();
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                initialSet.add( new Field(i, j) );
            }
        }
        return initialSet;
    }

    // PRINT HELPER - TO SEE WHAT HAPPENS ON THE BOARD :)
    /*
    private void printTmp() {
        Iterator<Field> iter = iterator();
        int n = 0;
        while (iter.hasNext()) {
            System.out.print( iter.next() + "  ");
            ++n;
            if (n%10 == 0)
                System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args) {
        Board b = new Board();
        b.appendShip(new Field(1,1), Orientation.HORIZONTAL, 4);
        b.printTmp();
        b.appendShip(new Field(7,0), Orientation.VERTICAL, 4);
        b.printTmp();
        b.appendShip(new Field(3,6), Orientation.VERTICAL, 3);
        b.printTmp();
    }
    */

    // add new Ship - null if it is not possible to append the ship
    // TODO: add return object to ships set
    // TODO: validate adjacent fields ( using validateSet() )
    Ship appendShip(Field startingField, Orientation orientation, int length) {
        Set<Field> setOfShipFields = generateSetOfFieldsForShip(startingField, orientation, length);
        if (setOfShipFields == null) {      // checks if the set is out of bounds
            return null;
        }
        // validate set of current ship-fields
        if (!validateSet( setOfShipFields ))
            return null;
        // validate adjacent fields



        Ship tmp = new Ship.ShipBuilder(setOfShipFields, length).build();
        //
        return tmp;
    }


    private boolean validateSet(Set<Field> set) {
        Set<Field> intersectedFields = set.stream().filter( checkIfFieldIsPartOfShip() ).collect(Collectors.toSet());
        return intersectedFields.size() == 0;
    }

    // checks if the current field is a part of existing ship
    private Predicate<Field> checkIfFieldIsPartOfShip() {
        return Field::isPartOfTheShip;
    }

    // generates set for the board (null if coordinates are reached)
    protected Set<Field> generateSetOfFieldsForShip(Field field, Orientation orientation, int length) {
        Set<Field> fields = new HashSet<>();

        while (length > 0) {
            Field fieldFromTheBoard = getFieldFromTheBoard( field );
            if (fieldFromTheBoard == null) {
                return null;
            }
            fields.add(fieldFromTheBoard);
            field = field.nextField( orientation );
            --length;
        }
        return fields;
    }

    // returns field from the board - to return pointer to the field on the board
    Field getFieldFromTheBoard(Field field) {
        for (Field f : board) {
            if ( f.equals( field ) ) {
                return f;
            }
        }
        return null;
    }

    // allows to iterate over Fields
    @Override
    public Iterator<Field> iterator() {
        return board.iterator();
    }
}
