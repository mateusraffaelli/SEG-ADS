# Usando uma imagem do Gradle 9.x com JDK 25 para compilar o projeto
FROM gradle:9.3.1-jdk25-alpine AS build
WORKDIR /codigo-java

# Copiando todo projeto gradle para dentro do contêiner
COPY . .

# Executando o comando gradle para compilar o projeto e gerar os arquivos de distribuição
# O comando 'gradle installDist' irá gerar os arquivos de distribuição na pasta build/install
RUN gradle installDist

# A partir daqui, iremos criar uma nova imagem que conterá apenas o JRE e os arquivos de distribuição
FROM eclipse-temurin:25-jre-alpine
WORKDIR /aplicacao

# Copiando os arquivos de distribuição gerados na etapa de build para o contêiner final
COPY --from=build /codigo-java/app/build/install/app /aplicacao

ENTRYPOINT ["./bin/app"]