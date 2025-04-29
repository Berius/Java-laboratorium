package zadanieONP;

public class ONP {
    private TabStack stack = new TabStack();

    boolean czyPoprawneRownanie(String rownanie) {
        return rownanie.endsWith("=");
    }

    public String obliczOnp(String rownanie) {
        if (!czyPoprawneRownanie(rownanie)) {
            return "Błędne równanie";
        }

        stack.setSize(0);
        String wynik = "";
        Double a, b;

        for (int i = 0; i < rownanie.length(); i++) {
            char znak = rownanie.charAt(i);

            if (Character.isDigit(znak)) {
                wynik += znak;
                if (i + 1 == rownanie.length() || !Character.isDigit(rownanie.charAt(i + 1))) {// jeśli następny znak nie jest cyfrą lub to koniec równania
                    stack.push(wynik);
                    wynik = "";
                }
            } else if (znak == '=') {
                return stack.pop();
            } else if (znak != ' ') {
                try {
                    if (znak == '!') {
                        a = Double.parseDouble(stack.pop());
                        stack.push(silnia(a.intValue()) + "");
                    } else {
                        b = Double.parseDouble(stack.pop());
                        a = Double.parseDouble(stack.pop());

                        switch (znak) {
                            case '+': stack.push((a + b) + ""); break;
                            case '-': stack.push((a - b) + ""); break;
                            case '*': stack.push((a * b) + ""); break;
                            case '/':
                                if (b == 0) throw new ArithmeticException("Błąd: Dzielenie przez zero!");
                                stack.push((a / b) + "");
                                break;
                            case '^': stack.push(Math.pow(a, b) + ""); break;
                            case '%': stack.push((a % b) + ""); break;
                            case '√':
                                stack.push(pierwiastek(a, b.intValue()) + "");
                                break;
                            default: throw new RuntimeException("Błąd: Nieznany operator '" + znak + "'");
                        }
                    }
                } catch (Exception e) {
                    return e.getMessage();
                }
            }
        }
        return "0.0";
    }

    private int silnia(int n) {
        if (n < 0) throw new RuntimeException("Błąd: Silnia z liczby ujemnej!");
        int wynik = 1;
        for (int i = 1; i <= n; i++) {
            wynik *= i;
        }
        return wynik;
    }
    private double pierwiastek(double a, int b) {
        if (b < 0) throw new IllegalArgumentException("Błąd: Stopień pierwiastka nie może być ujemny!");
        if (b % 2 == 0 && a < 0) throw new IllegalArgumentException("Błąd: Liczba a nie może być ujemna, gdy b jest parzyste!");
        if (b == 0) throw new IllegalArgumentException("Błąd: Stopień pierwiastka nie może być równy 0!");
        return Math.pow(a, 1.0 / b);
    }
    public String przeksztalcNaOnp(String rownanie) {
        if (!czyPoprawneRownanie(rownanie)) {
            return "Błędne równanie";
        }

        String wynik = "";
        for (int i = 0; i < rownanie.length(); i++) {
            char znak = rownanie.charAt(i);

            if (Character.isDigit(znak)) {
                wynik += znak;
                if (i + 1 == rownanie.length() || !Character.isDigit(rownanie.charAt(i + 1))) {
                    wynik += " ";
                }
            } else {
                switch (znak) {
                    case '+': case '-':
                        while (stack.getSize() > 0 && !stack.showValue(stack.getSize() - 1).equals("(")) {// dopóki stos nie jest pusty i na jego szczycie nie ma nawiasu
                            wynik += stack.pop() + " ";
                        }
                        stack.push("" + znak);
                        break;
                    case '*': case '/': case '%':
                        while (stack.getSize() > 0 && !stack.showValue(stack.getSize() - 1).equals("(")
                                && !stack.showValue(stack.getSize() - 1).equals("+")
                                && !stack.showValue(stack.getSize() - 1).equals("-")) {
                            wynik += stack.pop() + " ";
                        }
                        stack.push("" + znak);
                        break;
                    case '^': case '√':
                        while (stack.getSize() > 0 && stack.showValue(stack.getSize() - 1).equals("^")) {
                            wynik += stack.pop() + " ";
                        }
                        stack.push("" + znak);
                        break;
                    case '!':
                        wynik += "! ";
                        break;
                    case '(':
                        stack.push("" + znak);
                        break;
                    case ')':
                        while (stack.getSize() > 0 && !stack.showValue(stack.getSize() - 1).equals("(")) {
                            wynik += stack.pop() + " ";
                        }
                        stack.pop();
                        break;
                    case '=':
                        while (stack.getSize() > 0) {
                            wynik += stack.pop() + " ";
                        }
                        wynik += "=";
                        break;
                    default:
                        return "Błąd: Nieznany znak '" + znak + "'";
                }
            }
        }
        return wynik;
    }

    public static void main(String[] args) {
        String[] calculations = {
                "(2+3)*(6-2)^2=",
                "(7+1)*((4-2)^2)=",
                "(8+1)/((4-2)^2)=",
                "(2+3)*(2-3)+8=",
                "(22+3)/(2-34)*3+8=",
                "(2-34)*3+8",
                "(22+3)/(2-34)*3+8)="
        };

        ONP onp = new ONP();
        for (String calculation : calculations) {
            System.out.print(calculation + " ");
            String rownanieOnp = onp.przeksztalcNaOnp(calculation);
            System.out.print(rownanieOnp);
            String wynik = onp.obliczOnp(rownanieOnp);
            System.out.println(" " + wynik);
        }
    }
}
