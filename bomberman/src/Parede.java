public class Parede extends Obstaculo {
    public Parede(int linha, int coluna) {
        super(linha, coluna);
    }

    @Override
    public boolean bloqueiaFogo() {
        return true;
    }

    @Override
    public void sofrerExplosao(Tabuleiro tabuleiro) {
    }

    @Override
    public char simbolo() {
        return '█';
    }
}
