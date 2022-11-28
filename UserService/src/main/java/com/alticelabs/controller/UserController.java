package com.alticelabs.controller;

import com.alticelabs.models.User;
import com.alticelabs.service.UserService;

import java.util.Scanner;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    public void run() {
        //Run on the console
        Scanner scanner = new Scanner(System.in);
        boolean leave = false;

        System.out.println("Bem-vindo ao serviço de users!");
        System.out.println("Escolha a opção que deseja!");
        while (!leave) {
            showMenu();
            int s = scanner.nextInt();
            if (s == 1) createUser(scanner);
            else if (s == 2) creditBalance(scanner);
            else if (s == 3) debitBalance(scanner);
            else if (s == 4) getUserAccont(scanner);
            else if (s == 5) leave = true;
            else System.out.println("Opção inválida!");
        }
        System.out.println("Shutdown user service!");
    }

    private void getUserAccont(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        User user = userService.getUser(id);
        if (user != null) {
            System.out.println(user);
        }
        else System.out.println("Nenhum usuário foi encontrado com o ID pedido!");
    }

    private void creditBalance(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        System.out.println("Indica o montante do depósito!");
        int amount = scanner.nextInt();
        userService.creditBalance(id,amount);
    }

    private void debitBalance(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        System.out.println("Indica o montante do levantamento!");
        int amount = scanner.nextInt();
        userService.debitBalance(id,amount);
    }


    private void createUser(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        System.out.println("Indica o nome do usuário!");
        String name = scanner.next();
        userService.createUser(new User(id,name,0));
    }

    private void showMenu() {
        System.out.println("1-Criar user!");
        System.out.println("2-Depositar saldo num user!");
        System.out.println("3-Levantar saldo de um user!");
        System.out.println("4-Visualizar conta de um user!");
        System.out.println("5-Sair!");
    }
}
