package PracaZWatkami;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Task implements Runnable {
    private final String taskID;
    private boolean isCancelled;
    private boolean isCompleted;
    private long result;

    public Task(String taskID) {
        this.taskID = taskID;
        this.isCancelled = false;
        this.isCompleted = false;
        this.result = 0;
    }

    @Override
    public void run() {
        if (!isCancelled) {
            System.out.println("Zadanie " + taskID + " rozpoczęło pracę.");
            Random random = new Random();
            long result = 0;

            try {
                for (int i = 0; i < 1000; i++) {
                    result += random.nextInt(100);
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Zadanie " + taskID + " zostało anulowane podczas obliczeń.");
                        return;
                    }
                }
                Thread.sleep(5000); // Symulacja oczekiwania
            } catch (InterruptedException e) {
                System.out.println("Zadanie " + taskID + " zostało anulowane podczas oczekiwania.");
                return;
            }

            this.result = result;
            isCompleted = true;

            System.out.println("Zadanie " + taskID + " zakończone.");
        }
    }

    public void cancel() {
        isCancelled = true;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public long getResult() {
        return result;
    }

    public String getTaskID() {
        return taskID;
    }

    @Override
    public String toString() {
        return "Zadanie " + taskID + " | Anulowane: " + isCancelled + " | Zakończone: " + isCompleted + " | Wynik: " + result;
    }
}


class TaskManager {
    private List<Task> taskList = new ArrayList<>();
    private List<Thread> taskThreads = new ArrayList<>();
    private int maxTotalThreads;
    private int maxActiveThreads;
    private int runningThreads = 0;

    public TaskManager(int maxTotalThreads, int maxActiveThreads) {
        this.maxTotalThreads = maxTotalThreads;
        this.maxActiveThreads = maxActiveThreads;
    }

    // Dodawanie zadania
    public int addTask() {
        updateRunningThreads();
        if (runningThreads >= maxActiveThreads) {
            System.out.println("Maksymalna liczba równocześnie działających wątków (" + maxActiveThreads + ") została osiągnięta.");
            return -1;
        }
        if (taskThreads.size() >= maxTotalThreads) {
            System.out.println("Maksymalna liczba wątków (" + maxTotalThreads + ") została osiągnięta.");
            return -1;
        }

        final String taskID = "tsk" + (taskList.size() + 1);
        Task task = new Task(taskID);
        taskList.add(task);

        Thread taskThread = new Thread(task);
        taskThreads.add(taskThread);
        taskThread.start();

        runningThreads++;
        System.out.println("Zadanie " + taskID + " dodane do listy.");

        return taskList.size();
    }

    private void updateRunningThreads() {
        int count = 0;
        for (Thread t : taskThreads) {
            if (t.isAlive()) {
                count++;
            }
        }
        runningThreads = count;
    }

    public void checkTaskStatus(int taskIndex) {
        if (taskIndex < 0 || taskIndex >= taskList.size()) {
            System.out.println("Nieprawidłowy indeks zadania.");
            return;
        }
        Task task = taskList.get(taskIndex);
        System.out.println("Status zadania " + task.getTaskID() + ":");
        System.out.println("Anulowane: " + task.isCancelled());
        System.out.println("Zakończone: " + task.isCompleted());
        if (task.isCompleted()) {
            System.out.println("Wynik: " + task.getResult());
        }
    }


    public void cancelTask(int taskID) {
        if (taskID <= 0 || taskID > taskList.size()) {
            System.out.println("Zadanie " + taskID + " nie istnieje.");
            return;
        }

        Task task = taskList.get(taskID - 1);
        if (!task.isCancelled() && !task.isCompleted()) {
            task.cancel();
            Thread thread = taskThreads.get(taskID - 1);
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
            System.out.println("Zadanie " + taskID + " zostało anulowane.");
        } else {
            System.out.println("Zadanie " + taskID + " już się zakończyło lub zostało anulowane.");
        }
    }

    public void getTaskResult(int taskID) {
        if (taskID <= 0 || taskID > taskList.size()) {
            System.out.println("Zadanie " + taskID + " nie istnieje.");
            return;
        }

        Task task = taskList.get(taskID - 1);
        if (task.isCancelled()) {
            System.out.println("Zadanie " + taskID + " zostało anulowane, brak wyniku.");
        } else if (task.isCompleted()) {
            System.out.println("Wynik zadania " + taskID + ": " + task.getResult());
        } else {
            System.out.println("Zadanie " + taskID + " jeszcze się wykonuje.");
        }
    }

    public void showAllTasks() {
        System.out.println("\nLista wszystkich zadań:");
        for (Task task : taskList) {
            System.out.println(task);
        }
    }

    public void stopAllTasks() {
        System.out.println("Zatrzymywanie wszystkich zadań...");
        for (Thread thread : taskThreads) {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }
    }
}


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        TaskManager manager = new TaskManager(10, 3);

        while (true) {
            System.out.println("\n1. Dodaj zadanie");
            System.out.println("2. Sprawdź status zadania");
            System.out.println("3. Anuluj zadanie");
            System.out.println("4. Pobierz wynik zadania");
            System.out.println("5. Wyświetl listę zadań");
            System.out.println("6. Zakończ program");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    manager.addTask();
                    break;
                case 2:
                    System.out.print("Podaj numer zadania: ");
                    manager.checkTaskStatus(scanner.nextInt());
                    break;
                case 3:
                    System.out.print("Podaj numer zadania: ");
                    manager.cancelTask(scanner.nextInt());
                    break;
                case 4:
                    System.out.print("Podaj numer zadania: ");
                    manager.getTaskResult(scanner.nextInt());
                    break;
                case 5:
                    manager.showAllTasks();
                    break;
                case 6:
                    manager.stopAllTasks();
                    System.out.println("Zakończono program.");
                    return;
                default:
                    System.out.println("Nieprawidłowy wybór.");
            }
        }
    }
}
