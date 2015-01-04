package kr.pe.kwonnam.lazyreplicationdatasourceproxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource writeDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:/writedb.sql");
        return builder.build();
    }

    @Bean
    public DataSource readDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:/writedb.sql");
        return builder.build();
    }


}
