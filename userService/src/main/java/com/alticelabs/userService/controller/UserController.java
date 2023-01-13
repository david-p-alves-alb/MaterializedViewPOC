package com.alticelabs.userService.controller;



import com.alticelabs.userService.models.User;
import com.alticelabs.userService.models.UserEvent;
import com.alticelabs.userService.service.UserService;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Scanner;

@Controller
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
            int opcao = scanner.nextInt();
            if (opcao == 1) createUser(scanner);
            else if (opcao == 2) creditBalance(scanner);
            else if (opcao == 3) debitBalance(scanner);
            else if (opcao == 4) getUserAccount(scanner);
            else if (opcao == 5) getUserFromPast(scanner);
            else if (opcao == 6) getHistoryFromUser(scanner);
            else if (opcao == 7) leave = true;
            else System.out.println("Opção inválida!");
        }
        System.out.println("Shutdown user service!");
    }

    private void getHistoryFromUser(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        for (UserEvent userEvent : userService.getHistoryFromUser(id)) {
            System.out.println(userEvent);
        }
    }

    private void getUserFromPast(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        System.out.println("Indica o timestamp desejado!");
        Date timestamp = new Date(scanner.nextLong());
        User user = userService.getUserFromPast(id,timestamp);
        if (user != null) {
            System.out.println(user);
        }
        else System.out.println("Nenhum usuário foi encontrado com o ID pedido!");
    }

    private void getUserAccount(Scanner scanner) {
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
        Date creditBalanceDate = userService.creditBalance(id, amount);
        System.out.println("Crédito realizado no instante : " + creditBalanceDate.getTime());
    }

    private void debitBalance(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        System.out.println("Indica o montante do levantamento!");
        int amount = scanner.nextInt();
        Date debitBalanceDate = userService.debitBalance(id, amount);
        System.out.println("Débito realizado no instante : " + debitBalanceDate.getTime());
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
        System.out.println("5-Visualizar conta de um user no passado!");
        System.out.println("6-Visualizar histórico do user!");
        System.out.println("7-Sair!");
    }
}
