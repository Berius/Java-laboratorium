package Kino;

import java.io.Serializable;
import java.util.HashMap;

public class Seans implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tytul;
    private String dzien;
    private String godzina;
    private int ograniczeniaWiekowe;
    private HashMap<Character, HashMap<Integer, Boolean>> miejsca;

    public Seans(String tytul, String dzien, String godzina, int ograniczeniaWiekowe, int liczbaRzedow, int miejscaWRzedzie) {
        this.tytul = tytul;
        this.dzien = dzien;
        this.godzina = godzina;
        this.ograniczeniaWiekowe = ograniczeniaWiekowe;
        this.miejsca = new HashMap<>();

        for (char rzad = 'A'; rzad < 'A' + liczbaRzedow; rzad++) {
            miejsca.put(rzad, new HashMap<>());
            for (int nrMiejsca = 1; nrMiejsca <= miejscaWRzedzie; nrMiejsca++) {
                miejsca.get(rzad).put(nrMiejsca, false);
            }
        }
    }

    public boolean rezerwujMiejsce(char rzad, int nrMiejsca) {
        if (miejsca.containsKey(rzad) && miejsca.get(rzad).containsKey(nrMiejsca)) {
            if (!miejsca.get(rzad).get(nrMiejsca)) {
                miejsca.get(rzad).put(nrMiejsca, true);
                return true;
            } else {
                System.out.println("Miejsce " + rzad + nrMiejsca + " jest już zajęte!");
            }
        } else {
            System.out.println("Nieprawidłowe miejsce: " + rzad + nrMiejsca);
        }
        return false;
    }

    public int getOgraniczeniaWiekowe() {
        return ograniczeniaWiekowe;
    }

    @Override
    public String toString() {
        return "Seans: " + tytul + " (" + dzien + ", " + godzina + ", " + ograniczeniaWiekowe + "+)";
    }
}
