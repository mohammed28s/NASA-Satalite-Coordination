package com.Satliate.NASA.real.time.Satliate.Data;

import org.springframework.boot.SpringApplication;

public class TestNasaRealTimeSatliateDataApplication {

	public static void main(String[] args) {
		SpringApplication.from(NasaRealTimeSatliateDataApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
