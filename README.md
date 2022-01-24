# AMT - Projet de semestre

## Description

Le but de ce projet de semestre de la HEIG-VD est de permettre aux étudiants
d'aquérir les connaissances pour :

- travailler avec J2EE en appliquant le patron de conception MVC,
- intégrer un template pour faire un site d'e-commerce,
- se connecter à une base de données.

Le projet est guidé via le gitbook de notre professeur Nicolas Glassey:
<https://nicolas-glassey.gitbook.io/amt-backlog/>

## Installation

* Installez [Java 11](https://openjdk.java.net/install/).

* Installez [IntellJ IDEA 2021.2.2](https://www.jetbrains.com/idea/) avec les paramètres par défaut.

* Installez [PostgreSQL 14.1](https://www.postgresql.org/) avec les paramètres par défaut.

> Si votre installation de postgreSQL n'installe pas [pgAdmin 4 (interface graphique de postgreSQL)](https://www.pgadmin.org/download/), installez le manuellement

* Installez [Docker](https://docs.docker.com/engine/install/) (sous windows 4.2.0) avec les paramètres par défaut.

* Clonez le [repository](https://github.com/QuentinForestier/AMT-Semester-Project/tree/main).

* Lancez pgAdmin.

* Créez deux nouveaux utilisateurs (un pour la DB de l'application et un pour la DB du service de login/authentification): 

> Ceci est un exemple. il faudra s'en souvenir pour les fichiers de configuration du projet.

![utilisateur nom](https://user-images.githubusercontent.com/34660483/150751710-8a095bdf-5e78-4c36-9714-7f253408e01d.png)

![utilisateur password](https://user-images.githubusercontent.com/34660483/150751233-ed2e4096-9d4a-4cc4-b7b9-d8e5bf312e2e.png)

![utilisateur autorisations](https://user-images.githubusercontent.com/34660483/150751398-458e05bd-0ca0-4515-898a-b659e612327c.png)

> Les permissions peuvent être affinées, mais par simplification nous avons autorisé le maximum.

* Créez deux nouvelles base de données: avec le nom "crossport" et avec le nom "crossportLogin".

![image](https://user-images.githubusercontent.com/61196626/136580523-6dc9aebd-26fa-4706-9b22-603eda280234.png)

Résultat partiel avec un utilisateur et une base de données:

![Résultat final](https://user-images.githubusercontent.com/34660483/150751903-19af7b9d-dfb2-4b6c-9e33-4259dc407b3b.png)

* Lancez IntellJ et ouvrez le dossier "crossport" du projet cloné.

* Créez le fichier crossport/src/main/resources/application.properties et insérez le contenu suivant:

> les parties entre '<' '>' doivent être complétées avec vos valeurs

```
spring.jpa.hibernate.ddl-auto=create
spring.datasource.url=jdbc:postgresql://localhost:5432/crossport
spring.datasource.username=<Nom de l'utilisateur de base de données de l'app (dans notre cas crossport)>
spring.datasource.password=<Mot de passe de l'utilisateur de base de données de l'app>

server.port=8080

# Pour pouvoir faire des requetes DELETE, PUT
spring.mvc.hiddenmethod.filter.enabled=true

com.webapplication.crossport.config.jwt.secret=<Secret de génération des tokens JWT>

# S3 for AWS
com.webapplication.crossport.config.aws.access=<AWS S3 access key ID>
com.webapplication.crossport.config.aws.secret=<AWS S3 access key secret>
com.webapplication.crossport.config.aws.region=<AWS S3 bucket region>
com.webapplication.crossport.config.aws.bucket=<AWS S3 bucket name>
``` 

> Pour la première fois, afin de générer les tables, spring.jpa.hibernate.ddl-auto doit valoir <b>create</b> plus tard la valeur peut-être <b>update</b>.

> L'application utilise un [bucket AWS S3](https://aws.amazon.com/fr/s3/) pour le stockage des images.

![image](https://user-images.githubusercontent.com/61196626/136580863-9972b7d7-c1f6-42b4-af5d-eee507b1d311.png)

* Créez le fichier crossport/src/test/resources/application.properties et insérez le contenu suivant:

> les parties entre '<' '>' doivent être complétées avec vos valeurs

```
spring.datasource.url = jdbc:h2:mem:test
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect

server.port=8080

# Pour pouvoir faire des requetes DELETE, PUT
spring.mvc.hiddenmethod.filter.enabled=true

com.webapplication.crossport.config.jwt.secret=<Secret de génération des tokens JWT>

# S3 for AWS
com.webapplication.crossport.config.aws.access=<AWS S3 access key ID>
com.webapplication.crossport.config.aws.secret=<AWS S3 access key secret>
com.webapplication.crossport.config.aws.region=<AWS S3 bucket region>
com.webapplication.crossport.config.aws.bucket=<AWS S3 bucket name>
``` 

> Les fichiers applications.properties sont créés dynamiquement par les git actions.

* Lancez IntellJ et ouvrez le dossier "loginService" du projet cloné.

* Créez le fichier loginService/src/src/main/resources/application.properties et insérez le contenu suivant:

> les parties entre '<' '>' doivent être complétées avec vos valeurs

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:postgresql://db:5432/crossportLogin
spring.datasource.username=<Nom de l'utilisateur de base de données du service de login/auth (dans notre cas crossportLogin)>
spring.datasource.password=<Mot de passe de l'utilisateur de base de données du service de login/auth>

com.webapplication.loginCrossport.config.jwt.secret=<Secret de génération des tokens JWT (le même que du côté applicatif)>
com.webapplication.loginCrossport.config.admin.username=<Nom du compte admin pour le site web>
com.webapplication.loginCrossport.config.admin.password=<Mot de passe du compte admin pour le site web>

server.port=8081
``` 

## Utilisation

<b> Prérequis 1: Avoir effectué la partie "Installation".</b>
<b> Prérequis 2: Avoir votre instance de postegreSQL démarrée.</b>

#### Service de login et authentification

* Lancer le logiciel IntellJ, ouvrir le dossier "loginService".

* Lancer le projet grâce à l'icône start (triangle vert).

#### Application 

* Lancer le logiciel IntellJ, ouvrir le dossier "crossport".

* Lancer le projet grâce à l'icône start (triangle vert).

* Lancer un navigateur et accéder à l'application grâce à l'adresse suivante:
localhost:8080

### Tests

Pour les tests, vous devez avoir une instance docker en cours d'exécution. Les tests lancent automatiquement un container à partir du mocking du service d'authentification.

De plus, il est nécessaire que votre service Postgres, avec la base de données, tourne localement. Nous n'utilisons pas la base de données pour les tests mais le context de l'application doit quand de même de s'y connecter.

pour lancer les tests, exécutez la commande  "mvn -B package --file crossport/pom.xml" ou directement depuis intelliJ:

![tests](https://user-images.githubusercontent.com/34660483/150760822-9b36b44a-4e30-4cb1-91d8-bfed409bdad1.png)

## Support

En cas de problème contacter nous grâce à l'adresse email suivante:
alec.berney@heig-vd.ch

Si votre demande fait allusion à une nouvelle fonctionnalité, une erreur dans le code, un bug ou tout autre demande relative au code, il est toujours possible de contribuer au projet ou ouvrir une nouvelle issue.

## Contribuer

Avant de contribuer au projet, veuillez prendre connaissance des points suivants:

- [GitFlow](https://github.com/Quillasp/AMT-Semester-Project/wiki/Workflow-git)
- [Conventions de nommage](https://github.com/Quillasp/AMT-Semester-Project/wiki/Conventions-de-nommage)

Veuillez ensuite installer l'environement de développement complet comme indiqué plus haut dans le document.
Une fois l'environnement installé, vous pouvez contribuer au projet en:

1. Réalisant un fork du projet.
2. Développant votre ajout / fonctionnalité.
3. Ouvrant une issue..
4. Réalisant une pull request une fois que tous les tests fonctionnent (relativement à l'issue créée).

## Auteurs

Nous sommes une équipe de 5 étudiants de la HEIG-VD en informatique logiciel :

- Alec Berney (alecberney)
- Quentin Forestier (QuentinForestier)
- Melvyn Herzig (MelvynHerzig)
- Florian Gazetta (fg1989)
- Soulaymane Lamrani (quillasp)

## License

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

## Status du projet

Le projet est en cours de développement et le sera tout au long du semestre
d'automne.
