FROM anapsix/alpine-java:8_server-jre_unlimited
# https://github.com/anapsix/docker-alpine-java

# https://github.com/conda/conda-docker/blob/master/miniconda3/centos7/Dockerfile
# Install conda
RUN apk add --no-cache --virtual=.build-dependencies curl wget ca-certificates bash

ENV CONDA_DIR="/opt/conda"
ENV PATH="$CONDA_DIR/bin:$PATH"
RUN mkdir -p "$CONDA_DIR" && \
    wget "http://repo.continuum.io/miniconda/Miniconda3-latest-Linux-x86_64.sh" -O miniconda.sh && \
    bash miniconda.sh -f -b -p "$CONDA_DIR" && \
    echo "export PATH=$CONDA_DIR/bin:\$PATH" > /etc/profile.d/conda.sh && \
    rm miniconda.sh && \
    \
    conda config --add channels conda-forge && \
    conda config --add channels intel && \
    conda config --set auto_update_conda False && \
    conda install  -y -q intelpython3_core=2019.0.0 python=3  && \
    conda update --all --yes && \
    conda clean --all --yes && \
    rm -r "$CONDA_DIR/pkgs/" && \
    \
    apk del --purge .build-dependencies && \
    \
    mkdir -p "$CONDA_DIR/locks" && \
    chmod 777 "$CONDA_DIR/locks"

ENV PYTHONHASHSEED 1

# install scala
ENV SCALA_DIR="/opt/scala"
ENV PATH="$SCALA_DIR/bin:$PATH"
RUN mkdir -p "$SCALA_DIR" && \
    wget -O- "http://downloads.lightbend.com/scala/2.11.12/scala-2.11.12.tgz" \
    | tar xzf - -C $SCALA_DIR --strip-components=1 && \
    echo "export PATH=$SCALA_DIR/bin:\$PATH" > /etc/profile.d/scala.sh