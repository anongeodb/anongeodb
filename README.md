# anongeodb

This is a temporary anonymized repository for GeoDB (temporary name).

This repository contains precompiled Sedona libraries for Spark 2, so that the expriments of the paper can be reproduced. Spark 3 can be used in the non-anonymized version of GeoDB, that will be publicly available, if the paper is accepted.

In order to run the Data Loader and Query executor, first modify the scope of spark-hive_2.11 dependency in geodb/pom.xml to "provided" in case the spark-hive libraries are available to the execution environment.

Then run: mvn clean install -DskipTests=true

You should use the jar that has been created in geodb/target/it.unibz.inf.obda.geodb-1.16.1-jar-with-dependencies.jar in the spark submit.

For Data Loader:

$SPARK_HOME/bin/spark-submit --class it.unibz.krdb.obda.geofb.DataLoader --executor-memory 116GB --total-executor-cores 31 --master spark://ip:7078 /home/user/it.unibz.inf.obda.geodb-1.16.1-jar-with-dependencies.jar -i hdfs://ip:9001/user/synthetic/all768/ -o synthetic768 -lp TT,VP -dp false -dropDuplicates false -drdb false -tblfrm Parquet -geom false -df dict -outTripleTable triples

where, -i is the input HDFS directory that contains the NTriples files, -o is the HIVE database name which must have been created prior to execution. The rest of the parameters should be left unchanged.

For Query Executor:

$SPARK_HOME/bin/spark-submit --class it.unibz.krdb.obda.geofb.QueryExecutor --executor-memory 116GB --total-executor-cores 32 --master spark://ip:7078 /home/user/it.unibz.inf.obda.geodb-1.16.1-jar-with-dependencies.jar hdfs://ip:9001/user/synthetic/queries768/ synthetic768 hdfs://ip:9001/user/synthetic/asWKTTables.txt true true 384
