lazy-replication-datasource
=================================

Lazy Replication(master/slave - write/read split) Connection DataSource(Connection Pool) Proxy

[LazyReplicationConnectionDataSourceProxy](https://github.com/kwon37xi/replication-datasource/blob/master/src/main/java/kr/pe/kwonnam/rezyreplicationdatasourceproxy/LazyReplicationConnectionDataSourceProxy.java) 
is copy & modify of Spring framework's [LazyConnectionDataSourceProxy](https://github.com/spring-projects/spring-framework/blob/master/spring-jdbc/src/main/java/org/springframework/jdbc/datasource/LazyConnectionDataSourceProxy.java).

This DataSource suports replication(master/slave | write/read) database connection and also lazy proxy means the connection is not acquired until real DB job is executed.
