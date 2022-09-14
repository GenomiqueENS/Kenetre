#!/bin/bash

# This file contains the Docker instruction used to build the STAR executable 
# for Eoulsan

VERSION=2.4.0k
DOCKER_IMAGE="star:$VERSION"

cat > Dockerfile << EOF
############################################################
# Dockerfile to build star container images
# Based on Ubuntu
############################################################

# Set the base image to Ubuntu
FROM ubuntu:12.04

# File Author / Maintainer
MAINTAINER Sophie Lemoine <slemoine@biologie.ens.fr>

# Install compiler and dependencies
RUN apt-get update && apt-get install --yes build-essential gcc-multilib apt-utils zlib1g-dev vim-common git

# Install STAR
WORKDIR /tmp
RUN cd /tmp && \
    git clone https://github.com/alexdobin/STAR.git && \
    cd STAR/source && \
    git checkout STAR_$VERSION && \
    make clean STARstatic && \
    mv STARstatic /usr/local/bin/STAR && \
    make clean STARlong && \
    mv STAR /usr/local/bin/STARlong

# Cleanup
RUN rm -rf /tmp/STAR && apt-get clean && apt-get remove --yes --purge build-essential gcc-multilib apt-utils zlib1g-dev vim-common git
EOF

docker build --no-cache -t $DOCKER_IMAGE .

if [[ $? -eq 0 ]]; then
  docker run --rm -v `pwd`:/root -u `id -u`:`id -g` $DOCKER_IMAGE cp /usr/local/bin/STARstatic /usr/local/bin/STARlong /root
  docker rmi -f $DOCKER_IMAGE
  rm Dockerfile
fi
