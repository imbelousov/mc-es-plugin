# Пример плагина для Elasticsearch

Этот репозиторий содержит пример проекта плагина для Elasticsearch 8.10.4 с интеграционными тестами.

### Системные требования
- JDK 18
- docker + docker-compose (опционально)

### Сборка
```console
gradlew build
cd docker
docker build . --tag elasticsearch_myplugin
docker-compose up -d
```
