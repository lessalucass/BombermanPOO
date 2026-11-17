import java.util.Scanner;

public class Menu {
    private Scanner teclado;
    private Jogo jogoPausado;

    public Menu(Scanner teclado) {
        this.teclado = teclado;
    }

    public void iniciar() {
        System.out.println("Bomberman");
        boolean sair = false;
        while (!sair) {
            System.out.println("\n1- Jogar\n2- Sair");
            int op = lerNumero("Escolha uma opção: ");
            if (op == 1) {
                jogar();
            } else if (op == 2) {
                sair = querSair();
            } else {
                System.out.println("Opção inválida.");
            }
        }
        System.out.println("Até a próxima!");
    }

    private boolean querSair() {
        if (jogoPausado == null) {
            return true;
        }
        System.out.println("\nVocê tem um jogo em andamento.\n1- Salvar e sair\n2- Sair sem salvar\n3- Voltar");
        int op = lerNumero("Escolha uma opção: ");
        if (op == 1) {
            System.out.print("Nome do arquivo para salvar: ");
            String nome = teclado.nextLine().trim();
            try {
                Arquivos.salvar(jogoPausado, nome + ".sav");
                System.out.println("Jogo salvo em " + nome + ".sav");
                return true;
            } catch (ArquivoJogoException e) {
                System.out.println("Erro: " + e.getMessage());
                return false;
            }
        }
        return op == 2;
    }

    private void jogar() {
        System.out.println("\n1- Novo jogo\n2- Carregar jogo salvo\n3- Voltar");
        int op = lerNumero("Escolha uma opção: ");
        Jogo jogo;
        if (op == 1) {
            jogo = novoJogo();
        } else if (op == 2) {
            System.out.print("Nome do arquivo salvo (sem .sav): ");
            String nome = teclado.nextLine().trim();
            try {
                jogo = Arquivos.carregar(nome + ".sav");
            } catch (ArquivoJogoException e) {
                System.out.println("Erro: " + e.getMessage());
                return;
            }
        } else {
            return;
        }

        boolean continuar = true;
        while (continuar) {
            partida(jogo);
            if (jogo.getEstado() == Jogo.EM_ANDAMENTO) {
                jogoPausado = jogo;
            } else {
                jogoPausado = null;
            }
            System.out.println("\n1- Novo Jogo\n2- Reiniciar Jogo");
            if (lerNumero("Escolha uma opção: ") == 2) {
                jogo = jogo.reiniciar();
            } else {
                continuar = false;
            }
        }
    }

    private Jogo novoJogo() {
        String nome = "";
        while (nome.length() == 0) {
            System.out.print("Digite seu nome: ");
            nome = teclado.nextLine().trim();
        }
        int dificuldade = 0;
        while (dificuldade != Jogo.FACIL && dificuldade != Jogo.DIFICIL) {
            System.out.println("Dificuldade:\n1- Fácil (sem sabotador)\n2- Difícil");
            dificuldade = lerNumero("Escolha uma opção: ");
        }
        return new Jogo(nome, dificuldade);
    }

    private void partida(Jogo jogo) {
        System.out.println("\nP jogador | X parede | # caixote | C comum | R rastreador | S sabotador");
        System.out.println("B bomba | + fogo | * mais raio | & mais bombas | E saída");
        while (jogo.getEstado() == Jogo.EM_ANDAMENTO) {
            mostrarTela(jogo);
            System.out.println("\nMenu:\nMovimentar: W (cima), A (esquerda), S (baixo), D (direita)\n1- Plantar Bomba\n2- Esperar\n3- Dica\n4- Sair");
            System.out.print("Escolha uma opção: ");
            String entrada = teclado.nextLine().trim().toUpperCase();
            try {
                if (entrada.equals("W") || entrada.equals("A") || entrada.equals("S") || entrada.equals("D")) {
                    jogo.mover(entrada.charAt(0));
                } else if (entrada.equals("1")) {
                    jogo.plantarBomba();
                } else if (entrada.equals("2")) {
                    jogo.esperar();
                } else if (entrada.equals("3")) {
                    System.out.println(">> " + jogo.pedirDica());
                } else if (entrada.equals("4")) {
                    System.out.println("Jogo encerrado.");
                    return;
                } else {
                    System.out.println("Opção inválida.");
                }
            } catch (MovimentoInvalidoException e) {
                System.out.println("!! " + e.getMessage());
            } catch (LimiteDeBombaAtingidoException e) {
                System.out.println("!! " + e.getMessage());
        }
        }
        mostrarTela(jogo);
        if (jogo.getEstado() == Jogo.VITORIA) {
            System.out.println("\n*** VOCÊ VENCEU, " + jogo.getNome() + "! ***");
        } else {
            System.out.println("\n*** VOCÊ PERDEU! ***");
        }
    }

    private void mostrarTela(Jogo jogo) {
        System.out.println();
        for (String m : jogo.pegarMensagens()) {
            System.out.println(">> " + m);
        }
        System.out.println(jogo.desenharTabuleiro());
        System.out.println(jogo.status());
    }

    private int lerNumero(String pergunta) {
        while (true) {
            System.out.print(pergunta);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número.");
            }
        }
    }
}
