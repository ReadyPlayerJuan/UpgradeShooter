package main.game.weapons.stats;

public class WeaponStat {
    private WeaponStatType statType;
    private double minValue, maxValue;
    private double averageValue, variationRoll;
    private double defaultValue, upgradedValue;

    public WeaponStat(double minValue, double maxValue, double variationRoll) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.averageValue = (minValue + maxValue) / 2;
        this.variationRoll = variationRoll;
        double variation = averageValue-minValue;
        this.defaultValue = averageValue + (variationRoll * variation);
        this.upgradedValue = defaultValue;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public double getVariationRoll() {
        return variationRoll;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public double getFinalValue() {
        return upgradedValue;
    }
}
