## Requirements

#### Application
- Java 11
- Maven

#### Database
- MySQL (8.0+) 

## Start locally
```
mvn clean install
java <env variables: -DvariableName='value'> -jar <jar location>
```
## Start locally for development
```
mvn compile
mvn <env variables: -DvariableName='value'> spring-boot:run
```

## Environment variables:

- DB_PASSWORD (_default_: 'root') - _db password_
- DB_URL (_default_: 'jdbc:mysql://localhost:3306') - _db url_
- DB_USERNAME (_default_: 'root') - _db username_
- ADMIN_DEFAULT_USERNAME (_default_: 'admin') - _default admin username_
- ADMIN_DEFAULT_PASSWORD (_default_: 'admin') - _default admin password_
- TELEGRAM_BOT_USERNAME (_default_: empty) - _telegram bot username_
- TELEGRAM_BOT_TOKEN (_default_: empty) - _telegram bot token_
- TELEGRAM_BOT_URL_SECRET (_default_: empty) - _telegram bot webhook url secret_
- TELEGRAM_BOT_WEBHOOK_URL (_default_: empty) - _application base path for telegram webhook_
- APP_BASE_PATH (_default_: 'http://localhost:8080') - _application base path_
- SPRING_PROFILES_ACTIVE - _active Spring profiles_

## Spring profiles:

- production - _run application for production usage (Telegram bot is available)_
- default - _run application for local dev (Telegram bot is unavailable, no related env variables are required)_
- test - _integration tests_

## Docker

#### Local development:
```
mvn clean install
docker-compose -f docker-compose-dev.yml up
```

#### Start standalone instance:
```
mvn clean install
docker-compose -f docker-compose-prod.yml up
```