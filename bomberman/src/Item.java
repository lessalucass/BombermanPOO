public class Item extends Entidade {
    public static final int RAIO = 1;
    public static final int BOMBA = 2;
    public static final int SAIDA = 3;

    private int tipo;

    public Item(int tipo, int linha, int coluna) {
        super(linha, coluna);
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    @Override
    public char simbolo() {
        if (tipo == RAIO) {
            return '*';
        }
        if (tipo == BOMBA) {
            return '&';
        }
        return 'E';
    }
}
