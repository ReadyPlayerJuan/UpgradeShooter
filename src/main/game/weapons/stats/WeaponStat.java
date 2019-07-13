package main.game.weapons.stats;

public enum WeaponStat {
    BULLET_DAMAGE                       ("Damage", WeaponStatType.NUMBER),
    BULLET_DAMAGE_FALLOFF_PCT           ("Damage Falloff", WeaponStatType.PERCENTAGE), //not between zero and one. perhaps snipers can have reverse damage falloff??
    BULLET_DAMAGE_FALLOFF_DISTANCE      ("Damage Falloff Distance", WeaponStatType.NUMBER),
    BULLET_SPEED                        ("Bullet Speed", WeaponStatType.NUMBER),
    BULLET_NUM                          ("Num Bullets", WeaponStatType.NUMBER),
    BULLET_ACCELERATION                 ("Bullet Accel", WeaponStatType.NUMBER),
    BULLET_SIZE                         ("Bullet Size", WeaponStatType.NUMBER),
    BULLET_BOUNCE                       ("Bounce", WeaponStatType.NUMBER),
    BULLET_LIFETIME                     ("Bullet Lifetime", WeaponStatType.TIME),
    BULLET_KNOCKBACK                    ("Knockback", WeaponStatType.NUMBER),

    WEAPON_SPREAD                       ("Spread", WeaponStatType.PERCENTAGE_BETWEEN_ZERO_AND_ONE),
    WEAPON_FIRE_RATE                    ("Fire Rate", WeaponStatType.NUMBER),
    WEAPON_KICK                         ("Kick", WeaponStatType.NUMBER),
    WEAPON_LIFESTEAL                    ("Lifesteal", WeaponStatType.PERCENTAGE),
    WEAPON_CLIP_SIZE                    ("Clip Size", WeaponStatType.NUMBER),

    PLAYER_SPEED                        ("Player Speed", WeaponStatType.PERCENTAGE),
    PLAYER_HEALTH                       ("Player Health", WeaponStatType.PERCENTAGE),
    PLAYER_REGEN                        ("Player Regen", WeaponStatType.PERCENTAGE),
    PLAYER_RESISTANCE                   ("Player Damage Resist", WeaponStatType.PERCENTAGE),

    EXPLOSION_DAMAGE                    ("Explosion Damage", WeaponStatType.NUMBER),
    EXPLOSION_RADIUS                    ("Explosion Radius", WeaponStatType.NUMBER),
    EXPLOSION_KNOCKBACK                 ("Explosion Knockback", WeaponStatType.NUMBER),

    FIRE_DAMAGE                         ("Fire Damage", WeaponStatType.NUMBER),
    FIRE_DURATION                       ("Fire Duration", WeaponStatType.TIME),
    FIRE_SPREAD_CHANCE                  ("Fire Spread", WeaponStatType.PERCENTAGE_BETWEEN_ZERO_AND_ONE),

    LIGHTNING_DAMAGE                    ("Lightning Damage", WeaponStatType.NUMBER),
    LIGHTNING_MAX_BOUNCES               ("Lightning Bounces", WeaponStatType.NUMBER),
    LIGHTNING_STUN_PCT                  ("Lightning Stun Chance", WeaponStatType.PERCENTAGE_BETWEEN_ZERO_AND_ONE),
    LIGHTNING_STUN_DURATION             ("Lightning Stun", WeaponStatType.TIME);

    private String name;
    private WeaponStatType statType;
    WeaponStat(String name, WeaponStatType statType) {
        this.name = name;
        this.statType = statType;
    }
    public String getName() {
        return name;
    }
    public WeaponStatType getStatType() {
        return statType;
    }
}
