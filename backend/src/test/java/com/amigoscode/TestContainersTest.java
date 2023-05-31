package com.amigoscode;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;


public class TestContainersTest extends AbstractTestcontainersUnitTest
{

	@Test
	void canStartPostgresDB()
	{
		assertThat(postgreSQLContainer.isRunning()).isTrue();
		assertThat(postgreSQLContainer.isCreated()).isTrue();
	}
	

}
