package zadanieSet;

public class Main {
    public static void main(String[] args) {
        try {
            Set<Przedmiot> przedmiotSet = new Set<>(5);
            przedmiotSet.dodajElement(new Przedmiot("Ksiazka"));
            przedmiotSet.dodajElement(new Przedmiot("Dlugopis"));
            przedmiotSet.dodajElement(new Przedmiot("Sluchawki"));
            System.out.println("Indeks słuchawek: " + przedmiotSet.szukaj(new Przedmiot("Sluchawki")));
            System.out.println("Przedmiot Set: " + przedmiotSet);
            przedmiotSet.usunElement(new Przedmiot("Sluchawki"));
            System.out.println("Przedmiot Set: " + przedmiotSet);
            System.out.println("Indeks słuchawek: " + przedmiotSet.szukaj(new Przedmiot("Sluchawki")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Set<Osoba> osobaSet = new Set<>(2);
            osobaSet.dodajElement(new Osoba("Ewa", 12));
            osobaSet.dodajElement(new Osoba("Kuba", 28));
            osobaSet.dodajElement(new Osoba("Adam", 15));
            System.out.println("Osoba Set: " + osobaSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Set<Przedmiot> przedmiotSet1 = new Set<>(5);
            przedmiotSet1.dodajElement(new Przedmiot("Ksiazka"));
            przedmiotSet1.dodajElement(new Przedmiot("Dlugopis"));
            przedmiotSet1.dodajElement(new Przedmiot("Sluchawki"));

            Set<Przedmiot> przedmiotSet2 = new Set<>(5);
            przedmiotSet2.dodajElement(new Przedmiot("Sluchawki"));
            przedmiotSet2.dodajElement(new Przedmiot("Telefon"));
            przedmiotSet2.dodajElement(new Przedmiot("Laptop"));

            Set<Przedmiot> dodajSet = Set.dodajElementy(przedmiotSet1, przedmiotSet2);
            System.out.println("Dodaj Set: " + dodajSet);

            Set<Przedmiot> odejmijSet = Set.odejmijElementy(przedmiotSet1, przedmiotSet2);
            System.out.println("Odejmij Set: " + odejmijSet);

            Set<Przedmiot> przetnijSet = Set.przeciecie(przedmiotSet1, przedmiotSet2);
            System.out.println("Przetnij Set: " + przetnijSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}