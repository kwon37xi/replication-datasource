package kr.pe.kwonnam.replicationdatasource.config;

import kr.pe.kwonnam.replicationdatasource.routingdatasource.ReplicationRoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link kr.pe.kwonnam.replicationdatasource.routingdatasource.ReplicationRoutingDataSource}와
 * {@link org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy}를 사용한
 * 리플리케이션 설정 예제.
 * <p/>
 * 동일한 테이블에 동일한 양의 데이터가 들어있지만 쓰기쪽은 write_x 읽기 쪽은 read_x 라는 형태의 name 컬럼 데이터를
 * 가지도록 조작한 두 개의 데이터 소스를 생성한다.
 * <p/>
 * 실전 환경에서는 writeDataSource는 Master DB를, readDataSource는 Slave DB를 바라보는 커넥션 풀이어야 한다.
 */
@Configuration
public class WithRoutingDataSourceConfig {
    @Bean(destroyMethod = "shutdown")
    public DataSource writeDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
                .setName("routingWriteDb")
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:/writedb.sql");
        return builder.build();
    }

    @Bean(destroyMethod = "shutdown")
    public DataSource readDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
                .setName("routingReadDb")
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:/readdb.sql");
        return builder.build();
    }

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
