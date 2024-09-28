FROM alpine:3.20.3

# Install packages
RUN apk update && apk upgrade && apk add bash make openjdk21 maven nodejs npm
RUN npm install -g @angular/cli@17

# Aliases
RUN echo 'alias ll="ls -al"' >> ~/.bashrc

# Prepare folders
WORKDIR /src

# Expose tomcat port 80
EXPOSE 8080
