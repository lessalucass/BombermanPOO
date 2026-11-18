public abstract class Obstaculo extends Entidade implements Destrutivel {
    public Obstaculo(int linha, int coluna) {
        super(linha, coluna);
    }

    public boolean bloqueiaFogo() {
        return false;
    }

    public boolean explodeAoToque() {
        return false;
    }
}
