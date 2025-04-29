package lab13;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("src/synchronizacjaDostepu/equations.txt");
        if (!file.exists()) {
            System.out.println("Plik equations.txt nie istnieje!");
            return;
        }

        List<String> equations = Files.readAllLines(file.toPath());

        ExecutorService executor = Executors.newFixedThreadPool(4);
        Lock lock = new ReentrantLock();

        for (String eq : equations) {
            Future<String> readFuture = executor.submit(new OdczytywanieRownania(eq));
            String equation = readFuture.get();
            ONP taskOnp = new ONP();
            RozwiazanieRownania solver = new RozwiazanieRownania(equation, file, lock, taskOnp);
            executor.submit(solver);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
