clean:
	./gradlew clean

setup:
	./gradlew wrapper --gradle-version 8.6
	./gradlew build

build:
	./gradlew clean build

start:
	./gradlew bootRun

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew installDist

start-dist:
	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

reload-classes:
	./gradlew -t classes
report:
	./gradlew jacocoTestReport

