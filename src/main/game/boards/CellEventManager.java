package main.game.boards;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class CellEventManager<S, T> {
    public void callEvents(CellSlotter<S> cellSlotter1, CellSlotter<T> cellSlotter2) {
        HashMap<Integer, HashMap<Integer, LinkedList<Slottable>>> cells1 = cellSlotter1.getCells();
        HashMap<Integer, HashMap<Integer, LinkedList<Slottable>>> cells2 = cellSlotter2.getCells();

        int minX = Math.max(cellSlotter1.getMinX(), cellSlotter2.getMinX());
        int maxX = Math.min(cellSlotter1.getMaxX(), cellSlotter2.getMaxX());
        int minY = Math.max(cellSlotter1.getMinY(), cellSlotter2.getMinY());
        int maxY = Math.min(cellSlotter1.getMaxY(), cellSlotter2.getMaxY());

        for(int x = minX; x <= maxX; x++) {
            HashMap<Integer, LinkedList<Slottable>> col1 = cells1.get(x);
            HashMap<Integer, LinkedList<Slottable>> col2 = cells2.get(x);

            if(col1 != null && col2 != null) {
                for (int y = minY; y <= maxY; y++) {
                    LinkedList<Slottable> cell1 = col1.get(y);
                    LinkedList<Slottable> cell2 = col2.get(y);

                    if(cell1 != null && cell2 != null) {
                        for(Slottable s1: cell1) {
                            for(Slottable s2: cell2) {
                                event((S)s1, (T)s2);
                            }
                        }
                    }
                }
            }
        }
    }

    public abstract void event(S item1, T item2);
}
