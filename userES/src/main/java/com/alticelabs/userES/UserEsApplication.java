package com.alticelabs.userES;

import com.alticelabs.userES.controller.ExagonController;
import com.alticelabs.userES.repository.ExagonUpdater;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@Configuration
public class UserEsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserEsApplication.class, args);
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Bean
	public CommandLineRunner schedulingRunner(TaskExecutor executor,ExagonUpdater exagonUpdater) {
		return args -> executor.execute(exagonUpdater);
	}

	@Bean
	public CommandLineRunner consoleRunner(ExagonController exagonController) {
		return args -> {
			exagonController.run();
			System.exit(0);
		};
	}
}
