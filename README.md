# SCALA AKKA TCP SAMPLE

## Runbook
### Step 1: Install docker
Follow this instruction: https://docs.docker.com/compose/install/

### Step 2: Run postgres with docker-compose
`cd ./`

`docker-compose up -d`

### Step 3: Create `item` table
`psql -U postgres -h localhost -p 5432 -a -f insert.sql`

### Step 3: Build client and server jar
``sbt client/assembly``

``sbt server/assembly``

### Step 4: Run server and client application with jar file
#### Server: ``java -jar modules/server/target/scala-2.13/server-assembly-0.1.0-SNAPSHOT.jar <host> <port>``
#### Client: ``java -jar modules/client/target/scala-2.13/client-assembly-0.1.0-SNAPSHOT.jar <host> <port>``

#### Example:
``java -jar modules/server/target/scala-2.13/server-assembly-0.1.0-SNAPSHOT.jar 0.0.0.0 9900``

``java -jar modules/client/target/scala-2.13/client-assembly-0.1.0-SNAPSHOT.jar localhost 9900``

#### Note: If you want to run multiple client simultaneously, you just need to run client command on multiple CLI windows