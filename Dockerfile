# Usar uma imagem oficial do OpenJDK 17 como base para o build com Gradle
FROM openjdk:17-jdk-slim AS build

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar os arquivos necessários para o Gradle e o código-fonte
COPY gradlew .
COPY gradle gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src/

# Garantir que o Gradlew seja executável
RUN chmod +x gradlew

# Baixar e preparar as dependências (cache para evitar baixar em todos os builds)
RUN ./gradlew dependencies --no-daemon

# Compilar e empacotar a aplicação
RUN ./gradlew clean build -x test --no-daemon

# Listar arquivos no diretório output para depuração
RUN ls -l /app/build/libs

# Copiar o entrypoint
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]

# Usar uma imagem menor do OpenJDK 17 para a execução
FROM openjdk:17-jdk-slim

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR gerado no estágio de build para o estágio de execução
COPY --from=build /app/build/libs/*.war /app/

# Expor a porta padrão da aplicação Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "app.war"]