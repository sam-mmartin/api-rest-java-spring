# Project Backend Java-Spring

## Projeto api rest do meu site pessoal

### Pré-requisitos

Antes de começar você vai precisar ter instalado na sua máquina
as seguinte ferramentas:

- [Git](https://git-scm.com)
- [Docker](https://docker.com)
- [VS Code](https://code.visualstudio.com/)
- [wsl2](https://learn.microsoft.com/pt-br/windows/wsl/install)
- [Java](https://www.oracle.com/br/java/technologies/downloads/)
- [Maven](https://www.oracle.com/br/java/technologies/downloads/)

Caso necessite verificar como criar o projeto do zero acesse o `HELP_EXEC.md`

### Executar o projeto

```bash
$ docker-compose build
$ docker-compose up
```

> [!IMPORTANT]
>
> Caso ocorra o erro `mvnw: Permission denied` ao executar o comando `docker-compose up` execute o comando:

```bash
$ sudo chmod +x mvnw
```

### Acessar manualmente o banco

Para acessar o banco de dado após ter iniciado o container.

```bash
# Obtem o id do container
$ docker ps
$ docker exec -it <CONTAINER_ID> bash
$ mongosh -u root -p <SENHA_DEFINIDA_DOCKER_COMPOSE> --authenticationDatabase admin
```
