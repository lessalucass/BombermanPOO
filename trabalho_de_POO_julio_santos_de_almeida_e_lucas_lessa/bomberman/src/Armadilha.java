public class Armadilha extends Obstaculo {
    private boolean pareceParede;

    public Armadilha(int linha, int coluna, boolean pareceParede) {
        super(linha, coluna);
        this.pareceParede = pareceParede;
    }

    @Override
    public boolean explodeAoToque() {
        return true;
    }

    @Override
    public void sofrerExplosao(Tabuleiro tabuleiro) {
        tabuleiro.removerObstaculo(linha, coluna);
        tabuleiro.adicionarEvento("Uma armadilha explodiu!");
        tabuleiro.detonar(linha, coluna, 2);
    }

    @Override
    public char simbolo() {
        if (pareceParede) {
            return '█';
        }
        return '#';
    }
}
