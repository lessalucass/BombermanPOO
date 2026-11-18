public class Bomba extends Entidade {
    public static final int TEMPO = 5;

    private int restante;
    private int raio;
    private boolean nova;

    public Bomba(int linha, int coluna, int raio) {
        super(linha, coluna);
        this.raio = raio;
        this.restante = TEMPO;
        this.nova = true;
    }

    public void passarTurno() {
        if (nova) {
            nova = false;
        } else {
            restante--;
        }
    }

    public boolean pronta() {
        return restante <= 0;
    }

    public void detonarAgora() {
        restante = 0;
    }

    public boolean desarmavel() {
        return restante > TEMPO / 2;
    }

    public int getRaio() {
        return raio;
    }

    @Override
    public char simbolo() {
        return 'B';
    }
}
