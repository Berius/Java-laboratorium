package lab13;

public class TabStack {
    private String[] stack = new String[100];
    private int size = 0;

    public synchronized String pop() {
        if (size == 0) {
            throw new RuntimeException("Błąd: Stos jest pusty!");
        }
        size--;
        return stack[size];
    }

    public synchronized void push(String a) {
        if (size >= stack.length) {
            throw new RuntimeException("Błąd: Stos jest pełny!");
        }
        stack[size] = a;
        size++;
    }
    public int getSize() {
        return size;
    }

    public String showValue(int i) {
        if (i < size) return stack[i];
        return null;
    }

    public void setSize(int i) {
        size = i;
    }

    public synchronized void clear() {
        size = 0;
    }
}