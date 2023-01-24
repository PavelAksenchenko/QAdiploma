package data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DataGenerator {

    public static String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCurrentYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static Card getValidCard() {
        return new Card("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "Card Holder", "123");
    }

    public static Card getDeclinedCard() {
        return new Card("4444 4444 4444 4442", getCurrentMonth(), getCurrentYear(), "Card Holder", "123");
    }

    public static Card getFakeCard() {
        return new Card("4444 4444 4444 4449", getCurrentMonth(), getCurrentYear(), "Card Holder", "123");
    }

    public static Card getInvalidHolderCard() {
        return new Card("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "123456789Йцукенгшщзхъ!\"№;%:?*()123456789Йцукенгшщзхъ!\"№;%:?*()", "123");
    }

    public static Card getInvalidExpDateCard(int months) {
        String month = LocalDate.now().minusMonths(months).format(DateTimeFormatter.ofPattern("yy"));
        String year = getCurrentYear();
        return new Card("4444 4444 4444 4441", month, year, "Card Holder", "123");
    }

}