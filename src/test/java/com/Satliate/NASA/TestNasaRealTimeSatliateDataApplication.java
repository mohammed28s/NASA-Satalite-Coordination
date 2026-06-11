package com.Satliate.NASA;

import org.springframework.boot.SpringApplication;

public class TestNasaRealTimeSatliateDataApplication {

	public static void main(String[] args) {
		SpringApplication.from(NasaRealTimeSatliateDataApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
