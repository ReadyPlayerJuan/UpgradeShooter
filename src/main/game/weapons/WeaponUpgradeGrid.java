package main.game.weapons;

import main.util.ExpandableGrid;
import main.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WeaponUpgradeGrid {
    private int usedSlots, maxSlots;
    private double[][][] grid;

    public WeaponUpgradeGrid(int numSlots) {
        ExpandableGrid<Integer> gridTemp = new ExpandableGrid<>(-1);
        ArrayList<Integer> edgeLocations = new ArrayList<>();
        List<Integer[]> directions = Arrays.asList(new Integer[]{1, 0}, new Integer[]{-1, 0}, new Integer[]{0, 1}, new Integer[]{0, -1});

        usedSlots = 0;
        maxSlots = numSlots;

        gridTemp.add(usedSlots++, 0, 0);
        edgeLocations.add(0);
        edgeLocations.add(0);
        while(usedSlots < maxSlots) {
            int startSlotIndex;
            int startSlotX, startSlotY;
            int newSlotX = 0, newSlotY = 0;
            int directionX = 0, directionY = 0;
            while(true) {
                startSlotIndex = (int)(Math.random() * edgeLocations.size() / 2);
                startSlotX = edgeLocations.get(startSlotIndex*2);
                startSlotY = edgeLocations.get(startSlotIndex*2 + 1);

                boolean valid = false;

                Collections.shuffle(directions);
                for(Integer[] direction: directions) {
                    directionX = direction[0];
                    directionY = direction[1];
                    newSlotX = startSlotX + directionX;
                    newSlotY = startSlotY + directionY;
                    if(gridTemp.get(newSlotX, newSlotY) == -1) {
                        //open space, works
                        valid = true;
                        break;
                    }
                }
                if(valid)
                    break;
            }

            int lineLength = Util.choose(1, 1, 1, 1, 2, 2, 2, 2, 3);
            while(lineLength > 0 && usedSlots < maxSlots) {
                if(gridTemp.get(newSlotX, newSlotY) == -1) {
                    gridTemp.add(usedSlots++, newSlotX, newSlotY);
                    edgeLocations.add(newSlotX);
                    edgeLocations.add(newSlotY);
                }
                newSlotX += directionX;
                newSlotY += directionY;
                lineLength--;
            }
        }

        System.out.println(gridTemp);
    }

    //private void addRandomSlot()

    public static void main(String[] args) {
        new WeaponUpgradeGrid(15);
    }
}
