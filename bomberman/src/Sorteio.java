public class Sorteio {
    public static int entre(int n) {
        return (int) (Math.random() * n);
    }

    public static boolean chance(int percentual) {
        return entre(100) < percentual;
    }
}
