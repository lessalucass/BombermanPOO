public class InimigoComum extends Inimigo {
    public InimigoComum(int linha, int coluna) {
        super(linha, coluna);
    }

    @Override
    public void mover(Tabuleiro tabuleiro, int turno) {
        if (turno % 2 == 0) {
            andarAleatorio(tabuleiro);
        }
    }

    @Override
    public char simbolo() {
        return 'C';
    }
}
