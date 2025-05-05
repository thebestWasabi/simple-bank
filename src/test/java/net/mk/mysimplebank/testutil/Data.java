package net.mk.mysimplebank.testutil;

import net.mk.mysimplebank.entity.Transaction;

import static net.mk.mysimplebank.entity.TransactionType.DEPOSIT;
import static net.mk.mysimplebank.entity.TransactionType.PAYMENT;
import static net.mk.mysimplebank.testutil.Constants.BEAUTY_CATEGORY;
import static net.mk.mysimplebank.testutil.Constants.EDUCATION_CATEGORY;
import static net.mk.mysimplebank.testutil.Constants.FIVE_DOLLARS;
import static net.mk.mysimplebank.testutil.Constants.HEALTH_CATEGORY;
import static net.mk.mysimplebank.testutil.Constants.ONE_DAY_AGO;
import static net.mk.mysimplebank.testutil.Constants.ONE_DOLLAR;
import static net.mk.mysimplebank.testutil.Constants.ONE_WEEK_AGO;
import static net.mk.mysimplebank.testutil.Constants.TEN_DOLLARS;
import static net.mk.mysimplebank.testutil.Constants.TWO_DOLLARS;
import static net.mk.mysimplebank.testutil.Constants.TWO_MONTH_AGO;

/**
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
public final class Data {
    public Data() {
    }

    public static final Transaction transaction1 = Transaction.builder()
            .id("t1").value(TWO_DOLLARS).type(PAYMENT).category(BEAUTY_CATEGORY).createdDate(ONE_DAY_AGO)
            .build();

    public static final Transaction transaction2 = Transaction.builder()
            .id("t2").value(ONE_DOLLAR).type(PAYMENT).category(HEALTH_CATEGORY).createdDate(ONE_DAY_AGO)
            .build();

    public static final Transaction transaction3 = Transaction.builder()
            .id("t3").value(TWO_DOLLARS).type(PAYMENT).category(EDUCATION_CATEGORY).createdDate(TWO_MONTH_AGO)
            .build();

    public static final Transaction transaction4 = Transaction.builder()
            .id("t4").value(TEN_DOLLARS).type(DEPOSIT).category(BEAUTY_CATEGORY).createdDate(ONE_WEEK_AGO)
            .build();

    public static final Transaction transaction5 = Transaction.builder()
            .id("t5").value(FIVE_DOLLARS).type(PAYMENT).category("INVALID_CATEGORY").createdDate(ONE_WEEK_AGO)
            .build();
}
