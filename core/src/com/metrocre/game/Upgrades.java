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
    };

    public static Upgrades fromString(String string) {
        switch (string) {
            case "Speed":
                return Speed;
            case "Defence":
                return Defence;
            case "Attack":
                return Attack;
        }
        throw new IllegalArgumentException();
    }
}
