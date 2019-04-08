package main.game.boards;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class CellSlotter<T> {
    private int minX, maxX, minY, maxY;
    private HashMap<Integer, HashMap<Integer, LinkedList<Slottable>>> cells = new HashMap<>();

    public void clear() {
        cells.clear();
        minX = 0;
        maxX = 0;
        minY = 0;
        maxY = 0;
    }

    public void addAll(Collection<T> items) {
        for(T s: items) {
            add((Slottable)s);
        }
    }

    public void addAll(T[] items) {
        for(T s: items) {
            add((Slottable)s);
        }
    }

    public void addAndUpdateAll(Collection<T> items, double cellSize) {
        for(T s: items) {
            Slottable slottable = (Slottable)s;
            slottable.updateSlotPositions(cellSize);
            add(slottable);
        }
    }

    public void addAndUpdateAll(T[] items, double cellSize) {
        for(T s: items) {
            Slottable slottable = (Slottable)s;
            slottable.updateSlotPositions(cellSize);
            add(slottable);
        }
    }

    public void add(Slottable item) {
        int slotMinX = item.getSlotMinX();
        int slotMaxX = item.getSlotMaxX();
        int slotMinY = item.getSlotMinY();
        int slotMaxY = item.getSlotMaxY();

        if(slotMinX < minX)
            minX = slotMinX;
        if(slotMaxX > maxX)
            maxX = slotMaxX;
        if(slotMinY < minY)
            minY = slotMinY;
        if(slotMaxY > maxY)
            maxY = slotMaxY;

        for(int x = slotMinX; x <= slotMaxX; x++) {
            HashMap<Integer, LinkedList<Slottable>> column = cells.get(x);
            if(column == null) {
                column = new HashMap<>();
                cells.put(x, column);
            }

            for(int y = slotMinY; y <= slotMaxY; y++) {
                LinkedList<Slottable> cell = column.get(y);
                if(cell == null) {
                    cell = new LinkedList<>();
                    column.put(y, cell);
                }

                cell.add(item);
            }
        }
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public HashMap<Integer, HashMap<Integer, LinkedList<Slottable>>> getCells() {
        return cells;
    }
}
