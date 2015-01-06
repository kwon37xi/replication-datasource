package kr.pe.kwonnam.lazyreplicationdatasourceproxy;

import kr.pe.kwonnam.lazyreplicationdatasourceproxy.config.WithLazyReplicationConnectionDataSourceProxyConfig;
import org.springframework.test.context.ContextConfiguration;

/**
 * Replication with {@link kr.pe.kwonnam.rezyreplicationdatasourceproxy.LazyReplicationConnectionDataSourceProxy}.
 */
@ContextConfiguration(classes = {WithLazyReplicationConnectionDataSourceProxyConfig.class})
public class ReplicationLazyConnectionDataSourceProxyIntegrationTest {
}
