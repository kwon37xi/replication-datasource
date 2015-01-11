package kr.pe.kwonnam.replicationdatasource.config;

import kr.pe.kwonnam.replicationdatasource.LazyReplicationConnectionDataSourceProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * {@link kr.pe.kwonnam.replicationdatasource.LazyReplicationConnectionDataSourceProxy} 를 통한 리플리케이션 테스트 설정
 * *
 * 동일한 테이블에 동일한 양의 데이터가 들어있지만 쓰기쪽은 write_x 읽기 쪽은 read_x 라는 형태의 name 컬럼 데이터를
 * 가지도록 조작한 두 개의 데이터 소스를 생성한다.
 * <p/>
 * 실전 환경에서는 writeDataSource는 Master DB를, readDataSource는 Slave DB를 바라보는 커넥션 풀이어야 한다.
 */
@Configuration
public class WithLazyReplicationConnectionDataSourceProxyConfig {
    @Bean(destroyMethod = "shutdown")
    public DataSource writeDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
                .setName("lazyWriteDb")
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:/writedb.sql");
        return builder.build();
    }

    @Bean(destroyMethod = "shutdown")
    public DataSource readDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
                .setName("lazyReadDb")
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:/readdb.sql");
        return builder.build();
    }
    
    @Bean
    public DataSource dataSource(@Qualifier("writeDataSource") DataSource writeDataSource, @Qualifier("readDataSource") DataSource readDataSource) {
        return new LazyReplicationConnectionDataSourceProxy(writeDataSource, readDataSource);
    }
}