### Mockito Notes ###

@Mock
fields annotated with @Mock will be automatically be initialized w/ a mock instance of their type
@InjectMocks 
Mockito attempts to instantiate fields annotated w/ @InjectMocks by passing all mocks into a constructor/  
    - If Mockito doesn't find a constructor, it will try setter injection or field injection 
    
   
When we are testing the controller, which will have a dependency upon the the service,
    - running the test will cause an error because the service will not be instantiated correctly
    - use the @MockBean annotation 
    
    
BDDMockito
provides BDD aliases for various Mockito methods so we can werite our arrange step using given(instead of when), likewise
we could write our Assert step using then (instead of verify)