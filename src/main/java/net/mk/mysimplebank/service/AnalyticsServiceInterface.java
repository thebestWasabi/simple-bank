package net.mk.mysimplebank.service;

import net.mk.mysimplebank.entity.BankAccount;
import net.mk.mysimplebank.entity.User;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
public interface AnalyticsServiceInterface {
    BigDecimal getMonthlySpendingByCategory(BankAccount bankAccount, String category);

    Map<String, BigDecimal> getMonthlySpendingByCategories(User user, Set<String> categories);
}
