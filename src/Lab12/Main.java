package Lab12;

import java.util.*;
import java.util.concurrent.*;

class ZadanieWatki {

    // FutureTaskManager - generyczna klasa zarządzająca zadaniami
    public static class FutureTaskManager<T> extends FutureTask<T> {
        private final String taskName;
        private final CountDownLatch latch;

        public FutureTaskManager(Callable<T> callable, String taskName, CountDownLatch latch) {
            super(callable);
            this.taskName = taskName;
            this.latch = latch;
        }

        @Override
        protected void done() {
            try {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Zadanie " + taskName + " zostało przerwane.");
                } else if (isCancelled()) {
                    System.out.println("Zadanie " + taskName + " zostało anulowane.");
                } else {
                    T result = get();
                    System.out.println("Zadanie " + taskName + " zakończone, wynik: " + result);
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Błąd w zadaniu " + taskName + ": " + e.getMessage());
            } finally {
                latch.countDown(); // zawsze zmniejszamy licznik
            }
        }
    }


    // Zadanie - wykonujące jakąś pracę (symulacja pracy + sprawdzenie przerwania)
    static class Zadanie implements Callable<String> {
        private final int id;

        public Zadanie(int id) {
            this.id = id;
        }

        @Override
        public String call() throws Exception {
            Random random = new Random();
            int sleepTime = random.nextInt(5000) + 1000;
            for (int i = 0; i < sleepTime / 100; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Zadanie " + id + " zostało przerwane podczas pracy.");
                    return "Przerwano zadanie " + id;
                }
                Thread.sleep(100); // symulacja pracy fragmentami
            }
            return "Wynik zadania " + id;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<FutureTaskManager<String>> zadania = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(0); // Startowo 0
        int counter = 1;

        while (true) {
            System.out.println("\nMENU:");
            System.out.println("1. Dodaj nowe zadanie");
            System.out.println("2. Pokaż status wszystkich zadań");
            System.out.println("3. Anuluj zadanie");
            System.out.println("4. Pokaż wynik zadania");
            System.out.println("5. Wyjdź");

            System.out.print("Twój wybór: ");
            int wybor;
            try {
                wybor = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Podano nieprawidłową wartość. Spróbuj ponownie.");
                scanner.next(); // czyścimy błędne wejście
                continue;
            }

            switch (wybor) {
                case 1:
                    // Przy dodawaniu nowego zadania, zwiększ CountDownLatch
                    latch = new CountDownLatch((int) latch.getCount() + 1);
                    Zadanie zadanie = new Zadanie(counter);
                    FutureTaskManager<String> futureTask = new FutureTaskManager<>(zadanie, "Zadanie " + counter, latch);
                    zadania.add(futureTask);
                    executor.execute(futureTask);
                    System.out.println("Dodano zadanie nr " + counter);
                    counter++;
                    break;

                case 2:
                    for (int i = 0; i < zadania.size(); i++) {
                        FutureTaskManager<String> z = zadania.get(i);
                        System.out.println((i + 1) + ": " + z + " (Done: " + z.isDone() + ", Cancelled: " + z.isCancelled() + ")");
                    }
                    break;

                case 3:
                    System.out.print("Podaj numer zadania do anulowania: ");
                    int nrAnuluj = scanner.nextInt();
                    if (nrAnuluj >= 1 && nrAnuluj <= zadania.size()) {
                        FutureTaskManager<String> z = zadania.get(nrAnuluj - 1);
                        boolean result = z.cancel(false);
                        System.out.println(result ? "Anulowano." : "Nie udało się anulować (może już zakończone?).");
                    } else {
                        System.out.println("Błędny numer zadania.");
                    }
                    break;

                case 4:
                    System.out.print("Podaj numer zadania, aby zobaczyć wynik: ");
                    int nrWynik = scanner.nextInt();
                    if (nrWynik >= 1 && nrWynik <= zadania.size()) {
                        FutureTaskManager<String> z = zadania.get(nrWynik - 1);
                        if (z.isDone() && !z.isCancelled()) {
                            try {
                                System.out.println("Wynik: " + z.get());
                            } catch (InterruptedException | ExecutionException e) {
                                System.out.println("Błąd podczas pobierania wyniku: " + e.getMessage());
                            }
                        } else if (z.isCancelled()) {
                            System.out.println("Zadanie zostało anulowane.");
                        } else {
                            System.out.println("Zadanie nadal trwa.");
                        }
                    } else {
                        System.out.println("Błędny numer zadania.");
                    }
                    break;

                case 5:
                    System.out.println("Zamykanie programu. Oczekiwanie na zakończenie wszystkich zadań...");
                    try {
                        latch.await(); // Czekamy na zakończenie wszystkich zadań
                    } catch (InterruptedException e) {
                        System.out.println("Czekanie przerwane.");
                    }
                    executor.shutdownNow();
                    System.out.println("Program zakończony.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Niepoprawny wybór.");
            }
        }
    }
}
