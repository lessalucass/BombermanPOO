public class Caixote extends Obstaculo {
    public Caixote(int linha, int coluna) {
        super(linha, coluna);
    }

    @Override
    public void sofrerExplosao(Tabuleiro tabuleiro) {
        tabuleiro.removerObstaculo(linha, coluna);
    }

    @Override
    public char simbolo() {
        return '#';
    }
}
