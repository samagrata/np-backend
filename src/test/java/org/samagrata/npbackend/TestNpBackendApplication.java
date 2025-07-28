package org.samagrata.npbackend;

import org.springframework.boot.SpringApplication;

public class TestNpBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(NpBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
