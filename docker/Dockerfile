FROM elasticsearch:8.10.4
COPY myplugin.zip /data/es-plugins/
RUN bin/elasticsearch-plugin install file:///data/es-plugins/myplugin.zip