package kr.pe.kwonnam.lazyreplicationdatasourceproxy.config;

import kr.pe.kwonnam.lazyreplicationdatasourceproxy.routingdatasource.ReplicationRoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WithRoutingDataSourceConfig {
    /**
     * {@link org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource}는
     * {@link org.springframework.beans.factory.InitializingBean}을 구현하므로,
     * 명시적으로 afterPropertiesSet()메소드를 호출하거나
     * 별도 @Bean으로 만들어 Spring Life Cycle을 타도록 해야 한다.
     */
    @Bean
    public DataSource routingDataSource(@Qualifier("writeDataSource") DataSource writeDataSource, @Qualifier("readDataSource") DataSource readDataSource) {
        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<Object, Object>();
        dataSourceMap.put("write", writeDataSource);
        dataSourceMap.put("read", readDataSource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(writeDataSource);

        return routingDataSource;
    }

    /**
     * {@link org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy}로 감싸서
     * 트랜잭션 동기화가 이루어진 뒤에 실제 커넥션을 확보하도록 해준다.
     *
     * @param routingDataSource
     * @return
     */
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }
}
