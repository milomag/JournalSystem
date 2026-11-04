## Instalation Guide
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


## Spex
- React-frontend igång på port 5173.
- Spring Boot-backend igång på port 8080.
- MySQL-databas igång i Docker på port 3307.