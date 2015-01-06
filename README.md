# replication-datasource

Lazy Replication(master/slave - write/read split) Connection DataSource(Connection Pool) Proxy([LazyReplicationConnectionDataSourceProxy](https://github.com/kwon37xi/replication-datasource/blob/master/src/main/java/kr/pe/kwonnam/rezyreplicationdatasourceproxy/LazyReplicationConnectionDataSourceProxy.java))
is copy & modify of Spring framework's [LazyConnectionDataSourceProxy](https://github.com/spring-projects/spring-framework/blob/master/spring-jdbc/src/main/java/org/springframework/jdbc/datasource/LazyConnectionDataSourceProxy.java).

This DataSource suports replication(master/slave | write/read) database connection and also lazy proxy means the connection is not acquired until real DB job is executed.

There are two ways of implementing replication datasources.

## Spring's LazyConnectionDataSourceProxy + AbstractRoutingDataSource

Refer to [ReplicationRoutingDataSource](https://github.com/kwon37xi/replication-datasource/blob/master/src/test/java/kr/pe/kwonnam/replicationdatasource/routingdatasource/ReplicationRoutingDataSource.java) and [LazyReplicationDataSourceProxyApplicationConfig](https://github.com/kwon37xi/replication-datasource/blob/master/src/test/java/kr/pe/kwonnam/replicationdatasource/config/LazyReplicationDataSourceProxyApplicationConfig.java).
You can make replication data source with only spring framework's two basic classes.

This works very nicely with Spring's [TransactionSynchronizationManager](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/support/TransactionSynchronizationManager.html).

## LazyReplicationConnectionDataSourceProxy

I refered to Spring framework's [LazyConnectionDataSourceProxy](https://github.com/spring-projects/spring-framework/blob/master/spring-jdbc/src/main/java/org/springframework/jdbc/datasource/LazyConnectionDataSourceProxy.java) and modified a little for supporting replication
to make [LazyReplicationConnectionDataSourceProxy](https://github.com/kwon37xi/replication-datasource/blob/master/src/main/java/kr/pe/kwonnam/rezyreplicationdatasourceproxy/LazyReplicationConnectionDataSourceProxy.java).

This has features of LazyConnectionDataSourceProxy and support database replication(master/slave | read/write) routing.

```java
@Bean
public DataSource writeDataSource() {
    DataSource writeDataSource = ...;
    reaturn writeDataSource;
}

@Bean
public DataSource readDataSource() {
    DataSource readDataSource = ...;
    return readDataSource;
}


@Bean
public DataSource dataSource(DataSource writeDataSource, DataSource readDataSource) {
    return new LazyReplicationConnectionDataSourceProxy(writeDataSource, readDataSource);
}
```

when you use
```java
// working with read database
@Transactional(readOnly = true)
public Object readQuery() {
    ....
}

// working with write database
@Transactional(readOnly = false)
public void writeExection() {
    ....
}
```
