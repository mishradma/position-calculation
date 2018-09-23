/**
 * 
 */
package com.org.mycompany.positions.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Dayanand Mishra
 *
 */
@Configuration
@EntityScan(basePackages = { "com.org.mycompany.positions.domain" })
@EnableJpaRepositories(basePackages = { "com.org.mycompany.positions.repository" })
public class DatabaseConfig {
	// @Bean
	// public DataSource dataSource() {
	// // jdbc:hsqldb:mem:testdb
	// EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
	// EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL)/*
	// .addScript("db/hsqldb/db.sql") */.build();
	// return db;
	// }
	//
	// @PostConstruct
	// public void getDbManager() {
	// DatabaseManagerSwing.main(new String[] { "--url", "jdbc:hsqldb:mem:testdb",
	// "--user", "sa", "--password", "" });
	// }

}
