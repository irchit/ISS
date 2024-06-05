package iss.group.application.Domain;

import java.util.Date;
import java.util.Objects;

public class Spectacol {

    private int id;
    private String titlu, descriere;
    private Date data;
    private Float pret;

    public Spectacol() {}

    public Spectacol(int id, String titlu, String descriere, Date data, Float pret) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.data = data;
        this.pret = pret;
    }

    public String getTitlu(){return titlu;}
    public void setTitlu(String titlu){this.titlu = titlu;}

    public String getDescriere(){return descriere;}
    public void setDescriere(String descriere){this.descriere = descriere;}

    public Date getData(){return data;}
    public void setData(Date data){this.data = data;}

    public Float getPret(){return pret;}
    public void setPret(Float pret){this.pret = pret;}

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spectacol spectacol = (Spectacol) o;
        return id == spectacol.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
