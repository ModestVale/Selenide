import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.*;

public class AppCardDeliveryTest {
    @Test
    public void shouldCreateOrder() {
        $("[data-test-id='city'] input").setValue("Уфа");

        String dateForInput = generateDate(5, "dd.MM.yyyy");
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

        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!\nВстреча успешно забронирована на " + dateForInput));

    }

    @BeforeEach
    public void openBrowser() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldCreateOrderWithExtendedControls() {

        SelenideElement cityField = $("[data-test-id='city'] input");
        cityField.sendKeys("Са");
        $(".input__popup").shouldBe(Condition.visible, Duration.ofSeconds(5));
        ElementsCollection cityMenuItems = $$(".menu-item");
        assertEquals(18, cityMenuItems.size());

        cityMenuItems
                .findBy(Condition.text("Самара"))
                .click();

        $("[data-test-id='date'] input").click();

        $(".calendar")
                .shouldBe(Condition.visible, Duration.ofSeconds(2));

        int daysToAdd = 7;
        String defaultMonth = generateDate(3, "MM");
        String planningMonth = generateDate(daysToAdd, "MM");
        String planningDay = generateDate(daysToAdd, "d");
        String dateForInput = generateDate(daysToAdd, "dd.MM.yyyy");
        if (!defaultMonth.equals(planningMonth)) {
            $("[data-step='1']").click();
        }
        $$("td.calendar__day").find(Condition.exactText(planningDay)).click();

        $("[data-test-id='name'] input").setValue("Петров Игорь");
        $("[data-test-id='phone'] input").setValue("+79226584321");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $$("button[type='button']")
                .findBy(Condition.text("Забронировать"))
                .click();

        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Успешно!\nВстреча успешно забронирована на " + dateForInput));
    }

    private String generateDate(int days, String dateFormat) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(dateFormat));
    }
}
