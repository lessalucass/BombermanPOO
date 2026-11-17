import java.io.Serializable;
import java.util.ArrayList;

public class Tabuleiro implements Serializable {
    public static final int[][] DIRECOES = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private int tamanho;
    private Obstaculo[][] obstaculos;
    private boolean[][] fogo;
    private Jogador jogador;
    private ArrayList<Inimigo> inimigos = new ArrayList<Inimigo>();
    private ArrayList<Bomba> bombas = new ArrayList<Bomba>();
    private ArrayList<Item> itens = new ArrayList<Item>();
    private ArrayList<String> eventos = new ArrayList<String>();
    private boolean jogadorAtingido;

    public Tabuleiro(char[][] grade) {
        tamanho = grade.length;
        obstaculos = new Obstaculo[tamanho][tamanho];
        fogo = new boolean[tamanho][tamanho];
        for (int l = 0; l < tamanho; l++) {
            for (int c = 0; c < tamanho; c++) {
                char ch = grade[l][c];
                if (ch == 'X') {
                    obstaculos[l][c] = new Parede(l, c);
                } else if (ch == '#') {
                    obstaculos[l][c] = new Caixote(l, c);
                } else if (ch == 'P') {
                    jogador = new Jogador(l, c);
                } else if (ch == 'C') {
                    inimigos.add(new InimigoComum(l, c));
                } else if (ch == 'R') {
                    inimigos.add(new InimigoRastreador(l, c));
                } else if (ch == 'S') {
                    inimigos.add(new InimigoSabotador(l, c));
                }
            }
        }
    }

