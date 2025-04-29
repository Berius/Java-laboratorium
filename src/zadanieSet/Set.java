package zadanieSet;

import java.util.Arrays;

class Set<T extends Comparable<T>> {
    private T[] set;
    private int pojemnosc;
    private int rozmiar;

    public Set(int pojemnosc) {
        this.pojemnosc = pojemnosc;
        this.set = (T[]) new Comparable[pojemnosc];
        this.rozmiar = 0;
    }

    public void dodajElement(T element) {
        if (rozmiar >= pojemnosc) {
            throw new IllegalStateException("Zbiór jest pełny!");
        }
        if (szukaj(element) != -1) {
            return;
        }
        set[rozmiar++] = element;
        Arrays.sort(set, 0, rozmiar);
    }

    public int szukaj(T element) {
        for (int i = 0; i < rozmiar; i++) {
            if (set[i].compareTo(element) == 0) {
                return i;
            }
        }
        return -1;
    }

    public void usunElement(T element) {
        int index = szukaj(element);
        if (index == -1) {
            throw new IllegalArgumentException("Element nie został znaleziony w zbiorze!");
        }
        for (int i = index; i < rozmiar - 1; i++) {
            set[i] = set[i + 1];
        }
        set[--rozmiar] = null;
    }

    public static <T extends Comparable<T>> Set<T> dodajElementy(Set<T> s1, Set<T> s2) {
        Set<T> nowySet = new Set<>(s1.pojemnosc + s2.pojemnosc);
        for (int i = 0; i < s1.rozmiar; i++) {
            nowySet.dodajElement(s1.set[i]);
        }
        for (int i = 0; i < s2.rozmiar; i++) {
            nowySet.dodajElement(s2.set[i]);
        }
        return nowySet;
    }

    public static <T extends Comparable<T>> Set<T> odejmijElementy(Set<T> s1, Set<T> s2) {
        Set<T> nowySet = new Set<>(s1.pojemnosc);
        for (int i = 0; i < s1.rozmiar; i++) {
            if (s2.szukaj(s1.set[i]) == -1) {
                nowySet.dodajElement(s1.set[i]);
            }
        }
        return nowySet;
    }

    public static <T extends Comparable<T>> Set<T> przeciecie(Set<T> s1, Set<T> s2) {
        Set<T> nowySet = new Set<>(Math.min(s1.pojemnosc, s2.pojemnosc));
        for (int i = 0; i < s1.rozmiar; i++) {
            if (s2.szukaj(s1.set[i]) != -1) {
                nowySet.dodajElement(s1.set[i]);
            }
        }
        return nowySet;
    }

    @Override
    public String toString() {
        return "Rozmiar: " + rozmiar + ", Pojemność: " + pojemnosc + ", Elementy: " + Arrays.toString(Arrays.copyOf(set, rozmiar));
    }
}
