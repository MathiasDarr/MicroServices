package org.mddarr.ordersservice;

import org.junit.ClassRule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AbstractIntegrationTest {
    @ClassRule
    public static GenericContainer dynamoDBLocal =
            new GenericContainer("amazon/dynamodb-local:1.11.477")
                    .withExposedPorts(8000);


}
