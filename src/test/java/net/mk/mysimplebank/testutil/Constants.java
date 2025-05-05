package net.mk.mysimplebank.testutil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */

public final class Constants {
    private Constants() {
    }

    public static final BigDecimal ONE_DOLLAR = new BigDecimal("1.0");
    public static final BigDecimal TWO_DOLLARS = new BigDecimal("2.0");
    public static final BigDecimal FIVE_DOLLARS = new BigDecimal("5.0");
    public static final BigDecimal TEN_DOLLARS = new BigDecimal("10.0");

    public static final String BEAUTY_CATEGORY = "Beauty";
    public static final String HEALTH_CATEGORY = "Health";
    public static final String EDUCATION_CATEGORY = "Education";

    public static final LocalDateTime NOW = LocalDateTime.of(2025, 5, 1, 0, 0);

    public static final LocalDateTime ONE_DAY_AGO = NOW.minusDays(1);
    public static final LocalDateTime ONE_MONTH_AGO = NOW.minusMonths(1);
    public static final LocalDateTime TWO_MONTH_AGO = NOW.minusMonths(2);
    public static final LocalDateTime ONE_WEEK_AGO = NOW.minusWeeks(1);
    public static final LocalDateTime FIVE_WEEK_AGO = NOW.minusWeeks(5);
}
