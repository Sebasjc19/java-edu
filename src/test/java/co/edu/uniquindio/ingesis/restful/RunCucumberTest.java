package co.edu.uniquindio.ingesis.restful;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "src/test/java/co/edu/uniquindio/ingesis/restful/steps"
)
public class RunCucumberTest {
}
