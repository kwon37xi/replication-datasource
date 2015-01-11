package kr.pe.kwonnam.replicationdatasource;

import kr.pe.kwonnam.replicationdatasource.config.WithLazyReplicationConnectionDataSourceProxyConfig;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * Replication with {@link kr.pe.kwonnam.rezyreplicationdatasourceproxy.LazyReplicationConnectionDataSourceProxy}.
 */
@ContextConfiguration(classes = {WithLazyReplicationConnectionDataSourceProxyConfig.class})
@DirtiesContext
public class LazyReplicationConnectionDataSourceProxySpringIntegrationTest extends AbstractReplicationDataSourceIntegrationTest {
}
