#!/bin/sh

echo "Compilando aplicação..."

ls

# Verifica se o arquivo gradlew existe antes de tentar executá-lo
if [ ! -f "./gradlew" ]; then
  echo "Erro: gradlew não encontrado!"
  exit 1
fi

# Torna o gradlew executável
chmod +x ./gradlew

# Compila a aplicação (caso necessário)
./gradlew clean build -x test --no-daemon

# Confere se o JAR foi gerado
if [ -z "$(ls -A build/libs/*.war 2>/dev/null)" ]; then
  echo "Erro: O arquivo WAR não foi encontrado no diretório build/libs."
  exit 1
fi

echo "Iniciando aplicação..."
CMD ["java", "-jar", "/app/app.war"]