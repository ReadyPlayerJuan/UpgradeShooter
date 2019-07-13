package main.game.weapons;

public enum WeaponType {
    PISTOL ("Pistol"),
    REVOLVER ("Revolver"),
    SNIPER_RIFLE ("Sniper Rifle"),
    MACHINE_GUN ("Machine Gun"),
    ASSAULT_RIFLE ("Assault Rifle"),
    SHOTGUN ("Shotgun"),
    LASER_GUN ("Laser Gun"),
    CHARGE_RIFLE ("Charge Rifle"),
    TASER ("Taser"),
    FLAME_THROWER ("Flame Thrower"),
    ROCKET_LAUNCHER ("Rocket Launcher"),
    GRENADE_LAUNCHER ("Grenade Launcher"),
    MINE_LAYER ("Mine Layer");

    private String name;
    WeaponType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
