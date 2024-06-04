.PHONY: help build run test lint format

help: ## Show this help message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

build: ## Build the project
	./gradlew build

run: ## Run the project locally
	./gradlew run

test: format lint ## Run the tests
	./gradlew test

format: ## Run linting and formatting
	./gradlew ktfmtFormat || ./gradlew ktfmtCheck