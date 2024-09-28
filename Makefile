.PHONY: help
help:
	@echo "TARGETS:"
	@echo "  help     : print this help"
	@echo "  frontend : build the frontend (Angular)"
	@echo "  backend  : build the backend  (Spring)"
	@echo "  run      : run the application"
	@echo "  build    : frontend + backend"
	@echo "  package  : create the .jar"
	@echo "  all      : frontend + backend + run"

.PHONY: frontend
frontend:
	cd angular && ng build --configuration development
	cp -r angular/dist/angular/browser/* src/main/resources/static/

.PHONY: backend
backend:
	mvn install

.PHONY: build
build: frontend backend

.PHONY: package
package: build
	mvn package
	mv target/arsonist-*.jar release/

.PHONY: run
run:
	mvn spring-boot:run

.PHONY: all
all: build run
