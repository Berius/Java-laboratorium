package synchronizacjaDostepu;

import java.util.concurrent.Callable;

public class OdczytywanieRownania implements Callable<String> {
    private final String equation;

    public OdczytywanieRownania(String equation) {
        this.equation = equation;
    }

    @Override
    public String call() {
        return equation;
    }
}
