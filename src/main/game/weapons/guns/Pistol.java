package main.game.weapons.guns;

import main.game.entities.Entity;
import main.game.weapons.Weapon;
import main.game.weapons.WeaponType;
import main.game.weapons.stats.WeaponStatType;

public class Pistol extends Weapon {
    public static final WeaponType type = WeaponType.PISTOL;
    public static final double[] elementalWeights = new double[] {
            1.0, //NO ELEMENT
            0.0, //EXPLOSION
            0.0, //FIRE
            0.0, //LIGHTNING
            0.0, //FROST
    };
    protected static final WeaponStatType[] availableStats = new WeaponStatType[] {
            WeaponStatType.BULLET_DAMAGE,
            WeaponStatType.BULLET_SPEED,
            WeaponStatType.BULLET_SIZE,
            WeaponStatType.BULLET_KNOCKBACK,
            WeaponStatType.WEAPON_SPREAD,
            WeaponStatType.WEAPON_ACCURACY,
            WeaponStatType.WEAPON_FIRE_RATE,
            WeaponStatType.WEAPON_KICK,
    };
    protected static final double[][] defaultStats = new double[][] {
            {8.0, 15.0},    //DAMAGE
            {250, 350},     //BULLET SPEED
            {20.0, 25.0},   //BULLET SIZE
            {80, 120},      //BULLET KNOCKBACK
            {17.5, 25.0},   //WEAPON SPREAD
            {0.80, 0.90},   //WEAPON ACCURACY
            {1.5, 2.5},     //WEAPON FIRE RATE
            {40.0, 60.0},   //WEAPON KICK
    };

    //inherited: protected WeaponStat[] stats;
    //inherited: protected double[] variationRolls;

    public Pistol() {
        randomizeVariationRolls();

        initStats();
    }

    @Override
    public void update(Entity player) {

    }
}
