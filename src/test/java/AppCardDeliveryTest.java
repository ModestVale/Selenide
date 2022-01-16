import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.*;

public class AppCardDeliveryTest {
    @Test
    public void shouldCreateOrder() {
        $("[data-test-id='city'] input").setValue("Уфа");

        Date dateNow = new Date();
        Date futureDate = DateUtils.addDays(dateNow, 5);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        String dateForInput = formatForDateNow.format(futureDate);
        SelenideElement dateField = $("[data-test-id='date'] input");
        dateField.sendKeys(Keys.CONTROL + "a");
        dateField.sendKeys(Keys.DELETE);
        dateField.setValue(dateForInput);

        $("[data-test-id='name'] input").setValue("Петров Игорь");
        $("[data-test-id='phone'] input").setValue("+79226584321");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $$("button[type='button']")
                .findBy(Condition.text("Забронировать"))
                .click();
        $("fieldset").shouldHave(Condition.attribute("disabled"), Duration.ofSeconds(2));
        $("fieldset").shouldNotHave(Condition.attribute("disabled"), Duration.ofSeconds(15));
        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Успешно!\nВстреча успешно забронирована на " + dateForInput));

    }

    @BeforeEach
    public void openBrowser() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldCreateOrderWithExtendedControls() {

        SelenideElement cityField = $("[data-test-id='city'] input");
        cityField.sendKeys("С");
        cityField.sendKeys("а");
        $(".input__popup").shouldBe(Condition.visible, Duration.ofSeconds(5));
        ElementsCollection cityMenuItems = $$(".menu-item");
        assertEquals(18, cityMenuItems.size());

        cityMenuItems.get(7).click();

        $("[data-test-id='date'] input").click();

        $(".calendar")
                .shouldBe(Condition.visible, Duration.ofSeconds(2));

        Date dateNow = new Date();
        Date futureDate = DateUtils.addWeeks(dateNow, 1);
        futureDate = DateUtils.setHours(futureDate, 0);
        futureDate = DateUtils.setMinutes(futureDate, 0);
        futureDate = DateUtils.setSeconds(futureDate, 0);
        futureDate = DateUtils.setMilliseconds(futureDate, 0);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        String dateForInput = formatForDateNow.format(futureDate);

        long futureDateUnixTime = futureDate.getTime();
        String dayButtonSelector = "[data-day='" + futureDateUnixTime + "']";
        SelenideElement dayElement = $(dayButtonSelector);
        if (!dayElement.exists()) {
            $("[data-step='1']").click();
            dayElement = $(dayButtonSelector);
        }

        dayElement.click();

        $("[data-test-id='name'] input").setValue("Петров Игорь");
        $("[data-test-id='phone'] input").setValue("+79226584321");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $$("button[type='button']")
                .findBy(Condition.text("Забронировать"))
                .click();
        $("fieldset").shouldHave(Condition.attribute("disabled"), Duration.ofSeconds(2));
        $("fieldset").shouldNotHave(Condition.attribute("disabled"), Duration.ofSeconds(15));
        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Успешно!\nВстреча успешно забронирована на " + dateForInput));
    }
}
