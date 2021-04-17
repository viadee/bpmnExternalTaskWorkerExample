# External-Task Examples
## Example for Camunda's external task worker using the ExternalTaskClient  
[ExternalTaskExampleHandler](ExternalTaskExampleHandler)  

This small example includes two different implementations of the same process. One implementation uses the classical approach for service tasks, i.e. a Java delegate, whereas the second one uses Camunda's ExternalTaskClient to implement an external task worker. After startup the application continuously starts processes for both implementations and their respective execution of the service task is logged.  
  
More information: https://blog.viadee.de/camunda-bpm-external-task-worker (german)  

## Example for exception-handling while using Camunda's external task worker 
[ExceptionHandling](ExceptionHandling)  
  
More information: tbd. 
