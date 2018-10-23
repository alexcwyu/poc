FROM bigdata/hadoop:3_1_1

# https://github.com/gettyimages/docker-spark

# SPARK
ENV SPARK_PACKAGE spark-2.3.2-bin-without-hadoop
ENV SPARK_HOME /opt/spark
ENV SPARK_DIST_CLASSPATH="$HADOOP_HOME/etc/hadoop/*:$HADOOP_HOME/share/hadoop/common/lib/*:$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/hdfs/*:$HADOOP_HOME/share/hadoop/hdfs/lib/*:$HADOOP_HOME/share/hadoop/hdfs/*:$HADOOP_HOME/share/hadoop/yarn/lib/*:$HADOOP_HOME/share/hadoop/yarn/*:$HADOOP_HOME/share/hadoop/mapreduce/lib/*:$HADOOP_HOME/share/hadoop/mapreduce/*:$HADOOP_HOME/share/hadoop/tools/lib/*"
ENV PATH $PATH:${SPARK_HOME}/bin

RUN mkdir -p "$SPARK_HOME" && \
     wget -O- "https://www.apache.org/dyn/mirrors/mirrors.cgi?action=download&filename=spark/spark-2.3.2/spark-2.3.2-bin-without-hadoop.tgz"  \
    | tar xzf - -C $SPARK_HOME --strip-components=1 && \
    echo "export PATH=$SPARK_HOME/bin:\$PATH" > /etc/profile.d/spark.sh && \
    chown -R root:root $SPARK_HOME

WORKDIR $SPARK_HOME
