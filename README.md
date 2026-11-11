## Instalation Guide(partialy obsolet)
- Download node.js LTS
- run comands:

npm create vite@latest frontend -- --template react

cd frontend

npm install

npm run dev

- ladda ner docker

nu ska vi fixa db med docker
- run: docker volume create journalsystem-data
- run: docker run --name journalsystem-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=journalsystem -p 3306:3306 -v journalsystem-data:/var/lib/mysql -d mysql:8.4
- Alternativt kör: docker run --name journalsystem-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=journalsystem -p 3307:3306 -v journalsystem-data:/var/lib/mysql -d mysql:8.4

docker volume create journalsystem-data(must be run on a devise to be able to make a local database)

npm install react-router-dom (React Router allowes uss to navigate bettwen the pages without having to reload the whole site)


## Spex
- React-frontend igång på port 5173.
- Spring Boot-backend igång på port 8080.
- MySQL-databas igång i Docker på port 3307.

## Docker Kommandon
- docker compose up:	Startar containrarna som redan finns.
- docker compose up --build:	Bygger om images och startar containrarna.
- docker compose down:	Stoppar och tar bort containrarna.
- docker compose up --build -d: Bygger containrar och låter dom köra i bakgrunden
- docker compose down: Stänger ner containrar
- docker exec -it journalsystem-db mysql -u root -p: Få tillgång till mysql databasen