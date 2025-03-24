package zadanieSet;

class Osoba implements Comparable<Osoba> {
    private String imie;
    private int wiek;

    public Osoba(String imie, int wiek) {
        this.imie = imie;
        this.wiek = wiek;
    }

    @Override
    public int compareTo(Osoba o) {
        return Integer.compare(this.wiek, o.wiek);
    }

    @Override
    public String toString() {
        return imie + " (" + wiek + " lat)";
    }
}