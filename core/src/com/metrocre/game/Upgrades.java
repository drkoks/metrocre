package com.metrocre.game;

public enum Upgrades {
    Speed() {
        @Override
        public String toString() {
            return "Speed";
        }
    },
    Defence() {
        @Override
        public String toString() {
            return "Defence";
        }
    },
    Attack() {
        @Override
        public String toString() {
            return "Attack";
        }
    },
    Pistol() {
        @Override
        public String toString() {
            return "Pistol";
        }
    },
    Railgun() {
        @Override
        public String toString() {
            return "Railgun";
        }
    },

    HealTower() {
        @Override
        public String toString() {
            return "HealTower";
        }
    },
    GunTower() {
        @Override
        public String toString() {
            return "GunTower";
        }
    },
    ;

    public static Upgrades fromString(String string) {
        switch (string) {
            case "Speed":
                return Speed;
            case "Defence":
                return Defence;
            case "Attack":
                return Attack;
            case "Pistol":
                return Pistol;
            case "Railgun":
                return Railgun;
            case "HealTower":
                return HealTower;
            case "GunTower":
                return GunTower;
        }
        throw new IllegalArgumentException();
    }

    public boolean isTower() {
        return this == HealTower || this == GunTower;
    }
}
