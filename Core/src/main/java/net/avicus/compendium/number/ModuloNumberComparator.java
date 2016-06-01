package net.avicus.compendium.number;

public class ModuloNumberComparator implements NumberComparator {
    private final int mod;

    public ModuloNumberComparator(int mod) {
        this.mod = mod;
    }

    @Override
    public boolean perform(double a, double b) {
        return a % this.mod == b;
    }
}
