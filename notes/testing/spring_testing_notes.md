### Spring Boot testing


@SpringBootTest
starts an application context to be used in a test.  

@WebMvcTest
This annotation fires up an application context that only contains the beans needed for testing the web controller

setting the @WebMvcTest controllers parameter to a controller class restricts the application context for this test to the given controller 
bean and some framework beans needed for a Spring Web MVC.  All other beans we might need have to be included seperately or mocked away with @MockBean

Spring Boot automatically provides beans like an @ObjectMapper to map to and from JSON and a MockMvc instance to simulate HTTP
requests.  

Controllers:
controllers are responsible for the following
1) Listen to HTTP requests -> controller should respond to certain URLs, HTTP methods & content types
2) Deserialize Input -> controller should parse the incoming HTTP request & create Java objects from variables in the URL
    HTTP request parameters & the request body
3) Validate Input -> controller should validate against bad input
4) Call the business logic -> Having parsed the input, the controller must transform the input into the model expected by the business logic
5) Serialize the output -> The controller takes the output of the business logic & serialized it into an HTTP resposne
6) Translate Exceptions -> If an exception occurs somewhere along the way, the controller should translate it into a meaningful error message & HTTP status for the user

In order to test the controller we must use an integration test

1) Verifying HTTP Request Matching
verifying that a controller listens to a certain HTTP request is pretty straightforward.  We simply call the perform() method of MockMvc & provide the URL we want to test

2)



