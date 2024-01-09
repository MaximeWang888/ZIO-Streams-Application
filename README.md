# ZIO-Streams-Application

Bienvenue dans le projet ZIO-Streams-Application ! Suivez les instructions ci-dessous pour démarrer l'application.

## Prérequis

Assurez-vous que les outils suivants sont installés sur votre machine :

- Docker Desktop
- MySQL
- SBT (Simple Build Tool)

## Clonage du Projet
```bash
# Clonez le Projet
git clone https://github.com/MaximeWang888/ZIO-Streams-Application.git
cd ZIO-Streams-Application
```

# Démarrage de la Base de Données MySQL
```bash
# Démarrage du Conteneur MySQL avec Docker Compose
docker-compose up -d
# Cela démarrera le conteneur MySQL en utilisant le script `init.sql` pour initialiser la base de données.

#Vérification des Logs du Conteneur (Optionnel) :
#Pour vérifier que l'initialisation s'est bien déroulée, vous pouvez consulter les logs du conteneur MySQL :
docker-compose logs mysql-container
```

# Construction et Exécution de l'Application
```bash
# Exécution de l'Application
# Suivez les instructions spécifiques de votre application pour la construction et l'exécution.
# Cela pourrait impliquer l'utilisation de sbt, Maven, ou tout autre outil de construction que vous utilisez.

# Lancez l'application avec sbt :
sbt run
```

# Accéder à l'Application
```bash
# Une fois l'application démarrée, accédez-y via le navigateur ou tapez sur une console ou tout autre client d'application.
curl -i "http://localhost:8080/temperature/chicago"
curl -i "http://localhost:8080/temperature/recommendation/london"
curl -i "http://localhost:8080/meteo/rainy"
```

# Nettoyage ou éteindre la base de donnée
```bash
# Après avoir terminé, vous pouvez nettoyer les ressources avec la commande :
docker-compose down
```

-----

# Structure du Projet

Voici la structure des répertoires de ce projet :

```plaintext
├── src
│   ├── main
│   │   ├── scala
│   │   │   ├── controller
│   │   │   │   ├── TemperatureController.scala
│   │   │   │   └── WeatherController.scala
│   │   │   ├── model
│   │   │   │   └── DataModel.scala
│   │   │   ├── service
│   │   │   │   ├── TemperatureService.scala
│   │   │   │   └── WeatherService.scala
│   │   │   └── MainApp.scala
│   │   └── resources
│   │       └── application.conf
├── src
│   └── test
│       └── scala
├── docker
│   └── init.sql
├── resources
│   └── application.conf
├── build.sbt
├── README.md
├── .gitignore
└── docker-compose.yml
```

- src/main/scala/controller: Contient les fichiers des contrôleurs, tels que TemperatureController.scala et WeatherController.scala.
- src/main/scala/model: Contient les fichiers de définition de modèle, par exemple, DataModel.scala.
- src/main/scala/service: Contient les fichiers de services, tels que TemperatureService.scala et WeatherService.scala.
- src/main/scala: Contient le fichier principal de l'application, MainApp.scala.
- src/main/resources: Contient les fichiers de ressources de l'application, par exemple, application.conf.
- src/test/scala: L'emplacement prévu pour les fichiers de test.
- docker: Contient le script SQL d'initialisation (init.sql).
- resources: Contient une autre copie de application.conf.
- build.sbt: Fichier de configuration de construction pour SBT.
- README.md: Fichier de documentation principal.
- .gitignore: Fichier spécifiant les fichiers et répertoires à ignorer lors de la gestion de version avec Git.
- docker-compose.yml: Fichier de configuration Docker Compose.