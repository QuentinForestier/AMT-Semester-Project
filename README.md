# AMT - Projet de semestre

## Description

Le but de ce projet de semestre de la HEIG-VD est de permettre aux étudiants
d'aquérir les connaissances pour :

- travailler avec J2EE en appliquant le patron de conception MVC,
- intégrer un template pour faire un site d'e-commerce,
- se connecter à une base de données.

Le projet est guidé via le gitbook de notre professeur Nicolas Glassey:
https://nicolas-glassey.gitbook.io/amt-backlog/

# Installation

Installez Java 11:
https://openjdk.java.net/install/

Installez IntellJ IDEA 2021.2.2 avec les paramètres par défaut:
https://www.jetbrains.com/idea/

Installez PostgreSQL 14.1 avec les paramètres par défaut:
https://www.postgresql.org/

Installer Docker (sous windows 4.2.0) avec les paramètres par défaut:
https://docs.docker.com/engine/install/
> Docker est utile uniquement pour les tests.

Clonez le repository.

Lancez pgAdmin (interface graphique de PostgreSQL).<br/>
Créez une nouvelle Base de données avec le nom "crossport".<br/>
![image](https://user-images.githubusercontent.com/61196626/136580523-6dc9aebd-26fa-4706-9b22-603eda280234.png)

Voici le résultat:<br/>
![image](https://user-images.githubusercontent.com/61196626/136581154-1049602a-261b-496f-b4e7-4b10c45b0130.png)

Lancez IntellJ, ouvrez le dossier "crossport" du projet fraichement clone et allez sous le fichier application.properties.<br/>
![image](https://user-images.githubusercontent.com/61196626/136580863-9972b7d7-c1f6-42b4-af5d-eee507b1d311.png)

Remplissez le champ password avec le mot de passe que vous avez défini pour l'utilisateur postgre (lors de l'installation de postgre).<br/>
![image](https://user-images.githubusercontent.com/61196626/136581346-32020bd2-91b6-45fb-ad2b-3d1444851dcc.png)

Exécuter le projet.

Les tables de la base de données devraient être créées grâce au framework Hibernate.

Le projet est maintenant mis en place.

L'application est à son début, cette partie pourrait changer rapidement.

# Utilisation

Une fois la partie "Installation" réalisée, il vous suffit de lancer le logiciel IntellJ, puis d'ouvrir le dossier "crossport" contenu dans le projet que vous venez de clone ou fork:<br/>
![image](https://user-images.githubusercontent.com/61196626/137506386-579bbb42-76b1-4c77-b055-9d4a11b860fa.png)

Lancer le projet grâce à l'icône start (triangle vert).
> Le projet utilise un micro service comme service d'authentification. Par manque de temps, le service de production est utilisé pour le développement, du moins temporairement. \
> Le serveur d'authentification est disponible tous les jours entre 7h et 23h.
> Pour l'utiliser lancer les deux commandes suivantes:
>
>`$  ssh -L 23:10.0.1.12:22 CROSSPORT@16.170.194.237 -i "<path>/<to>/<dmz key>.pem"`
>
>`$ ssh -L 22192:10.0.1.92:22 -L 8081:10.0.1.92:8080  CROSSPORT@16.170.194.237 -i "<path>/<to>/<dmz key>.pem"`

Finalement, il vous suffit de lancer votre navigateur préféré et accéder à l'application grâce à l'adresse suivante:
localhost:8080

L'application n'est pas encore disponible sur internet et cette partie propose une façon d'accéder / tester l'application d'un façon provisoire.

## Tests
Pour les tests, vous devez avoir une instance docker en cours d'exécution. Les tests lancent automatiquement un container à partir du mocking du service d'authentification.

# Support

En cas de problème contacter nous grâce à l'adresse email suivante:
alec.berney@heig-vd.ch

Si votre demande fait allusion à une nouvelle fonctionnalité, une erreur dans le code, un bug ou tout autre demande relative au code, il est toujours possible de contribuer au projet ou ouvrir une nouvelle issue.

# Contribuer

[En cours d'élaboration]

Avant de contribuer au projet, veuillez prendre connaissance des points suivants:

* [GitFlow](https://github.com/Quillasp/AMT-Semester-Project/wiki/Workflow-git)
* [Conventions de nommage](https://github.com/Quillasp/AMT-Semester-Project/wiki/Conventions-de-nommage)

Veuillez ensuite installer l'environement de développement complet comme indiqué plus haut dans le document.
Une fois l'environnement installé, vous pouvez contribuer au projet en:

1. Réalisant un fork du projet au niveau de la branche main.
2. Ouvrant une issue sur votre repository fraîchement cloné.
3. Développant votre ajout / fonctionnalité.
4. Réalisant une pull request une fois que tous les tests fonctionnent.

# Auteurs

Nous sommes une équipe de 5 étudiants de la HEIG-VD en informatique logiciel :

- Alec Berney (alecberney)
- Quentin Forestier (QuentinForestier)
- Melvyn Herzig (MelvynHerzig)
- Florian Gazetta (fg1989)
- Soulaymane Lamrani (quillasp)

# License

MIT License

Copyright (c) 2021 Crossport

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

# Status du projet

Le projet est en cours de développement et le sera tout au long du semestre
d'automne.
