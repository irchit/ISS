package iss.group.application.Domain;

import java.util.Objects;

public class Loc {

    private int id;
    private int nrOrdine, rand, coloana;

    public Loc(int id, int nrOrdine, int rand, int coloana) {
        this.id = id;
        this.nrOrdine = nrOrdine;
        this.rand = rand;
        this.coloana = coloana;
    }

    public int getId() {
        return id;
    }

    public int getNrOrdine() {
        return nrOrdine;
    }

    public int getRand() {
        return rand;
    }

    public int getColoana() {
        return coloana;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNrOrdine(int nrOrdine) {
        this.nrOrdine = nrOrdine;
    }

    public void setRand(int rand) {
        this.rand = rand;
    }

    public void setColoana(int coloana) {
        this.coloana = coloana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loc loc = (Loc) o;
        return nrOrdine == loc.nrOrdine;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nrOrdine);
    }

    @Override
    public String toString() { return "L" + id; }
}
