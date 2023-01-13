package com.alticelabs.userES.controller;

import com.alticelabs.userES.models.UserEvent;
import com.alticelabs.userES.service.UserESService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Scanner;

@Component
public class ExagonController {
    private final UserESService service;

    public ExagonController(UserESService service) {
        this.service = service;
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
            if (s == 1) showUser(scanner);
            else if (s == 2) showHistory(scanner);
            else if (s == 3) showUserFromPast(scanner);
            else if (s == 4) leave = true;
            else System.out.println("Opção inválida!");
        }
        System.out.println("Shutdown user service!");
    }

    private void showUserFromPast(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        System.out.println("Indica o timestamp desejado!");
        Date timestamp = new Date(scanner.nextLong());
        System.out.println(service.getUserFromPast(id,timestamp));
    }

    private void showHistory(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        for (UserEvent userEvent : service.getAllEventsForUser(id)) {
            System.out.println(userEvent);
        }

    }

    private void showUser(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        System.out.println(service.getUser(id));
    }

    private void showMenu() {
        System.out.println("1-Visualizar conta do utilizador!");
        System.out.println("2-Visualizar últimas transações do utilizador!");
        System.out.println("3-Visualizar conta do utilizador no passado!");
        System.out.println("4-Sair!");
    }
}
