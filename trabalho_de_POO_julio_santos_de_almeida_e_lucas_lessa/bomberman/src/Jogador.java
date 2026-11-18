public class Jogador extends Entidade implements Destrutivel {
    private int vidas;
    private int bombasSimultaneas;
    private int raioExplosao;

    public Jogador(int linha, int coluna) {
        super(linha, coluna);
        vidas = 3;
        bombasSimultaneas = 1;
        raioExplosao = 1;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getBombasSimultaneas() {
        return bombasSimultaneas;
    }

    public void setBombasSimultaneas(int bombasSimultaneas) {
        this.bombasSimultaneas = bombasSimultaneas;
    }

    public int getRaioExplosao() {
        return raioExplosao;
    }

    public void setRaioExplosao(int raioExplosao) {
        this.raioExplosao = raioExplosao;
    }

    @Override
    public void sofrerExplosao(Tabuleiro tabuleiro) {
        tabuleiro.setJogadorAtingido(true);
    }

    @Override
    public char simbolo() {
        return 'P';
    }
}
