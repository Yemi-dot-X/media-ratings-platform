# Media Ratings Platform (MRP)

Java REST API mit Java HttpServer, PostgreSQL, JDBC und JWT. Zwischenabgabe.

Architektur, Endpunkte, Statuscodes, Tests und Projektumfang: siehe [DOCUMENTATION.md](DOCUMENTATION.md).

## Voraussetzungen

- Java 21
- Maven
- Docker Desktop mit Docker Compose
- Postman

## Programm starten

1. Datenbank starten (im Projektordner, in dem `docker-compose.yml` liegt):

```bash
docker compose up -d
```

2. Projekt bauen (im Projektordner, in dem `pom.xml` liegt):

```bash
mvn clean package
```

3. Anwendung starten: Klasse `at.fhtw.mrp.presentation.Main` ausführen
   (`src/main/java/at/fhtw/mrp/presentation/Main.java`, in IntelliJ auf den grünen Run-Button bei `main()` klicken).

Der Server läuft danach unter `http://localhost:8080`.

4. Tests ausführen (die Datenbank muss laufen):

```bash
mvn test
```

## Datenbank-Konfiguration

Lokale Entwicklungsumgebung, nur für Test und Abgabe.

| Einstellung | Wert |
|---|---|
| Host | localhost |
| Port | 5432 |
| Datenbank | postgres |
| Benutzer | postgres |
| Passwort | mysecretpassword |

Das Schema (`src/main/resources/schema.sql`) wird beim ersten Start mit leerem Datenbank-Volume automatisch angelegt.
Für eine frische Datenbank: `docker compose down -v` und danach `docker compose up -d`.

## Postman

1. Die mitgelieferte Postman Collection importieren.
2. Einen Benutzer über `POST /api/users/register` anlegen.
3. `POST /api/users/login` ausführen.
4. Der Token wird vom Script im Tab "Tests" der Login-Anfrage automatisch gespeichert:

```javascript
const response = pm.response.json();
pm.collectionVariables.set("token", response.token);
```

5. Bei geschützten Anfragen unter Authorization den Typ "Bearer Token" wählen und `{{token}}` eintragen.