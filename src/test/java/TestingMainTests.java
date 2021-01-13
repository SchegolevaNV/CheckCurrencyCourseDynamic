import main.Main;
import main.controllers.MainController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes=Main.class)
class TestingMainTests {

    @Autowired
    private MainController mainController;

    @Test
    void contextLoads() throws Exception {
        assertThat(mainController).isNotNull();
    }
}
