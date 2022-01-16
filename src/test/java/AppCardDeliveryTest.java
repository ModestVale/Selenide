import com.codeborne.selenide.Condition;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {
    @Test
    public void shouldCreateOrder() {
        $("[data-test-id='city'] input").setValue("Уфа");

        Date dateNow = new Date();
        Date futureDate = DateUtils.addDays(dateNow, 5);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        String dateForInput = formatForDateNow.format(futureDate);
        $("[data-test-id='date'] input").setValue(dateForInput);

        $("[data-test-id='name'] input").setValue("Петров Игорь");
        $("[data-test-id='phone'] input").setValue("+79226584321");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $$("button[type='button']")
                .findBy(Condition.text("Забронировать"))
                .click();
        $("fieldset").shouldHave(Condition.attribute("disabled"), Duration.ofSeconds(2));
        $("fieldset").shouldNotHave(Condition.attribute("disabled"), Duration.ofSeconds(15));
    }

    @BeforeEach
    public void openBrowser() {
        open("http://localhost:9999");
    }
}
