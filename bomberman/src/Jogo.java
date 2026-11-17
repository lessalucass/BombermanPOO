import java.io.Serializable;
import java.util.ArrayList;

public class Jogo implements Serializable {
    public static final int FACIL = 1;
    public static final int DIFICIL = 2;
    public static final int EM_ANDAMENTO = 0;
    public static final int VITORIA = 1;
    public static final int DERROTA = 2;

    private String nome;
    private int dificuldade;
    private Tabuleiro tabuleiro;
    private int turno;
    private int dicasUsadas;
    private int estado;
    private ArrayList<String> mensagens = new ArrayList<String>();
    private char[][] gradeInicial;
    private ArrayList<Item> itensIniciais;

    public Jogo(String nome, int dificuldade) {
        this.nome = nome;
        this.dificuldade = dificuldade;
        this.estado = EM_ANDAMENTO;
        gradeInicial = GeradorFase.criarGrade();
        itensIniciais = GeradorFase.preencher(gradeInicial, dificuldade);
        montarFase(3);
    }

    public Jogo reiniciar() {
        return new Jogo(nome, dificuldade);
    }

    private void montarFase(int vidas) {
        tabuleiro = new Tabuleiro(gradeInicial);
        for (Item it : itensIniciais) {
            tabuleiro.adicionarItem(new Item(it.getTipo(), it.getLinha(), it.getColuna()));
        }
        tabuleiro.getJogador().setVidas(vidas);
        turno = 0;
    }

    public void mover(char tecla) throws MovimentoInvalidoException {
        int[] d;
        if (tecla == 'W' || tecla == 'w') {
            d = Tabuleiro.DIRECOES[0];
        } else if (tecla == 'S' || tecla == 's') {
            d = Tabuleiro.DIRECOES[1];
        } else if (tecla == 'A' || tecla == 'a') {
            d = Tabuleiro.DIRECOES[2];
        } else if (tecla == 'D' || tecla == 'd') {
            d = Tabuleiro.DIRECOES[3];
        } else {
            throw new MovimentoInvalidoException("Use W, A, S ou D.");
        }
        Jogador j = tabuleiro.getJogador();
        int nl = j.getLinha() + d[0];
        int nc = j.getColuna() + d[1];
        if (!tabuleiro.dentro(nl, nc)) {
            throw new MovimentoInvalidoException("Você não pode sair do mapa.");
        }
        Obstaculo o = tabuleiro.obstaculoEm(nl, nc);
        if (o != null && o.explodeAoToque()) {
            tabuleiro.limparFogo();
            o.sofrerExplosao(tabuleiro);
        } else if (o != null) {
            throw new MovimentoInvalidoException("Tem um obstáculo no caminho.");
        } else if (tabuleiro.bombaEm(nl, nc) != null) {
            throw new MovimentoInvalidoException("Tem uma bomba no caminho.");
        } else {
            tabuleiro.limparFogo();
            j.setPosicao(nl, nc);
            verificarItem(true);
        }
        avancarTurno();
    }

    public void plantarBomba() throws LimiteDeBombaAtingidoException {
        Jogador j = tabuleiro.getJogador();
        if (tabuleiro.getBombas().size() >= j.getBombasSimultaneas()) {
            throw new LimiteDeBombaAtingidoException("Você já plantou o máximo de bombas (" + j.getBombasSimultaneas() + ").");
        }
        if (tabuleiro.bombaEm(j.getLinha(), j.getColuna()) != null) {
            throw new LimiteDeBombaAtingidoException("Já existe uma bomba aqui.");
        }
        tabuleiro.limparFogo();
        tabuleiro.adicionarBomba(new Bomba(j.getLinha(), j.getColuna(), j.getRaioExplosao()));
        mensagens.add("Bomba plantada! Ela explode em " + Bomba.TEMPO + " turnos.");
        avancarTurno();
    }

    public void esperar() {
        tabuleiro.limparFogo();
        avancarTurno();
    }

