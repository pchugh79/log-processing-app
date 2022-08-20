# log-processing-app 

This application provides capability to process the logs file which is collection of events.
Every event has 2 entries in the file - one entry when the event was started and another when the event was finished.
The entries in the file have no specific order (a finish event could occur before a start event for a given id)
Every line in the file is a JSON object containing the following event data:

id - the unique event identifier
 * state - whether the event was started or finished (can have values "STARTED" or "FINISHED"
 * timestamp - the timestamp of the event in milliseconds

Application Server logs also have the following additional attributes:
* type - type of log
* host - hostname

Example Contents of the log file:

{"id":"0","state":"STARTED","timestamp":1661020919404,"type":"APPLICATION_LOG","host":"12345"}
{"id":"0","state":"FINISHED","timestamp":1661020924404,"type":"APPLICATION_LOG","host":"12345"}
{"id":"76","state":"FINISHED","timestamp":1661020919555,"type":"APPLICATION_LOG","host":"12345"}
{"id":"77","state":"STARTED","timestamp":1661020919556,"type":"APPLICATION_LOG","host":"12345"}
{"id":"77","state":"FINISHED","timestamp":1661020924556,"type":"APPLICATION_LOG","host":"12345"}
{"id":"78","state":"STARTED","timestamp":1661020919557,"type":"APPLICATION_LOG","host":"12345"}
{"id":"78","state":"FINISHED","timestamp":1661020919557,"type":"APPLICATION_LOG","host":"12345"}
{"id":"79","state":"STARTED","timestamp":1661020919558,"type":"APPLICATION_LOG","host":"12345"}


In these sample events; event-ids with any long events that take longer than 4ms are - 
[0, 77]

# how to run 
This program offers two execution modes - 
* generate records - to generate sample file with given number of records.
* execute - to find all events with any long events that take longer than 4ms


## build

* mvn clean package
  (log-processor-1.0-SNAPSHOT.jar will be created under target directory)
* cd target  

## Generate input file

* java -jar log-processor-1.0-SNAPSHOT.jar --input.file-path=./log-file.txt --input.execution-mode=generate-records --input.num-of-recods=1000

Note: You can skip above step and provide your own input file.

## Process the input file

* java -jar log-processor-1.0-SNAPSHOT.jar --input.file-path=./log-file.txt --input.execution-mode=execute --logging.level.com.mycompany.log.processor=DEBUG


#### Logs will have following statements:
* 2022-08-21 00:21:34.781 DEBUG 2399 --- [           main] c.m.log.processor.LogEventProcessor      : Records processing time 422816658 nano-secs
* 2022-08-21 00:21:34.878  INFO 2399 --- [           main] c.m.l.p.LogEventProcessingApplication    : 143 Number of records with Finished processing timestamp greater than 4 ms.

#### Other logs to flag the records (sample below)
2022-08-21 00:24:29.065 DEBUG 2508 --- [           main] c.m.l.p.LogEventProcessingApplication    : Record : id=143, eventId='14', type='APPLICATION_LOG', host='12345', eventDuration=5000

