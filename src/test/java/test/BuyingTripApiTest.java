package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.ApiUtils;
import data.Card;
import data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class BuyingTripApiTest {

    Card invalidHolderCard = DataGenerator.getInvalidHolderCard();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Не должен отправлять запрос на оплату с некорректным именем владельца")
    void shouldNotSendPaymentRequestWithIncorrectName() {
        int statusCode = ApiUtils.getRequestStatusCode(invalidHolderCard, "/api/v1/pay");
        assertNotEquals(200, statusCode);
    }

    @Test
    @DisplayName("Не должен отправлять запрос на кредит с некорректным именем владельца")
    void shouldNotSendCreditRequestWithIncorrectName() {
        int statusCode = ApiUtils.getRequestStatusCode(invalidHolderCard, "/api/v1/credit");
        assertNotEquals(200, statusCode);
    }

    @Test
    @DisplayName("Должен провести оплату за услуги с валидной карты")
    void shouldCheckValidBuyerPayment() {
        int statusCode = ApiUtils.getRequestStatusCode(DataGenerator.getValidCard(), "/api/v1/pay");
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Должен провести оплату при покупке услуги в кредит")
    void shouldCheckValidBuyerCreditPayment() {
        int statusCode = ApiUtils.getRequestStatusCode(DataGenerator.getValidCard(), "/api/v1/credit");
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Не должен провести оплату при покупке услуги в кредит")
    void shouldCheckDeclinedCreditPayment() {
        int statusCode = ApiUtils.getRequestStatusCode(DataGenerator.getDeclinedCard(), "/api/v1/credit");
        assertNotEquals(200, statusCode);
    }

    @Test
    @DisplayName("Не должен провести оплату при покупке услуги в кредит")
    void shouldCheckDeclinedDebitPayment() {
        int statusCode = ApiUtils.getRequestStatusCode(DataGenerator.getDeclinedCard(), "/api/v1/pay");
        assertNotEquals(200, statusCode);
    }
}
