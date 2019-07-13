package main.game.weapons;

import main.game.weapons.stats.WeaponStat;
import main.game.weapons.stats.WeaponStatType;
import main.util.Util;

public abstract class Weapon {
    protected final WeaponType type;
    protected final double[] elementalWeights;
    protected final WeaponStat[] availableStats;
    protected final double[][] defaultStats;

    protected String name;

    protected String statNames = "";
    protected String statValues = "";

    protected double[][] stats;
    protected double[] variationRolls;

    public Weapon(WeaponType type, double[] elementalWeights, WeaponStat[] availableStats, double[][] defaultStats) {
        this.type = type;
        this.name = type.getName();
        this.elementalWeights = elementalWeights;
        this.availableStats = availableStats;
        this.defaultStats = defaultStats;
    }

    protected void randomizeVariationRolls() {
        variationRolls = new double[availableStats.length];
        for(int i = 0; i < availableStats.length; i++) {
            variationRolls[i] = Math.random();
        }
    }

    protected void initStats() {
        stats = new double[availableStats.length][2];

        for(int i = 0; i < availableStats.length; i++) {
            stats[i][0] = Util.mix(defaultStats[i][0], defaultStats[i][1], variationRolls[i]);
            stats[i][1] = stats[i][0];
        }

        updateWeaponInfo();
    }

    public abstract void update(double delta, WeaponController controller);
    protected abstract void fire(double delta, WeaponController controller);

    protected void updateWeaponInfo() {
        StringBuilder nameString = new StringBuilder();
        for(WeaponStat stat: availableStats) {
            nameString.append(stat.getName()).append("\n");
        }
        statNames = nameString.toString();


        StringBuilder valueString = new StringBuilder();
        for(int i = 0; i < availableStats.length; i++) {
            switch(availableStats[i].getStatType()) {
                case NUMBER:
                    valueString.append(String.format("%.1f", stats[i][1]));
                    break;
                case PERCENTAGE:
                case PERCENTAGE_BETWEEN_ZERO_AND_ONE:
                    valueString.append(String.format("%.1f", stats[i][1] * 100)).append("%");
                    break;
                case TIME:
                    valueString.append(String.format("%.1f", stats[i][1])).append("s");
                    break;
                case MULTIPLIER:
                    valueString.append(String.format("%.1f", stats[i][1])).append("x");
                    break;
            }
            valueString.append("\n");
        }
        statValues = valueString.toString();
    }

    public String getName() {
        return name;
    }
    public String getStatNamesString() {
        return statNames;
    }
    public String getStatValuesString() {
        return statValues;
    }
}
