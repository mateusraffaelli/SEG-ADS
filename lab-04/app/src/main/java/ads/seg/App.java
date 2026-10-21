package ads.seg;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Teste do UserService ===");
        System.out.println("Algoritmo de hash:");
        System.out.println("1 - BCrypt");
        System.out.println("2 - PBKDF2");
        System.out.print("Escolha: ");
        String escolhaAlgoritmo = scanner.nextLine().trim();

        UserService userService;
        if (escolhaAlgoritmo.equals("2")) {
            userService = new UserService(new InMemory(), "PBKDF2");
            System.out.println("Usando PBKDF2.");
        } else {
            userService = new UserService(new InMemory());
            System.out.println("Usando BCrypt.");
        }

        boolean executando = true;
        while (executando) {
            System.out.println();
            System.out.println("--- Menu ---");
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Autenticar (login)");
            System.out.println("3 - Alterar senha");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    cadastrar(scanner, userService);
                    break;
                case "2":
                    autenticar(scanner, userService);
                    break;
                case "3":
                    alterarSenha(scanner, userService);
                    break;
                case "0":
                    executando = false;
                    System.out.println("Encerrando.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }

    private static void cadastrar(Scanner scanner, UserService userService) {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        try {
            if (userService.register(login, senha)) {
                System.out.println("Usuário cadastrado com sucesso.");
            } else {
                System.out.println("Não foi possível cadastrar.");
            }
        } catch (UserAlreadyExistsException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Erro de criptografia: " + e.getMessage());
        }
    }

    private static void autenticar(Scanner scanner, UserService userService) {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        try {
            if (userService.authenticate(login, senha)) {
                System.out.println("Autenticado com sucesso.");
            }
        } catch (InvalidLoginException | InvalidPasswordException e) {
            System.out.println("Falha na autenticação: " + e.getMessage());
        }
    }

    private static void alterarSenha(Scanner scanner, UserService userService) {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Senha atual: ");
        String senhaAntiga = scanner.nextLine();
        System.out.print("Nova senha: ");
        String novaSenha = scanner.nextLine();

        try {
            if (userService.updatePassword(login, senhaAntiga, novaSenha)) {
                System.out.println("Senha alterada com sucesso.");
            } else {
                System.out.println("Não foi possível alterar a senha.");
            }
        } catch (InvalidLoginException | InvalidPasswordException e) {
            System.out.println("Falha na autenticação: " + e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Erro de criptografia: " + e.getMessage());
        }
    }
}