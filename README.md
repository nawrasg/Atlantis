#Atlantis
Version 0.1  
GPL v3 - Nawras GEORGI 2015  
***


Atlantis est un système domotique libre et gratuit sous licence GPL 3.


##Caractéristiques
* Météo
* Liste des courses
* Contenu Cuisine/Pharmacie/Produit d'hygiène et d'entretien avec quantité et date de péremption
* Gestion des lumières (Philips Hue)
* Gestion des capteurs Zwave
* Gestion des appareils connectés (ordinateurs, smartphones, tablettes, etc.)
* Lecteur de musique
* Musique de bienvenue : lorsque vous rentrez chez vous, votre morceau préféré est joué

##Demo
* Application web : <http://atlantis.nawrasg.fr/>
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