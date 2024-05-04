## Como criar o projeto do zero

### Instalar WSL2 no Windows

Execute um terminal em modo de **administrador** e insira o comando `wsl --install`.
Esse comando habilitará os recursos necessários para executar o WSL e instala a distribuição Ubuntu.
O comando só funcionará se o WSL não estiver instalado. Caso já esteja instalado, execute `wsl --list --online` para ver a lista de distros disponíveis e execute `wsl --install -d <Distroname>` para instalar uma outra distribuição.

Para mais informações acesse: [WSL2](https://learn.microsoft.com/pt-br/windows/wsl/install)

### Instalar Git

Após instalar o WSL2, acesse através do terminal e execute os comandos de instalação e configuração do Git.

```bash
$ sudo apt-get install git
$ git config --global user.name "Your Name"
$ git config --global user.email "youremail@domain.com"
```

### Instalar Docker

Em um terminal execute:

```bash
# Add Docker's official GPG key:
$ sudo apt-get update
$ sudo apt-get install ca-certificates curl
$ sudo install -m 0755 -d /etc/apt/keyrings
$ sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc

$ sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
$ echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
    sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

$ sudo apt-get update
```

Instalar os pacotes do Docker

```bash
$ sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

Para verificar se o Docker Engine foi instalado com sucesso vamos executar o comando para roda a imagem `hello-world`.

```bash
$ sudo docker run hello-world
```

### Instalar Docker Compose

```bash
$ sudo apt-get update
$ sudo apt-get install docker-compose-plugin
```

Para verificar se o docker compose foi instalado corretamente execute:

```bash
$ docker compose version
```

Para executar o docker sem permissões `sudo`

1. Vamos criar um docker group se ele não existir
   > `sudo groupadd docker`
2. Adicionar o usuário ao docker group
   > `sudo usermod -aG docker $USER`
3. Faça login no novo grupo docker (para evitar ter que sair/fazer login novamente; mas se não for suficiente, tente reinicializar)
   > `newgrp docker`
4. Verifique se o docker pode ser executado sem root
   > `docker run hello-world`
5. Se ocorrer erro reinicie o serviço Docker

   ```bash
   # Para parar o serviço
   $ sudo service docker stop
   $ sudo systemctl stop docker

   # Para iniciar o serviço
   $ sudo service docker start
   $ sudo systemctl start docker
   ```

### Instalar Java e Maven

A versão minima sugerida é a `Java 17`, para o Maven a `3.9.6`. Acesse o site do [Apache Maven](https://maven.apache.org/download.cgi) e copie o link do arquivo "binário tar.gz". Acesse pelo terminal e execute os comandos.

```bash
# Instala o Java JDK
$ sudo apt update
$ sudo apt install openjdk-19-jre-headless

# Instala o gerenciador Maven
$ sudo apt-get update
$ wget <LINK_BYNARY_TARGZ_FILE>
$ tar -xvf apache-maven-VERSION-bin.tar.gz
$ mv apache-maven-VERSION /opt/

# Define a variável de ambiente
$ M2_HOME='/opt/apache-maven-VERSION'
$ PATH="$M2_HOME/bin:$PATH"
$ export PATH
```

Acesse o site [Spring Initializr](https://start.spring.io/) selecione as configurações desejadas e gere um novo projeto Spring-Boot. Extraia o arquivo baixado na pasta que deseje.

### Criar o Dockerfile

O Primeiro passo para executar a aplicação é criar o Dockerfile.

```
FROM eclipse-temurin:latest
RUN apt-get update && apt-get -y upgrade
RUN apt-get install -y inotify-tools dos2unix
RUN mkdir -p /app
WORKDIR /app
```

Eclipse Temurin é a imagem base com a ultima versão do OpenJDK instalado em um sistema Debian.
<br> O utilitário chamado `inotify-tools` ajuda a monitorar as alterações no código.
<br> Usamos `dos2unix` para solução alternativa para normalizar as terminações de linha em arquivos criados no Windows e torná-los capazes de executar dentro do container baseados em unix.

Vamos criar um arquivo `run.sh` e um `docker-compose.yml` para ajudar a automatizar as tarefas.

```
dos2unix mvnw
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" &
while true; do
    inotifywait -e modify,create,delete,move -r ./src/ && ./mvnw compile
done
```

```
version: "2.27"

services:
  mongodb:
    image: mongo
    container_name: mongodb
    expose:
      - 27017
    ports:
      - 27017:27017
    volumes:
      - ./db/mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js:ro
      - ./db/data:/data/db
    environment:
      MONGO_INITDB_DATABASE: personal
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: 123456
    networks:
      - mongo-compose-network

  mongo-express:
    image: mongo-express
    container_name: mongo-express
    ports:
      - 8081:8081
    environment:
      ME_CONFIG_MONGODB_SERVER: mongodb
      ME_CONFIG_BASICAUTH_USERNAME: sam
      ME_CONFIG_BASICAUTH_PASSWORD: 123456
      ME_CONFIG_MONGODB_PORT: 27017
      ME_CONFIG_MONGODB_ADMINUSERNAME: root
      ME_CONFIG_MONGODB_ADMINPASSWORD: 123456
    networks:
      - mongo-compose-network

  app:
    build:
      context: ./
      dockerfile: Dockerfile
    container_name: spring-app
    volumes:
      - ./:/app
      - ./.m2:/root/.m2
    working_dir: /app
    command: sh run.sh
    ports:
      - 8080:8080
      - 35729:35729
      - 5005:5005
    depends_on:
      mongodb:
        condition: service_started
    networks:
      - mongo-compose-network

networks:
    mongo-compose-network:
      driver: bridge
```

### mongodb

> - **Image**: utiliza a imagem oficial do MongoDB
>
> - **Ports**: mapeia a porta 27017 do container para a porta 27017 do host
>
> - **Environment**: configuração das variaveis de ambiente para usuário e senha para acesso ao banco
>
> - **Volumes**: declara um volume, garantindo armazenamento persistente para dados do MongoDB

### mongo-express

> - **Image**: utiliza a imagem oficial do MongoDB Express
>
> - **Ports**: mapeia a porta 8081 do container para a porta 8081 do host
>
> - **Environment**: especifica o nome de usuário do administrador, a senha e o nome do host do servidor MongoDB

### app

O diretório de código-fonte local é montado dentro do container no caminho `/app`.
<br> Montamos o diretório `.m2` de dentro do container para está disponível como um diretório local em nosso sistema de arquivos local. Isso permitirá que destruamos o container e recrie sem grandes problemas.

- A inicialização do container fica condicionada a inicialização do container com o servidor MongoDB
- A aplicação irá executar na porta 8080
- Porta 35729 habilita o plugin livereload a monitorar as alterações no código
- Porta 5005 torna o debug disponível pela IDE

Na raiz do projeto crie uma pasta nomeada `db` para podermos guardar o script de criação do banco de dados e usuário para acesso.
A camada de volumes do docker compose no serviço mongodb é responsável por copiar todo o conteúdo da pasta **db** para a pasta **docker-entrypoint-initdb.d** dentro do container.

```js
print("Started Adding the Users.");

db.getSiblingDB("<DATABASE>");
db.createUser({
  user: "<USER>",
  pwd: "<PASSWORD>",
  roles: [
    {
      role: "readWrite",
      db: "<DATABASE>",
    },
  ],
});

db.createCollection("user");
```

Após os passos executados acima estamos pronto para rodarmos os containers com as aplicações. É necessário contruir as imagens antes de executar os containers.

```bash
$ docker-compose build
```

e para executar o container

```bash
$ docker-compose up
```

Se quiser rodar os containers em segundo plano e verificar os logs das aplicações

```bash
$ docker-compose up -d
# Obtem o id do container
$ docker ps
$ docker logs --tail 1000 -f <CONTAINER_ID>
```

Para destruir os containers criados

```bash
$ docker-compose down
```
