package synchronizacjaDostepu;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;

public class RozwiazanieRownania extends FutureTask<String> {
    private final String equation;
    private final File file;
    private final Lock lock;

    public RozwiazanieRownania(String equation, File file, Lock lock, ONP onp) {
        super(() -> {
            String expr = equation.trim();
            synchronized(onp) {
                String exprOnp = onp.przeksztalcNaOnp(expr);
                return onp.obliczOnp(exprOnp);
            }
        });
        this.equation = equation;
        this.file = file;
        this.lock = lock;
    }

    @Override
    protected void done() {
        try {
            String result = get();
            lock.lock();
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).equals(equation)) {
                        lines.set(i, lines.get(i) + " " + result);
                        break;
                    }
                }
                Files.write(file.toPath(), lines);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
