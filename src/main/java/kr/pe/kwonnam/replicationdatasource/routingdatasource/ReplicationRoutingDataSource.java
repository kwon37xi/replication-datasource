package kr.pe.kwonnam.replicationdatasource.routingdatasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * {@link org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource}와
 * {@link org.springframework.transaction.support.TransactionSynchronizationManager}를 통해
 * Transaction의 readOnly 값에 따라 데이터 소스 분기
 */
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
    private Logger log = LoggerFactory.getLogger(ReplicationRoutingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceType = TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "read" : "write";
        log.info("current dataSourceType : {}", dataSourceType);
        return dataSourceType;
    }
}