    public String pedirDica() {
        if (dicasUsadas >= 3) {
            return "Suas 3 dicas já acabaram.";
        }
        dicasUsadas++;
        Jogador j = tabuleiro.getJogador();
        String resposta = "Nenhum power-up escondido por perto.";
        if (tabuleiro.temPowerUpEscondidoPerto(j.getLinha(), j.getColuna())) {
            resposta = "Tem um power-up escondido por perto!";
        }
        return resposta + " (dicas restantes: " + (3 - dicasUsadas) + ")";
    }

    private void avancarTurno() {
        if (estado != EM_ANDAMENTO) {
            return;
        }
        turno++;
        int antes = tabuleiro.getInimigos().size();
        tabuleiro.passarTurnoDasBombas();
        int mortos = antes - tabuleiro.getInimigos().size();
        if (mortos > 0) {
            mensagens.add(mortos + " inimigo(s) eliminado(s)!");
        }
        if (foiAtingido()) {
            return;
        }

        for (Inimigo i : tabuleiro.getInimigos()) {
            i.mover(tabuleiro, turno);
        }
        ArrayList<Inimigo> inimigos = tabuleiro.getInimigos();
        for (int i = inimigos.size() - 1; i >= 0; i--) {
            if (tabuleiro.emFogo(inimigos.get(i).getLinha(), inimigos.get(i).getColuna())) {
                inimigos.remove(i);
            }
        }

        Jogador j = tabuleiro.getJogador();
        if (tabuleiro.inimigoEm(j.getLinha(), j.getColuna()) != null) {
            perderVida("Um inimigo te pegou!");
            return;
        }
        if (foiAtingido()) {
            return;
        }
        verificarItem(false);
    }

    private boolean foiAtingido() {
        if (tabuleiro.isJogadorAtingido()) {
            tabuleiro.setJogadorAtingido(false);
            perderVida("Você foi atingido por uma explosão!");
            return true;
        }
        return false;
    }

    private void verificarItem(boolean avisar) {
        Jogador j = tabuleiro.getJogador();
        Item item = tabuleiro.itemVisivelEm(j.getLinha(), j.getColuna());
        if (item == null) {
            return;
        }
        if (item.getTipo() == Item.RAIO) {
            j.setRaioExplosao(j.getRaioExplosao() + 1);
            tabuleiro.removerItem(item);
            mensagens.add("Power-up: o raio da explosão aumentou!");
        } else if (item.getTipo() == Item.BOMBA) {
            j.setBombasSimultaneas(j.getBombasSimultaneas() + 1);
            tabuleiro.removerItem(item);
            mensagens.add("Power-up: você pode plantar mais uma bomba!");
        } else if (tabuleiro.getInimigos().size() == 0) {
            estado = VITORIA;
        } else if (avisar) {
            mensagens.add("Achou a saída, mas ainda restam " + tabuleiro.getInimigos().size() + " inimigo(s)!");
        }
    }

    private void perderVida(String motivo) {
        mensagens.addAll(tabuleiro.getEventos());
        Jogador j = tabuleiro.getJogador();
        int vidas = j.getVidas() - 1;
        mensagens.add(motivo + " Vidas restantes: " + vidas);
        if (vidas <= 0) {
            estado = DERROTA;
            return;
        }
        montarFase(vidas);
        mensagens.add("A fase foi reiniciada.");
    }

    public String desenharTabuleiro() {
        return tabuleiro.desenhar();
    }

    public String status() {
        Jogador j = tabuleiro.getJogador();
        return nome + " | Vidas: " + j.getVidas()
                + " | Bombas: " + tabuleiro.getBombas().size() + "/" + j.getBombasSimultaneas()
                + " | Raio: " + j.getRaioExplosao()
                + " | Turno: " + turno
                + " | Inimigos: " + tabuleiro.getInimigos().size();
    }

    public ArrayList<String> pegarMensagens() {
        ArrayList<String> saida = new ArrayList<String>();
        saida.addAll(mensagens);
        saida.addAll(tabuleiro.getEventos());
        mensagens.clear();
        tabuleiro.getEventos().clear();
        return saida;
    }

    public int getEstado() {
        return estado;
    }

    public String getNome() {
        return nome;
    }
}
