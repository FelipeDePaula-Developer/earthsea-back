#!/bin/bash

# Garantir que o Gradlew seja executável
chmod +x ./gradlew

# Compilar o código com Gradle
./gradlew clean build -x test

# Buscar o nome do arquivo JAR gerado no diretório build/libs
JAR_FILE=$(ls build/libs/*.jar | head -n 1)

# Verificar se o arquivo JAR foi gerado corretamente
if [ -z "$JAR_FILE" ]; then
  echo "Erro: O arquivo JAR não foi encontrado no diretório build/libs."
  exit 1
fi

# Executar a aplicação
java -jar "$JAR_FILE"