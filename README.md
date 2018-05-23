# FRAM Solr Change Monitor
This service is responsible to poll the changes from escenic solr service and queue it for further processing by the other FRAM services. 

## Architecture
This service stack has 3 parts
- The main spring boot java service
- A relational database to store the processed timestamps
- A RabbitMQ instance to send the changes

## Running it on local
For local usage you can use the `docker-compose.yml` to spin up the required services

## Usage
- Every 10 seconds the application polls the changes from escenic solr, always fetching the changes from the last 10 seconds. 
- The changelog can be reset with  the `POST /changebatch` endpoint, sending 
`{
 	"resetMillis": 864000000
 }` as payload. The `resetMillis` is how many millis you need to go back in time to reprocess. 
 
