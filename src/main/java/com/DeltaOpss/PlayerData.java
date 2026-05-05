package com.DeltaOpss;

public class PlayerData {
    private int hearts;
    private boolean heartless;
    private boolean immortal;
    private boolean last;

    public PlayerData() {
        this.hearts = 10;
        this.heartless = false;
        this.immortal = false;
        this.last = false;
    }

    public int getHearts() { return hearts; }
    public void setHearts(int hearts) { this.hearts = Math.max(0, Math.min(30, hearts)); }
    public void addHearts(int amount) { setHearts(this.hearts + amount); }

    public boolean isHeartless() { return heartless; }
    public void setHeartless(boolean heartless) { this.heartless = heartless; }

    public boolean isImmortal() { return immortal; }
    public void setImmortal(boolean immortal) { this.immortal = immortal; }

    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }

    public boolean isNormal() { return !heartless && !immortal; }

    public void setLastWarning(boolean b) {
    }
}
