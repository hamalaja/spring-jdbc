package vn.hamalaja.jdbc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * @author lamhm
 *
 */
@Configuration
public class DatabaseConfig {

	// @Value("${driverClassName}")
	// private String driverClassName;

	// @Bean
	// public JdbcTemplate jdbcTemplate() {
	// DriverManagerDataSource dataSource = new DriverManagerDataSource();
	// dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	// dataSource.setUrl("jdbc:mysql://localhost:3306/spring-series");
	// dataSource.setUsername("root");
	// dataSource.setPassword("123456789");
	// return new JdbcTemplate(dataSource);
	// }

	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(new EmbeddedDatabaseBuilder().generateUniqueName(true).setType(EmbeddedDatabaseType.H2).setScriptEncoding("UTF-8")
				.ignoreFailedDrops(true).addScript("schema.sql").addScripts("insert.sql").build());
	}
}
