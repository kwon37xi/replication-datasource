package kr.pe.kwonnam.replicationdatasource.config;

import kr.pe.kwonnam.rezyreplicationdatasourceproxy.LazyReplicationConnectionDataSourceProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class WithLazyReplicationConnectionDataSourceProxyConfig {

    @Bean
    public DataSource dataSource(@Qualifier("writeDataSource") DataSource writeDataSource, @Qualifier("readDataSource")DataSource readDataSource) {
        return new LazyReplicationConnectionDataSourceProxy(writeDataSource, readDataSource);
    }
}