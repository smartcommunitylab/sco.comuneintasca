# sco.comuneintasca
Comune in Tasca: Content integration from comune di Trento.


## Build/Run Java

mvn clean package -Dmaven.test.skip=true

## Docker

Build image
``
docker build -t smartcommunitylab/comuneintasca Dockerfile .
``

Run image
``
docker run -p 8080:8080 smartcommunitylab/comuneintasca
``

Docker compose 
``
docker-compose up
``

## Variables
Set environment variables to run the applications. Apart from standard Spring properties, the app may require (if not from docker compose)
- `DATASOURCE_URL` Mongo URL for connection
- `GOOGLE_APIS_APIKEY` Google API Key to connect to the Google service for Spreadsheet reading
- `COUCHDB_HOST` Couch DB host
- `COUCHDB_PORT` Couch DB port
- `COUCHDB_DBNAME` Name of the Couch DB to store
- `COUCHDB_USER` Couch DB username
- `COUCHDB_PASSWORD` Couch DB password
- `SETUP_FILE` URL Reference to the setup file