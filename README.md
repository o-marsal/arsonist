# Project
This project is an technical exercise test.  
See:
- the exercice instruction in [doc/test_technique.md](https://github.com/o-marsal/arsonist/blob/master/doc/test_technique.md)
- my specification in [doc/specification.md](https://github.com/o-marsal/arsonist/blob/master/doc/specification.md)

# Quick Execution

1. Download the jar file [release/arsonist-0.0.1.jar](https://github.com/o-marsal/arsonist/blob/master/release/arsonist-0.0.1.jar)

2. Execute the jar (requires java 17)
```
java -jar arsonist-0.0.1.jar
```

3. Open your browser at [http://localhost:8080](http://localhost:8080)


# Compilation

## Requirements

A docker container is provided, for build and run.  
Docker is the easiest way, the container contains all the requirements.  
Otherwise, you have to configure all the compilation tools in your workstation.


### Without docker

- Java : 17 (used 21 in the docker container)
- Maven: 3.6.3 (used 3.9.6 in the docker container)
- Npm  : (used 10.8.0 in the docker container)
- Make : (used 4.4.1 in the docker container)


### With docker

Use the scripts:
```sh
./docker-init.sh  # build the docker container
./docker-bash.sh  # run the docker container, in a bash shell
```

This works also in Windows, with WSL.


## Commands

```sh
# Build
make build

# Run
make run

# To see all the targets
make help
```

Then open a browser:
[http://localhost:8080](http://localhost:8080)
