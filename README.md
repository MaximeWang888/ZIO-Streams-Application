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

## Diagramme fonctionnel 

<?xml version="1.0" encoding="UTF-8"?>
<!-- Do not edit this file with editors other than draw.io -->
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
![functionnal_schema](https://github.com/MaximeWang888/ZIO-Streams-Application/assets/115694912/66677744-4a5f-4585-81a9-334f8edc6c21)
<svg xmlns="http://www.w3.org/2000/svg" style="background-color: rgb(255, 255, 255);" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" width="703px" height="1464px" viewBox="-0.5 -0.5 703 1464" content="&lt;mxfile host=&quot;app.diagrams.net&quot; modified=&quot;2024-01-19T22:51:56.954Z&quot; agent=&quot;Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:121.0) Gecko/20100101 Firefox/121.0&quot; etag=&quot;KIRq3uDB_aSTyKKAEC06&quot; version=&quot;22.1.17&quot; type=&quot;google&quot;&gt;&lt;diagram name=&quot;Page-1&quot; id=&quot;84yV7SaUmvy2Uh00cwKO&quot;&gt;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&lt;/diagram&gt;&lt;/mxfile&gt;"><defs/><g><rect x="16" y="608" width="80" height="80" fill="rgb(255, 255, 255)" stroke="rgb(0, 0, 0)" pointer-events="all"/><g transform="translate(-0.5 -0.5)"><switch><foreignObject style="overflow: visible; text-align: left;" pointer-events="none" width="100%" height="100%" requiredFeatures="http://www.w3.org/TR/SVG11/feature#Extensibility"><div xmlns="http://www.w3.org/1999/xhtml" style="display: flex; align-items: unsafe center; justify-content: unsafe center; width: 78px; height: 1px; padding-top: 648px; margin-left: 17px;"><div style="box-sizing: border-box; font-size: 0px; text-align: center;" data-drawio-colors="color: rgb(0, 0, 0); "><div style="display: inline-block; font-size: 12px; font-family: Helvetica; color: rgb(0, 0, 0); line-height: 1.2; pointer-events: all; white-space: normal; overflow-wrap: normal;">
