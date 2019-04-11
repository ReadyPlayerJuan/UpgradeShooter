package main.game.weapons;

import main.game.Team;

public interface WeaponController {
    public Team getTeam();
    public double getX();
    public double getY();
    public double getTargetX();
    public double getTargetY();
    public boolean isFiringWeapon();
    public boolean wasFiringWeapon();
}
