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

### Ссылки

- JDK https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html
- Gradle https://docs.gradle.org/current/userguide/installation.html#ex-installing-manually
- IntelliJ IDEA Community Edition https://www.jetbrains.com/ru-ru/idea/download/
