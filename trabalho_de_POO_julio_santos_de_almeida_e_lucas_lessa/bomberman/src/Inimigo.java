public abstract class Inimigo extends Entidade implements Destrutivel {
    public Inimigo(int linha, int coluna) {
        super(linha, coluna);
    }

    public abstract void mover(Tabuleiro tabuleiro, int turno);

    protected boolean podeEntrar(Tabuleiro tabuleiro, int l, int c) {
        return tabuleiro.livreParaInimigo(l, c);
    }

    protected boolean andar(Tabuleiro tabuleiro, int dl, int dc) {
        if (podeEntrar(tabuleiro, linha + dl, coluna + dc)) {
            setPosicao(linha + dl, coluna + dc);
            return true;
        }
        return false;
    }

    protected void andarAleatorio(Tabuleiro tabuleiro) {
        int inicio = Sorteio.entre(4);
        for (int i = 0; i < 4; i++) {
            int[] d = Tabuleiro.DIRECOES[(inicio + i) % 4];
            if (andar(tabuleiro, d[0], d[1])) {
                return;
            }
        }
    }

    @Override
    public void sofrerExplosao(Tabuleiro tabuleiro) {
        tabuleiro.removerInimigo(this);
    }
}
