import java.io.Serializable;

public abstract class Entidade implements Serializable {
    protected int linha;
    protected int coluna;

    public Entidade(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setPosicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public abstract char simbolo();
}
