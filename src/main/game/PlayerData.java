package main.game;

import main.game.weapons.Weapon;

public class PlayerData {
    private static Weapon[] equippedWeapons = {null, null};

    public static void setEquippedWeapon(int slot, Weapon weapon) {
        equippedWeapons[slot] = weapon;
    }

    public static Weapon getEquippedWeapon(int slot) {
        return equippedWeapons[slot];
    }
}
