package Kino;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Kino {
    private static final String PLIK_KLIENCI = "klienci.dat";
    private static final String PLIK_KLIENCI_TXT = "klienci.txt";

    public static void zapiszDoPliku(List<Klient> klienci) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PLIK_KLIENCI))) {
            out.writeObject(klienci);
        } catch (IOException e) {
            System.err.println("Błąd zapisu do pliku: " + e.getMessage());
        }
    }

    public static List<Klient> odczytajZPliku() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PLIK_KLIENCI))) {
            return (List<Klient>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Brak zapisanych klientów lub plik nie istnieje.");
            return new ArrayList<>();
        }
    }

    public static void zapiszDoPlikuTxt(List<Klient> klienci) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLIK_KLIENCI_TXT))) {
            for (Klient k : klienci) {
                writer.write(k.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Błąd zapisu do pliku tekstowego: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Tworzenie seansów
        Seans seans = new Seans("Chłopaki nie płaczą", "20.03.2025", "20:00", 16, 5, 10);
        Seans seans1 = new Seans("Kiler", "21.03.2025", "20:00", 12, 5, 10);

        // Sprawdzenie wieku użytkownika
        System.out.print("Podaj wiek: ");
        int wiek = scanner.nextInt();
        if (wiek < seans1.getOgraniczeniaWiekowe()) {
            System.out.println("Nie możesz zarezerwować biletu na ten seans! Ograniczenie wiekowe: " + seans1.getOgraniczeniaWiekowe() + "+.");
            return;
        }

        // Rezerwacja miejsc
        List<String> miejsca = new ArrayList<>();
        if (seans.rezerwujMiejsce('B', 5)) {
            miejsca.add("B5");
        }

        // Tworzenie klientów
        Klient klient = new Klient("Zawiślan", "Jakub", "jakub.zawislan@gmail.com", "123456789", seans, miejsca);
        Klient klient1 = new Klient("Kluska", "Kacper", "kacper.kluska@op.pl", "987654321", seans1, miejsca);

        // Odczytanie zapisanych klientów i dodanie nowego
        List<Klient> klienci = odczytajZPliku();
        klienci.add(klient1);

        // Zapis klientów do plików
        zapiszDoPliku(klienci);
        zapiszDoPlikuTxt(klienci);

        // Odczyt klientów z pliku i wyświetlenie
        List<Klient> wczytaniKlienci = odczytajZPliku();
        for (Klient k : wczytaniKlienci) {
            System.out.println(k);
        }
    }
}
