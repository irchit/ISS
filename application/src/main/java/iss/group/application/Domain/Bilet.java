package iss.group.application.Domain;


import java.util.Objects;

public class Bilet {


    private int id;
    private User user;
    private Loc loc;
    private Spectacol spectacol;

    public Bilet(int id, User user, Loc loc, Spectacol spectacol) {
        this.id = id;
        this.user = user;
        this.loc = loc;
        this.spectacol = spectacol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Loc getLoc() {
        return loc;
    }

    public void setLoc(Loc loc) {
        this.loc = loc;
    }

    public Spectacol getSpectacol() {
        return spectacol;
    }

    public void setSpectacol(Spectacol spectacol) {
        this.spectacol = spectacol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bilet bilet = (Bilet) o;
        return Objects.equals(user, bilet.user) && Objects.equals(loc, bilet.loc) && Objects.equals(spectacol, bilet.spectacol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, loc, spectacol);
    }
}

