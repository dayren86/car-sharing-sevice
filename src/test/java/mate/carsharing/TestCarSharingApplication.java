package mate.carsharing;

import org.springframework.boot.SpringApplication;

public class TestCarSharingApplication {

	public static void main(String[] args) {
		SpringApplication.from(CarSharingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
