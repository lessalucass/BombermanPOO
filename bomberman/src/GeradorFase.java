import java.util.ArrayList;

public class GeradorFase {
    public static final int TAMANHO = 20;

    public static char[][] criarGrade() {
        char[][] g = new char[TAMANHO][TAMANHO];
        for (int l = 0; l < TAMANHO; l++) {
            for (int c = 0; c < TAMANHO; c++) {
                g[l][c] = '.';
                if (l % 2 == 1 && c % 2 == 1 && Sorteio.chance(80)) {
                    g[l][c] = 'X';
                }
            }
        }
        int l = Sorteio.entre(TAMANHO);
        int c = Sorteio.entre(TAMANHO);
        while (g[l][c] != '.') {
            l = Sorteio.entre(TAMANHO);
            c = Sorteio.entre(TAMANHO);
        }
        g[l][c] = 'P';
        return g;
    }

    public static ArrayList<Item> preencher(char[][] g, int dificuldade) {
        int pl = 0;
        int pc = 0;
        for (int l = 0; l < g.length; l++) {
            for (int c = 0; c < g.length; c++) {
                if (g[l][c] == 'P') {
                    pl = l;
                    pc = c;
                }
            }
        }

        colocarInimigos(g, 'C', 3, pl, pc);
        colocarInimigos(g, 'R', 2, pl, pc);
        if (dificuldade == Jogo.DIFICIL) {
            colocarInimigos(g, 'S', 2, pl, pc);
        }

        ArrayList<int[]> caixotes = new ArrayList<int[]>();
        for (int l = 0; l < g.length; l++) {
            for (int c = 0; c < g.length; c++) {
                boolean longe = Math.abs(l - pl) + Math.abs(c - pc) > 2;
                if (g[l][c] == '.' && longe && Sorteio.chance(45)) {
                    g[l][c] = '#';
                    caixotes.add(new int[] {l, c});
                }
            }
        }

        ArrayList<Item> itens = new ArrayList<Item>();
        int saida = Sorteio.entre(caixotes.size());
        for (int i = 0; i < caixotes.size(); i++) {
            int[] p = caixotes.get(i);
            if (i == saida) {
                itens.add(new Item(Item.SAIDA, p[0], p[1]));
            } else if (Sorteio.chance(25)) {
                if (Sorteio.chance(50)) {
                    itens.add(new Item(Item.RAIO, p[0], p[1]));
                } else {
                    itens.add(new Item(Item.BOMBA, p[0], p[1]));
                }
            }
        }
        return itens;
    }

    private static void colocarInimigos(char[][] g, char tipo, int quantidade, int pl, int pc) {
        int colocados = 0;
        while (colocados < quantidade) {
            int l = Sorteio.entre(g.length);
            int c = Sorteio.entre(g.length);
            boolean longe = Math.abs(l - pl) + Math.abs(c - pc) >= 5;
            if (g[l][c] == '.' && longe) {
                g[l][c] = tipo;
                colocados++;
            }
        }
    }
}
