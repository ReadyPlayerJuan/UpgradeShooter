package main.game.weapons;

import main.game.weapons.stats.WeaponStat;
import main.game.weapons.stats.WeaponStatType;

public abstract class Weapon {
    protected final WeaponType type;
    protected final double[] elementalWeights;
    protected final WeaponStatType[] availableStats;
    protected final double[][] defaultStats;

    protected WeaponStat[] stats;
    protected double[] variationRolls;

    public Weapon(WeaponType type, double[] elementalWeights, WeaponStatType[] availableStats, double[][] defaultStats) {
        this.type = type;
        this.elementalWeights = elementalWeights;
        this.availableStats = availableStats;
        this.defaultStats = defaultStats;
    }

    protected void randomizeVariationRolls() {
        variationRolls = new double[availableStats.length];
    }

    protected void initStats() {
        stats = new WeaponStat[availableStats.length];

        for(int i = 0; i < availableStats.length; i++) {
            stats[i] = new WeaponStat(defaultStats[i][0], defaultStats[i][1], variationRolls[i]);
        }
    }

    public abstract void update(double delta, WeaponController controller);
    protected abstract void fire(double delta, WeaponController controller);
}
