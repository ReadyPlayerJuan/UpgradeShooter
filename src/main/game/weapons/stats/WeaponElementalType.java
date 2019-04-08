package main.game.weapons.stats;

public enum WeaponElementalType {
    NONE        (0),
    EXPLOSION   (1),
    FIRE        (2),
    LIGHTNING   (3),
    FROST       (4);

    public final int i;
    WeaponElementalType(int index) {
        i = index;
    }
}
