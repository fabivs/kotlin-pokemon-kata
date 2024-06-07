.PHONY: help build run test lint format

help: ## Show this help message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

build-docker: ## Build the docker image for the project
	docker build -t pokemon-application .

run-docker: ## Run the docker image for the project
	docker run -p 8080:8080 pokemon-application

build: ## Build the project
	./gradlew build

run: ## Run the project locally
	./gradlew run

test: format ## Run the tests
	./gradlew test

format: ## Run linting and formatting
	./gradlew ktfmtFormat || ./gradlew ktfmtCheck
