# TP 0 - Introduction à Jakarta EE (Aya Tijini)

Application Web de type « chatbot » très simple, pour une première prise de contact avec **Maven**, **Git/GitHub**,
**CDI** et **JSF**. Le serveur n'appelle pas de LLM : il applique un traitement local sur la question de l'utilisateur
(voir « Traitement personnel » plus bas).

## Versions utilisées

| Élément | Version |
|---|---|
| Java (JDK Temurin) | 25 |
| Jakarta EE (Web Profile) | 11 |
| Serveur d'application | Payara 7.2026.10 |
| PrimeFaces | 16.0.1 |
| Maven | 3.9.x |

## Lancer l'application

1. Construire le WAR : `mvn clean package` (génère `target/tp0AyaTijini-1.0-SNAPSHOT.war`).
2. Déployer le WAR sur Payara, par exemple avec IntelliJ IDEA (configuration « Payara Server > Local »)
   ou avec `asadmin deploy target/tp0AyaTijini-1.0-SNAPSHOT.war`.
3. Ouvrir http://localhost:8080/tp0AyaTijini-1.0-SNAPSHOT/ (la page `index.xhtml` est la page d'accueil).
   Le servlet de démonstration est sur http://localhost:8080/tp0AyaTijini-1.0-SNAPSHOT/hello-servlet.

## Organisation du code

| Fichier | Rôle |
|---|---|
| `src/main/webapp/index.xhtml` | Page JSF : rôle de l'API, question, réponse et conversation |
| `src/main/webapp/resources/` | Fichiers CSS et JavaScript utilisés par la page |
| `src/main/java/ma/emsi/ayatijini/tp0/jsf/Bb.java` | Backing bean (`@ViewScoped`) : garde l'état de la conversation |
| `src/main/java/ma/emsi/ayatijini/tp0/service/GenerateurReponse.java` | Service CDI (`@Dependent`) qui construit la réponse |
| `src/main/java/ma/emsi/ayatijini/tp0/HelloServlet.java` | Servlet de démonstration (message modifié) |
| `src/main/webapp/WEB-INF/web.xml` | Page d'accueil `index.xhtml` |

## Traitement personnel (exercice pour le bonus)

La classe `GenerateurReponse` renvoie un « miroir » de la question :

- la question lue à l'envers, lettre par lettre (donc les mots sont aussi dans l'ordre inverse) ;
- une ligne de statistiques : nombre de mots, de lettres et de voyelles (avec accents) ;
- pour la première question seulement, les 4 premiers mots du rôle de l'API entre crochets.

Exemple : `Salut Casablanca été` donne

```
été acnalbasaC tulaS
(3 mot(s), 18 lettre(s), 8 voyelle(s))
```

## Message d'erreur quand la question est vide

Si on envoie le formulaire avec une question vide, la page affiche en rouge « Il manque le texte de la question » :

- `Bb.envoyer()` détecte la question vide, crée un `FacesMessage` d'erreur, l'ajoute au `FacesContext`
  (`addMessage(null, ...)`, message global) et retourne `null` pour rester sur la même page ;
- `index.xhtml` contient `<h:messages ... globalOnly="true" showDetail="true" showSummary="false"/>` qui affiche
  ce message.

Si aucun rôle n'est indiqué, c'est un autre message (« Vous devez indiquer le rôle de l'API ») : il vient de la
validation JSF (`required="true"`) de la liste déroulante, avant l'appel de `envoyer()`.

## Écarts par rapport à l'énoncé

- `pom.xml` : PrimeFaces 16.0.x n'a pas d'artefact avec le classifier `jakarta` sur Maven Central ;
  le classifier est donc retiré.
- La classe du service s'appelle `GenerateurReponse` (nom utilisé par le backing bean), alors que la page de
  correction l'appelle « Modificateur ».
- Ajout d'un fichier `WEB-INF/faces-config.xml` (vide, version 4.1).
