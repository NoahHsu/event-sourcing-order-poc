# build docker
buildDockerImage:
	./gradlew jibDockerBuild

buildImageTar:
	./gradlew jibBuildTar

# kafka dependency
kafka-up:
	docker-compose -f Docker/docker-compose.yml -p event-sourcing up -d

kafka-pause:
	docker-compose -f Docker/docker-compose.yml -p event-sourcing pause

kafka-unpause:
	docker-compose -f Docker/docker-compose.yml -p event-sourcing unpause

kafka-resume: kafka-unpause

kafka-down:
	docker-compose -f Docker/docker-compose.yml -p event-sourcing down