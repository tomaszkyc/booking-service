
.PHONY: help
.DEFAULT_GOAL:= help



run: ## Runs application
	@docker-compose down
	@docker-compose build
	@docker-compose up -d


test: ## Performs application tests
	@mvn test


database:
	@echo "Stopping database"
	@docker-compose down
	@echo "Trying to start database"
	@docker-compose up db -d
	@echo "Database started correctly"




help: ## Show this help
	@egrep -h '\s##\s' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'