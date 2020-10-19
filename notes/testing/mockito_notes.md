### Mockito Notes ###

@Mock
fields annotated with @Mock will be automatically be initialized w/ a mock instance of their type
@InjectMocks 
Mockito attempts to instantiate fields annotated w/ @InjectMocks by passing all mocks into a constructor/  
    - If Mockito doesn't find a constructor, it will try setter injection or field injection 