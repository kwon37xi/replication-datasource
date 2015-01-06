package kr.pe.kwonnam.lazyreplicationdatasourceproxy;

import kr.pe.kwonnam.lazyreplicationdatasourceproxy.config.WithLazyReplicationConnectionDataSourceProxyConfig;
import org.springframework.test.context.ContextConfiguration;

/**
 * Replication with {@link kr.pe.kwonnam.lazyreplicationdatasourceproxy.routingdatasource.ReplicationRoutingDataSource}.
 *
 */
@ContextConfiguration(classes = {WithLazyReplicationConnectionDataSourceProxyConfig.class})
public class ReplicationRoutingDataSourceIntegrationTest extends AbstractReplicationDataSourceIntegrationTest {
}
