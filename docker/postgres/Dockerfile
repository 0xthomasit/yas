FROM quay.io/debezium/postgres:17-alpine
ENV  WAL2JSON_TAG=wal2json_2_6
ENV  PGVECTOR_TAG=v0.8.0
RUN    apk add --no-cache --virtual .debezium-build-deps gcc clang19 llvm19 git make musl-dev pkgconf \
         && git clone https://github.com/eulerto/wal2json -b master --single-branch \
         && (cd /wal2json && git checkout tags/$WAL2JSON_TAG -b $WAL2JSON_TAG && make && make install) \
         && (rm -rf wal2json && [ ! -d "wal2json" ] && echo "wal2json removed successfully") \
         && git clone https://github.com/pgvector/pgvector.git -b master --single-branch \
         && (cd /pgvector && git checkout tags/$PGVECTOR_TAG -b $PGVECTOR_TAG && make && make install) \
         && (rm -rf pgvector && [ ! -d "pgvector" ] && echo "pgvector removed successfully") \
         && apk del .debezium-build-deps