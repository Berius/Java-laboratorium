package zadanieSet;

public class Przedmiot implements Comparable<Przedmiot> {
    String nazwa;

    public Przedmiot(String name) {
        this.nazwa = name;
    }

    public String getName() {
        return nazwa;
    }

    @Override
    public int compareTo(Przedmiot o) {
        return this.nazwa.compareTo(o.nazwa);
    }

    @Override
    public String toString() {
        return nazwa;
    }
}
