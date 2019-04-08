package main.game.weapons;

import main.game.entities.Entity;
import main.game.weapons.stats.WeaponStat;
import main.game.weapons.stats.WeaponStatType;

public abstract class Weapon {
    public static final WeaponType type = null;
    public static final double[] elementalWeights = null;
    protected static final WeaponStatType[] availableStats = null;
    protected static final double[][] defaultStats = null;

    protected WeaponStat[] stats;
    protected double[] variationRolls;

    protected void randomizeVariationRolls() {
        variationRolls = new double[availableStats.length];
    }

    protected void initStats() {
        stats = new WeaponStat[availableStats.length];

        for(int i = 0; i < availableStats.length; i++) {
            stats[i] = new WeaponStat(defaultStats[i][0], defaultStats[i][1], variationRolls[i]);
        }
    }

    public abstract void update(Entity player);
}
