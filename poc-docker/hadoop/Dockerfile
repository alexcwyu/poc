FROM bigdata/base:jdk8_scala11

# https://github.com/gettyimages/docker-spark

# HADOOP
ENV HADOOP_VERSION 3.1.1
ENV HADOOP_HOME="/opt/hadoop"
ENV HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
ENV PATH $PATH:$HADOOP_HOME/bin

RUN mkdir -p "$HADOOP_HOME" && \
    wget -O- "http://archive.apache.org/dist/hadoop/common/hadoop-3.1.1/hadoop-3.1.1.tar.gz" \
    | tar xzf - -C $HADOOP_HOME --strip-components=1 && \
    echo "export PATH=$HADOOP_HOME/bin:\$PATH" > /etc/profile.d/hadoop.sh && \
    rm -rf $HADOOP_HOME/share/doc && \
    chown -R root:root $HADOOP_HOME