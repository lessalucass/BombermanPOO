import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Arquivos {
    public static void salvar(Jogo jogo, String nomeArquivo) throws ArquivoJogoException {
        try (ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            saida.writeObject(jogo);
        } catch (IOException e) {
            throw new ArquivoJogoException("Não foi possível salvar o jogo.");
        }
    }

    public static Jogo carregar(String nomeArquivo) throws ArquivoJogoException {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            return (Jogo) entrada.readObject();
        } catch (FileNotFoundException e) {
            throw new ArquivoJogoException("Jogo salvo não encontrado: " + nomeArquivo);
        } catch (IOException | ClassNotFoundException e) {
            throw new ArquivoJogoException("O arquivo não é um jogo salvo válido.");
        }
    }
}
