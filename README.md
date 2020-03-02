# Example for Camunda's external task worker using the ExternalTaskClient

This small example includes two different implementations of the same process. One implementation uses the classical approach for service tasks, i.e. a Java delegate, whereas the second one uses Camunda's ExternalTaskClient to implement an external task worker. After startup the application continuously starts processes for both implementations and their respective execution of the service task is logged.
