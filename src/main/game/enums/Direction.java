package main.game.enums;

public enum Direction {
    RIGHT (0),
    UP (1),
    LEFT (2),
    DOWN (3);

    public int i;
    Direction(int index) {
        this.i = index;
    }
}
