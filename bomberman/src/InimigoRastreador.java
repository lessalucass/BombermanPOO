public class InimigoRastreador extends Inimigo {
    public InimigoRastreador(int linha, int coluna) {
        super(linha, coluna);
    }

    @Override
    public void mover(Tabuleiro tabuleiro, int turno) {
        Jogador j = tabuleiro.getJogador();
        if (tabuleiro.caminhoLivre(linha, coluna, j.getLinha(), j.getColuna())) {
            andar(tabuleiro, Tabuleiro.sinal(j.getLinha() - linha), Tabuleiro.sinal(j.getColuna() - coluna));
        } else {
            andarAleatorio(tabuleiro);
        }
    }

    @Override
    public char simbolo() {
        return 'R';
    }
}
