package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Card;
import data.DataGenerator;
import data.DbUtils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.CreditPage;
import page.PaymentPage;
import page.StartPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class BuyingTripDbTest {

    Card validCard = DataGenerator.getValidCard();
    Card declinedCard = DataGenerator.getDeclinedCard();
    Card fakeCard = DataGenerator.getFakeCard();

    @BeforeEach
    public void openPage() throws SQLException {
        //DbUtils.clearTables();
        String url = System.getProperty("sut.url");
        open(url);
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Должен подтверждать покупку по карте со статусом APPROVED")
    void shouldConfirmPaymentWithValidCard() throws SQLException {
        StartPage startPage = new StartPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.fillData(validCard);
        assertEquals("APPROVED", DbUtils.findPaymentStatus());
        paymentPage.checkForSuccessfulNotification();
    }

    @Test
    @DisplayName("Должен подтверждать кредит по карте со статусом APPROVED")
    void shouldConfirmCreditWithValidCard() throws SQLException {
        StartPage startPage = new StartPage();
        CreditPage creditPage = startPage.goToCreditPage();
        creditPage.fillData(validCard);
        assertEquals("APPROVED", DbUtils.findCreditStatus());
        creditPage.checkForSuccessfulNotification();

    }

    @Test
    @DisplayName("Не должен подтверждать покупку по карте со статусом DECLINED")
    void shouldNotConfirmPaymentWithDeclinedCard() throws SQLException {
        StartPage startPage = new StartPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.fillData(declinedCard);
        assertEquals("DECLINED", DbUtils.findPaymentStatus());
        paymentPage.checkErrorMessageIsDisplayed();
    }

    @Test
    @DisplayName("Не должен подтверждать кредит по карте со статусом DECLINED")
    void shouldNotConfirmCreditWithDeclinedCard() throws SQLException {
        StartPage startPage = new StartPage();
        CreditPage creditPage = startPage.goToCreditPage();
        creditPage.fillData(declinedCard);
        assertEquals("DECLINED", DbUtils.findCreditStatus());
        creditPage.showErrorMessage();

    }

    @Test
    @DisplayName("Не должен подтверждать покупку по несуществующей карте")
    void shouldNotConfirmPaymentWithFakeCard() throws SQLException {
        DbUtils.clearTables();
        StartPage startPage = new StartPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.fillData(fakeCard);
        assertEquals("0", DbUtils.countRecords());
        paymentPage.checkErrorMessageIsDisplayed();

    }

    @Test
    @DisplayName("Не должен подтверждать кредит по несуществующей карте")
    void shouldNotConfirmCreditWithFakeCard() throws SQLException {
        StartPage startPage = new StartPage();
        CreditPage creditPage = startPage.goToCreditPage();
        creditPage.fillData(fakeCard);
        creditPage.showErrorMessage();
        assertEquals("0", DbUtils.countRecords());
    }
}
