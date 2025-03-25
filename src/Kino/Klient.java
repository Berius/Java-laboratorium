package Kino;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Klient implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nazwisko;
    private String imie;
    private String mail;
    private String telefon;
    private Seans seans;
    private List<String> miejsca;

    public Klient(String nazwisko, String imie, String mail, String telefon, Seans seans, List<String> miejsca) {
        this.nazwisko = nazwisko;
        this.imie = imie;
        this.mail = mail;
        this.telefon = telefon;
        this.seans = seans;
        this.miejsca = miejsca;
    }

    @Override
    public String toString() {
        return "Klient: " + imie + " " + nazwisko + ", Email: " + mail + ", Tel: " + telefon +
                "\nSeans: " + seans + "\nMiejsca: " + miejsca;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Klient klient = (Klient) obj;
        return nazwisko.equals(klient.nazwisko) && imie.equals(klient.imie) && mail.equals(klient.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nazwisko, imie, mail);
    }
}
