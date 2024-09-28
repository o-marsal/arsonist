# Spécifications

Ce document permet de lister les choix possibles, lister les avantages inconvénients, et faire les choix.
L'énoncé étant rédigé en français, cette spécification l'est également.


## Nom du projet

Vu qu'on met le feu à une forêt (virtuellement), j'ai choisi `arsonist` (pyromane en anglais).


## Environnement

### Exécution (runtime)

L'environnement d'exécution n'est pas précisé.
Choix possibles:
- application portable "stand-alone" (ne pas avoir à installer des applications tierces)
- docker
- machine virtuel

=> le backend étant Java, qui est portable, l'approche stand-alone semble à privilégier.
=> pour éviter les problèmes de version de java, créer aussi un conteneur docker d'exécution.


### Compilation (build)

L'environnement de développement n'est pas précisé.
Mais il peut être intéressant que le projet puisse être recompilé, quelque soit la station de travail.

=> le backend étant en java, privilégier maven (ou graddle).
=> le frontend est généralement géré par npm
=> pour gérer maven et npm, le plus simple est d'utiliser un Makefile.
=> pour éviter les problèmes de version des outils (java, maven, npm, make), utiliser un docker de build.


## Technologies

Compte tenu de l'énoncé, la solution la plus simple serait de faire une simple page html, embarquant le css et javascript.
Mais il n'y aurait alors ni backend, ni API REST, ni fichier de configuration, cela serait hors sujet.
> Java pour le backend, JavaScript pour le frontend, et API Rest, avec ou sans framefork

L'utilisation de backend + frontend ici est plutôt "pédagogique".
Ce n'est pas le meilleur choix pour la simplicité (maintenance), performance, coûts...


### Backend

Choix possible:
- Spring Boot: stand-alone, embarque un tomcat
- Wisdom framework: stand-alone, OSGi bassé sur Felix, lui-même basé sur tomcat
- Application web "servlet": nécessite un serveur web déjà installé
- Application java classique, avec une lib serveur serveur http

Privilégier une version stand-alone pour simplifier l'exécution, et la démonstration.
La modularité des composants OSGi n'est pas utile ici, et Sprint Boot est plus populaire que Wisdom.

=> choix de Spring Boot


### Frontend

Choix possible:
- Natif html / css / javascript
- SPA (Single Page Application): Angular / React / Vue...
- Template: Thymeleaf, Mustache...

Au vu de l'exercice demandé, il n'y a pas besoin d'utiliser un framework.
Le choix le plus simple serait de faire du natif html / css / javascript.

Mais l'offre d'emploi étant sur du Angular ou Vue, il peut être intéressant de les utiliser.

=> choix de Angular


## Architecture

Un premier choix est de savoir si le traitement de la simulation se fait côté frontend ou backend.
Le plus simple serait de tout faire côté frontend, ce serait le plus performant, le moins coûteux en requêtes.

Mais l'énoncé précise "API Rest".
L'API pourrait être utilisé:
- pour récupérer les paramètres de la simulation, si elle est côté frontend
- pour récupérer l'état en cours de la simulation, si elle est côté backend

A noter que ce choix implique une différence dans un contexte multi-utilisateurs:
- si la simulation tourne côté backend, plusieurs utilisateurs peuvent voir la même simulation.
- si la simulation tourne côté frontend, chaque utilisateur aura sa propre simulation.

Le choix le plus pédagogique serait de faire la simulation côté backend, pour qu'on ait plusieurs appels web-service.
Ce n'est pas le choix le plus simple, performant, moins couteux.

=> simulation côté backend


Cela signifie que:
- le fichier de configuration sera chargé côté backend
- la simulation étant côté backend, il enverra alors l'état (modèle) au frontend
- le frontend est passif, il se contente d'afficher l'état (modèle) qui reçoit du serveur


### Modèle de données

#### Format

La solution la plus simple et la plus courante est d'utiliser le format json, qui semble bien adapté ici.
Ce n'est pas la plus optimisé, mais semble bien adapté à l'exercice.
Une solution plus optimisé serait d'avoir un autre format plus "compacte" pour minimiser la taille des requêtes.

=> choix du format json


#### Modèle

Informations à transmettre:
- `state` : enum STOPPED | RUNNING, état de la simulation
- `width` : entier, largeur en nombre de cases
- `height`: entier, hauteur en nombre de cases
- `time`  : entier long, timestamp java (ms) de l'état, temps horloge serveur
- `step`  : entier, numéro de l'étape de simulation (utile pour le debug)
- `grid`  : string de taille <width> x <height>, où chaque caractère représente une case.

Une case est représenté par un caractère:
- "T" : case avec un arbre (Tree) sain
- "*" : case avec un arbre en feu
- "X" : case avec un arbre brûlé

Example:
```json
{
    "state" : "RUNNING",
    "width" : 3,
    "height": 3,
    "time"  : 1727345335000,
    "step"  : 1,
    "grid"  : "TTTTX*T*T"
}
```

### Communication

Puisqu'il est demandé d'utiliser des API Rest, cela signifie que la communication se fera par "polling".
A noter que le plus simple et plus efficace aurait été d'ouvrir une web-socket, où le serveur peut pousser directement les données.
Un compromis classique est donc de faire du long polling, pour conserver la réactivité de l'interface.

=> choix du long polling

Il faut aussi prévoir un bouton "démarer la simulation", qui sera aussi un "endpoint" web-service.


#### GET /state?time=<time>

Returne l'état en cours du serveur.
Si le paramètre `time` est donné (optionnel), il s'agit de l'horodate du modèle connu par le client
- si le serveur a un état plus récent, il le retourne directement
- sinon, le serveur attend (long polling) une mise à jour de l'état

Côté client, en cas d'erreur (ex: timeout du long polling), le client refera une requête en attendant au moins 1 seconde.

Exemple:
```
GET /state?time=1727345331000

200 OK
{
    "state" : "RUNNING",
    "width" : 3,
    "height": 3,
    "time"  : 1727345335000,
    "step"  : 1,
    "grid"  : "TTTTX*T*T"
}
```


#### GET /start

Démarre une nouvelle simulation.

Retourne:
"OK" : si la simlation
"ALREADY_RUNNING" : si une simulation est déjà en cours

Exemples:
```
GET  /start

200 OK
OK
```

```
GET  /start

200 OK
ALREADY_RUNNING
```

Note: le `200 OK` ici précise que la requête HTTP s'est bien passé, c'est indépendant du résultat la commande start.
