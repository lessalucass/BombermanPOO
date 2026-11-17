import java.util.ArrayList;

public class InimigoSabotador extends Inimigo {
    public InimigoSabotador(int linha, int coluna) {
        super(linha, coluna);
    }

    @Override
    protected boolean podeEntrar(Tabuleiro tabuleiro, int l, int c) {
        if (!tabuleiro.dentro(l, c) || tabuleiro.obstaculoEm(l, c) != null || tabuleiro.inimigoEm(l, c) != null) {
            return false;
        }
        Bomba b = tabuleiro.bombaEm(l, c);
        return b == null || b.desarmavel();
    }

    @Override
    public void mover(Tabuleiro tabuleiro, int turno) {
        Bomba alvo = tabuleiro.bombaDesarmavelPerto(linha, coluna);
        if (alvo == null) {
            andarAleatorio(tabuleiro);
        } else {
            int dl = Tabuleiro.sinal(alvo.getLinha() - linha);
            int dc = Tabuleiro.sinal(alvo.getColuna() - coluna);
            if (dl != 0) {
                andar(tabuleiro, dl, 0);
            } else {
                andar(tabuleiro, 0, dc);
            }
        }
        Bomba b = tabuleiro.bombaEm(linha, coluna);
        if (b != null && b.desarmavel()) {
            tabuleiro.removerBomba(b);
            tabuleiro.adicionarEvento("Um sabotador desarmou uma bomba sua!");
            esconderArmadilha(tabuleiro);
        }
    }

    private void esconderArmadilha(Tabuleiro tabuleiro) {
        ArrayList<int[]> vizinhas = new ArrayList<int[]>();
        for (int[] d : Tabuleiro.DIRECOES) {
            if (tabuleiro.celulaVazia(linha + d[0], coluna + d[1])) {
                vizinhas.add(new int[] {linha + d[0], coluna + d[1]});
            }
        }
        if (vizinhas.size() > 0) {
            int[] p = vizinhas.get(Sorteio.entre(vizinhas.size()));
            tabuleiro.setObstaculo(new Armadilha(p[0], p[1], Sorteio.chance(50)));
        }
    }

    @Override
    public char simbolo() {
        return 'S';
    }
}
