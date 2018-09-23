package com.org.mycompany.positions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.org.mycompany.positions.config",
		"com.org.mycompany.positions.services" })
public class PositionsApplication implements CommandLineRunner {
	private static final Logger LOG = LoggerFactory.getLogger(PositionsApplication.class);

	public static void main(String[] args) {
		LOG.info(
				"***************************STARTING POSITION APPLICATION***************************************************");
		SpringApplication.run(PositionsApplication.class, args);
		LOG.info(
				"******************************************APPLICATION POSITION FINISHED************************************");

	}

	@Override
	public void run(String... args) {
		LOG.info(
				"**********************EXECUTING : Position application with command line runner****************************");
		for (int i = 0; i < args.length; ++i) {
			LOG.info("args[{}]: {}", i, args[i]);
		}
	}

	@Deprecated
	public static final void printUsage() {
		LOG.info("********************** Insufficient Parameter passed to Application****************************");
		LOG.info("********************** Position application Usage****************************");
		LOG.info("java <directory to poll>");

	}
}
