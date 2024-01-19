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
curl -i "http://localhost:8080/temperature/recommendation/chicago"
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
│   │   │   │   ├── DataModel.scala
│   │   │   │   ├── DataModelDao.scala
│   │   │   │   └── DatabaseManager.scala
│   │   │   ├── service
│   │   │   │   ├── TemperatureService.scala
│   │   │   │   ├── TemperatureSteam.scala
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
De plus, il y a les fichiers DatabaseManager et DataModelDao pour gérer l'accès aux données et la gestion de la base de données.
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

# Mon Application Scala avec ZIO Stream

Mon application Scala suit une architecture MVC (Modèle-Vue-Contrôleur), ce qui facilite l'organisation et la maintenance du code.  L'appli est conçue pour récupérer périodiquement les données de température actuelle d'une API météo et les stocker dans une base de données. Voici un aperçu des composants clés de l'application :

## TemperatureStream

Le fichier `TemperatureStream.scala` dans le package `service` contient la logique de streaming pour récupérer les données de température à partir de l'API OpenWeatherMap. Voici ce qu'il fait :

- **`extractTemperature` :** Cette fonction analyse la réponse de l'API pour extraire la température actuelle.
- **`fetchData` :** Cette fonction effectue la requête HTTP pour obtenir les données météorologiques de l'API.
- **`appLogic` :** Cette ZIO Stream effectue une requête périodique à l'API, extrait la température de la réponse, et l'insère dans la base de données à l'aide de `dataModelDao`.

### Note

Nous avons actuellement implémenté la ZStream pour récupérer la température de manière périodique uniquement pour la ville de Chicago. Cela signifie que si vous souhaitez obtenir la température en temps réel d'une autre ville, cela ne sera possible qu'avec Chicago pour le moment. Cependant, il est tout à fait réalisable d'étendre cette fonctionnalité en ajoutant d'autres ZStream qui récupèreront la température pour différentes coordonnées correspondant aux différentes villes que vous souhaitez inclure.

Il est important de noter que la fréquence de récupération est actuellement définie à une périodicité de 5 minutes. Cette fréquence a été choisie en tenant compte du fait que la température d'une ville ne change généralement pas de manière significative en une courte période, permettant ainsi une mise à jour régulière sans surcharger inutilement les requêtes vers l'API météo.

En résumé, pour chaque ville que vous souhaitez inclure dans votre application, vous pouvez ajouter un nouveau ZStream spécifique à cette ville pour récupérer sa température actuelle de manière périodique.

## MainApp

Le fichier `MainApp.scala` agit comme le point d'entrée principal de l'application. Il combine le streaming de température avec un serveur HTTP pour exposer les fonctionnalités de l'application via des endpoints. Voici un aperçu des composants clés :

- **`server` :** Cette ZIO effectue le lancement du serveur HTTP qui expose les fonctionnalités de l'application via des endpoints.
- **`backgroundProcess` :** Cette ZIO exécute le streaming de température en arrière-plan tout en servant le serveur HTTP. Cela garantit que les données de température sont constamment mises à jour dans la base de données.

Le controlleur (`Controller`) est représenté par les classes dans le package `controller`, telles que `TemperatureController` et `WeatherController`. Ces classes écoutent sur les différentes routes et redirige les requêtes.

Le modèle (`Model`) est représenté par les classes dans le package `model`, telles que `Coordinates` et `DataModelDao`. Ces classes définissent la structure des données et la logique d'accès à la base de données.

La vue (`View`) n'est pas explicitement mentionnée dans ce contexte car elle peut prendre différentes formes selon les besoins. Dans notre cas, elle pourrait être une interface utilisateur (UI) qui consomme les données de l'API via les endpoints exposés par le serveur.
