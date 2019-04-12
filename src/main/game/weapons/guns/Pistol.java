package main.game.weapons.guns;

import main.game.entities.Entity;
import main.game.entities.projectiles.TestProjectile;
import main.game.weapons.Weapon;
import main.game.weapons.WeaponController;
import main.game.weapons.WeaponType;
import main.game.weapons.stats.WeaponStatType;
import rendering.textures.SpriteData;

public class Pistol extends Weapon {
    private static final WeaponType type = WeaponType.PISTOL;
    private static final double[] elementalWeights = new double[] {
            1.0, //NO ELEMENT
            0.0, //EXPLOSION
            0.0, //FIRE
            0.0, //LIGHTNING
            0.0, //FROST
    };
    private static final WeaponStatType[] availableStats = new WeaponStatType[] {
            WeaponStatType.BULLET_DAMAGE,
            WeaponStatType.BULLET_SPEED,
            WeaponStatType.BULLET_SIZE,
            WeaponStatType.BULLET_KNOCKBACK,
            WeaponStatType.WEAPON_SPREAD,
            WeaponStatType.WEAPON_ACCURACY,
            WeaponStatType.WEAPON_FIRE_RATE,
            WeaponStatType.WEAPON_KICK,
    };
    private static final double[][] defaultStats = new double[][] {
            {10.0, 15.0},   //DAMAGE
            {350, 450},     //BULLET SPEED
            {9.0, 13.0},    //BULLET SIZE
            {80, 120},      //BULLET KNOCKBACK
            {10.0, 15.0},   //WEAPON SPREAD
            {1.00, 1.75},   //WEAPON ACCURACY
            {2.0, 2.5},     //WEAPON FIRE RATE
            {40.0, 60.0},   //WEAPON KICK
    };

    //inherited: protected WeaponStat[] stats;
    //inherited: protected double[] variationRolls;

    private double fireTimer = 0;

    public Pistol() {
        super(type, elementalWeights, availableStats, defaultStats);

        randomizeVariationRolls();
        initStats();
    }

    @Override
    public void update(double delta, WeaponController controller) {
        boolean firing = controller.isFiringWeapon();
        boolean prevFiring = controller.wasFiringWeapon();

        //System.out.println(fireTimer);
        if(firing) {
            double fireTime = 1.0 / stats[6].getFinalValue();

            if(!prevFiring) {
                fireTimer = fireTime;
            } else {
                fireTimer += delta;
            }
            while(fireTimer >= fireTime) {
                fireTimer -= fireTime;
                fire(delta, controller);
            }
        } else {
            fireTimer = 0;
        }
    }

    protected void fire(double delta, WeaponController controller) {
        double angle = Math.atan2(controller.getTargetY(), controller.getTargetX());
        double skewPct = Math.pow(Math.random(), stats[5].getFinalValue()); //how much off to one side the shot is.
        angle += Math.toRadians(stats[4].getFinalValue()) * skewPct * Math.signum(Math.random()-0.5); //add angle variation

        new TestProjectile(controller, this, SpriteData.PLAYER,
                controller.getX(), controller.getY(),
                stats[0].getFinalValue(),   //damage
                stats[1].getFinalValue(),   //speed
                angle,                      //angle
                stats[2].getFinalValue(),   //bullet radius
                stats[3].getFinalValue());  //knockback
    }
}
