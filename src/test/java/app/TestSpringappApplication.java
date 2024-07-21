package app;

import org.springframework.boot.SpringApplication;

public class TestSpringappApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringappApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
