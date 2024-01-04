# ZIO-Streams-Application

Bienvenue dans le projet ZIO-Streams-Application ! Suivez les instructions ci-dessous pour démarrer l'application.

## Prérequis

Assurez-vous que les outils suivants sont installés sur votre machine :

- Docker Desktop
- MySQL

## Étapes pour Démarrer l'Application

```bash
# Clonez le Projet
git clone https://github.com/MaximeWang888/ZIO-Streams-Application.git
cd ZIO-Streams-Application

# Démarrage du Conteneur MySQL avec Docker Compose
docker-compose up -d
# Cela démarrera le conteneur MySQL en utilisant le script `init.sql` pour initialiser la base de données.

#Vérification des Logs du Conteneur (Optionnel) :
#Pour vérifier que l'initialisation s'est bien déroulée, vous pouvez consulter les logs du conteneur MySQL :
docker-compose logs mysql-container

# Exécution de l'Application
# Suivez les instructions spécifiques de votre application pour la construction et l'exécution.
# Cela pourrait impliquer l'utilisation de sbt, Maven, ou tout autre outil de construction que vous utilisez.

# Accéder à l'Application
# Une fois l'application démarrée, accédez-y via le navigateur ou tout autre client d'application.

# Nettoyage ou éteindre la base de donnée
# Après avoir terminé, vous pouvez nettoyer les ressources avec la commande :
docker-compose down
