package com.alticelabs.controller;

import com.alticelabs.models.User;
import com.alticelabs.repository.KafkaMaterializedView;

import java.util.Scanner;

public class UserMaterializedController {
    private final KafkaMaterializedView kafkaMaterializedView;

    public UserMaterializedController(KafkaMaterializedView kafkaMaterializedView) {
        this.kafkaMaterializedView = kafkaMaterializedView;
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
            if (s == 1) getUserAccount(scanner);
            else if (s == 2) leave = true;
            else System.out.println("Opção inválida!");
        }
        System.out.println("Shutdown user service!");
    }

    private void getUserAccount(Scanner scanner) {
        System.out.println("Indica o id do usuário!");
        String id = scanner.next();
        User user = kafkaMaterializedView.getUser(id);
        if (user != null) {
            System.out.println(user);
        }
        else System.out.println("Nenhum usuário foi encontrado com o ID pedido!");
    }



    private void showMenu() {
        System.out.println("1-Pesquisar user!");
        System.out.println("2-Sair!");
    }
}
