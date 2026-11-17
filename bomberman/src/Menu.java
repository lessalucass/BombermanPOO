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
            System.out.println("\nMenu:\n1- Movimentar\n2- Plantar Bomba\n3- Esperar\n4- Dica\n5- DEBUG\n6- Sair");
            int op = lerNumero("Escolha uma opção: ");
            try {
                if (op == 1) {
                    System.out.print("Qual a direção (W para Cima, S para Baixo, A para Esquerda, D para Direita)? ");
                    String texto = teclado.nextLine().trim();
                    if (texto.length() != 1) {
                        throw new MovimentoInvalidoException("Digite só uma letra: W, A, S ou D.");
                    }
                    jogo.mover(texto.charAt(0));
                } else if (op == 2) {
                    jogo.plantarBomba();
                } else if (op == 3) {
                    jogo.esperar();
                } else if (op == 4) {
                    System.out.println(">> " + jogo.pedirDica());
                } else if (op == 5) {
                    System.out.println("\n[DEBUG]\n" + jogo.desenharTabuleiro(true));
                } else if (op == 6) {
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
        System.out.println(jogo.desenharTabuleiro(false));
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
