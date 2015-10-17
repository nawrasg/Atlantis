#Atlantis
Version 0.2  
GPL v4 - Nawras GEORGI - Septembre 2015  
***


Atlantis est un système domotique libre et gratuit sous licence GPL 3.


##Caractéristiques
* Météo
* Alarme
* Liste des courses
* Contenu Cuisine/Pharmacie/Produit d'hygiène et d'entretien avec quantité et date de péremption
* Gestion des lumières (Philips Hue)
* Gestion des capteurs Zwave
* Création de scénarios (exemple : allumer la lumière pendant 5 secondes si un mouvement est détecté)
* Gestion des plantes
* Gestion des appareils connectés (ordinateurs, smartphones, tablettes, etc.)
* Gestion des caméras de surveillance
* Lecteur de musique
* Musique de bienvenue : lorsque vous rentrez chez vous, votre morceau préféré est joué
* Géolocalisation des appareils utilisant Atlantis

##Demo
* Application web (utiliser un quelconque identifiant) : <http://atlantis.nawrasg.fr/>
* Application Android : <https://play.google.com/store/apps/details?id=fr.nawrasg.atlantis>

##Prérequis
* Apache LAMP (apt-get install apache2 php5 mysql-server phpmyadmin php5-curl)
* Music On Console (apt-get install moc)
* mpg321 (apt-get install mpg321)

##Installation
Naviguez dans le répertoire web de votre serveur puis exécutez la commande suivante en tant d'administrateur :

    wget https://raw.githubusercontent.com/nawrasg/Atlantis/master/install.sh && bash install.sh

##Licences
* [Application Android](Licenses/android.md)
* [AngularJS](Licenses/angular.md)