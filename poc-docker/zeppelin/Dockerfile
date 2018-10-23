FROM bigdata/spark:2_3_2

# https://github.com/gettyimages/docker-spark
#https://github.com/big-data-europe/docker-zeppelin/blob/master/docker-compose.yml

ENV APACHE_SPARK_VERSION 2.3.2
ENV APACHE_HADOOP_VERSION 3.1.1
ENV ZEPPELIN_VERSION 0.8.0
ENV ZEPPELIN_HOME /opt/zeppelin


# http://apache.01link.hk/zeppelin/zeppelin-0.8.0/zeppelin-0.8.0-bin-all.tgz 

RUN mkdir -p "$ZEPPELIN_HOME" && \
     wget -O- "http://ftp.cuhk.edu.hk/pub/packages/apache.org/zeppelin/zeppelin-0.8.0/zeppelin-0.8.0-bin-all.tgz"  \
    | tar xzf - -C $ZEPPELIN_HOME --strip-components=1 && \
    echo "export PATH=$ZEPPELIN_HOME/bin:\$PATH" > /etc/profile.d/zeppelin.sh && \
    chown -R root:root $ZEPPELIN_HOME

WORKDIR $ZEPPELIN_HOME