    public static int sinal(int x) {
        if (x > 0) {
            return 1;
        }
        if (x < 0) {
            return -1;
        }
        return 0;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public ArrayList<Inimigo> getInimigos() {
        return inimigos;
    }

    public ArrayList<Bomba> getBombas() {
        return bombas;
    }

    public boolean dentro(int l, int c) {
        return l >= 0 && c >= 0 && l < tamanho && c < tamanho;
    }

    public Obstaculo obstaculoEm(int l, int c) {
        return obstaculos[l][c];
    }

    public Bomba bombaEm(int l, int c) {
        for (Bomba b : bombas) {
            if (b.getLinha() == l && b.getColuna() == c) {
                return b;
            }
        }
        return null;
    }

    public Inimigo inimigoEm(int l, int c) {
        for (Inimigo i : inimigos) {
            if (i.getLinha() == l && i.getColuna() == c) {
                return i;
            }
        }
        return null;
    }

    public Item itemEm(int l, int c) {
        for (Item it : itens) {
            if (it.getLinha() == l && it.getColuna() == c) {
                return it;
            }
        }
        return null;
    }

    public Item itemVisivelEm(int l, int c) {
        if (obstaculos[l][c] == null) {
            return itemEm(l, c);
        }
        return null;
    }

    public boolean livreParaInimigo(int l, int c) {
        return dentro(l, c) && obstaculos[l][c] == null && bombaEm(l, c) == null && inimigoEm(l, c) == null;
    }

    public boolean celulaVazia(int l, int c) {
        return livreParaInimigo(l, c) && itemEm(l, c) == null
                && !(jogador.getLinha() == l && jogador.getColuna() == c);
    }

    public boolean caminhoLivre(int l1, int c1, int l2, int c2) {
        if (l1 != l2 && c1 != c2) {
            return false;
        }
        int dl = sinal(l2 - l1);
        int dc = sinal(c2 - c1);
        int l = l1 + dl;
        int c = c1 + dc;
        while (l != l2 || c != c2) {
            if (obstaculos[l][c] != null) {
                return false;
            }
            l += dl;
            c += dc;
        }
        return true;
    }

    public Bomba bombaDesarmavelPerto(int l, int c) {
        Bomba melhor = null;
        int menor = 7;
        for (Bomba b : bombas) {
            int d = Math.abs(b.getLinha() - l) + Math.abs(b.getColuna() - c);
            if (b.desarmavel() && d < menor) {
                menor = d;
                melhor = b;
            }
        }
        return melhor;
    }

    public boolean temPowerUpEscondidoPerto(int l, int c) {
        for (Item it : itens) {
            boolean perto = Math.abs(it.getLinha() - l) <= 3 && Math.abs(it.getColuna() - c) <= 3;
            if (it.getTipo() != Item.SAIDA && obstaculos[it.getLinha()][it.getColuna()] != null && perto) {
                return true;
            }
        }
        return false;
    }

    public void setObstaculo(Obstaculo o) {
        obstaculos[o.getLinha()][o.getColuna()] = o;
    }

    public void removerObstaculo(int l, int c) {
        obstaculos[l][c] = null;
    }

    public void adicionarItem(Item item) {
        itens.add(item);
    }

    public void removerItem(Item item) {
        itens.remove(item);
    }

    public void adicionarBomba(Bomba bomba) {
        bombas.add(bomba);
    }

    public void removerBomba(Bomba bomba) {
        bombas.remove(bomba);
    }

    public void removerInimigo(Inimigo inimigo) {
        inimigos.remove(inimigo);
    }

    public void setJogadorAtingido(boolean atingido) {
        jogadorAtingido = atingido;
    }

    public boolean isJogadorAtingido() {
        return jogadorAtingido;
    }

    public void adicionarEvento(String texto) {
        eventos.add(texto);
    }

    public ArrayList<String> getEventos() {
        return eventos;
    }

    public void limparFogo() {
        for (int l = 0; l < tamanho; l++) {
            for (int c = 0; c < tamanho; c++) {
                fogo[l][c] = false;
            }
        }
    }

    public boolean emFogo(int l, int c) {
        return fogo[l][c];
    }

    public void passarTurnoDasBombas() {
        for (Bomba b : bombas) {
            b.passarTurno();
        }
        boolean explodiu = true;
        while (explodiu) {
            explodiu = false;
            for (int i = 0; i < bombas.size(); i++) {
                Bomba b = bombas.get(i);
                if (b.pronta()) {
                    bombas.remove(i);
                    detonar(b.getLinha(), b.getColuna(), b.getRaio());
                    explodiu = true;
                    break;
                }
            }
        }
    }

    public void detonar(int l, int c, int raio) {
        atingir(l, c);
        for (int[] d : DIRECOES) {
            for (int i = 1; i <= raio; i++) {
                int nl = l + d[0] * i;
                int nc = c + d[1] * i;
                if (!dentro(nl, nc)) {
                    break;
                }
                Obstaculo o = obstaculos[nl][nc];
                if (o != null && o.bloqueiaFogo()) {
                    break;
                }
                atingir(nl, nc);
                if (o != null) {
                    break;
                }
            }
        }
    }

    private void atingir(int l, int c) {
        fogo[l][c] = true;
        for (Bomba b : bombas) {
            if (b.getLinha() == l && b.getColuna() == c) {
                b.detonarAgora();
            }
        }
        ArrayList<Destrutivel> alvos = new ArrayList<Destrutivel>();
        if (obstaculos[l][c] != null) {
            alvos.add(obstaculos[l][c]);
        }
        for (Inimigo i : inimigos) {
            if (i.getLinha() == l && i.getColuna() == c) {
                alvos.add(i);
            }
        }
        if (jogador.getLinha() == l && jogador.getColuna() == c) {
            alvos.add(jogador);
        }
        for (Destrutivel d : alvos) {
            d.sofrerExplosao(this);
        }
    }

    public String desenhar() {
        StringBuilder sb = new StringBuilder("    ");
        for (int c = 0; c < tamanho; c++) {
            sb.append(String.format("%-3d", c));
        }
        sb.append("\n");
        for (int l = 0; l < tamanho; l++) {
            sb.append(String.format("%2d  ", l));
            for (int c = 0; c < tamanho; c++) {
                sb.append(simboloDe(l, c)).append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private char simboloDe(int l, int c) {
        if (fogo[l][c]) {
            return '+';
        }
        if (jogador.getLinha() == l && jogador.getColuna() == c) {
            return jogador.simbolo();
        }
        Inimigo inimigo = inimigoEm(l, c);
        if (inimigo != null) {
            return inimigo.simbolo();
        }
        if (bombaEm(l, c) != null) {
            return 'B';
        }
        Obstaculo o = obstaculos[l][c];
        if (o != null) {
            return o.simbolo();
        }
        if (itemEm(l, c) != null) {
            return itemEm(l, c).simbolo();
        }
        return '.';
    }
}
