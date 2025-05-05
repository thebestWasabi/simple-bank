package net.mk.mysimplebank.service;

import net.mk.mysimplebank.entity.BankAccount;
import net.mk.mysimplebank.entity.User;
import net.mk.mysimplebank.service.impl.AnalyticsService;
import net.mk.mysimplebank.testutil.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static net.mk.mysimplebank.testutil.Constants.BEAUTY_CATEGORY;
import static net.mk.mysimplebank.testutil.Constants.EDUCATION_CATEGORY;
import static net.mk.mysimplebank.testutil.Constants.HEALTH_CATEGORY;
import static net.mk.mysimplebank.testutil.Constants.ONE_DOLLAR;
import static net.mk.mysimplebank.testutil.Constants.TWO_DOLLARS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
class AnalyticsServiceTest {
    private final TransactionService transactionService = new TransactionService();
    private final AnalyticsService analyticsService = new AnalyticsService(transactionService);

    private final User user = new User();
    private BankAccount bankAccount;

    @BeforeEach
    void setUp() {
        // Валидные транзакции (должны попасть в результат)
        final var validTransactions = List.of(Data.transaction1, Data.transaction2);

        // Старые транзакции (старше одного месяца — игнорируются)
        final var oldTransactions = List.of(Data.transaction3);

        // Неправильный тип транзакций (DEPOSIT — игнорируются)
        final var depositTransactions = List.of(Data.transaction4);

        // Невалидные категории (не входят в input set или validateCategories — игнорируются)
        final var invalidCategoryTransactions = List.of(Data.transaction5);

        // Один аккаунт со всеми видами транзакций
        final var allTransactions = Stream.of(validTransactions, oldTransactions, depositTransactions, invalidCategoryTransactions)
                .flatMap(Collection::stream)
                .toList();

        bankAccount = BankAccount.builder()
                .id("001")
                .transactions(allTransactions)
                .build();

        user.getBankAccounts().add(bankAccount);
    }

    @Test
    void getMonthlySpendingByCategory_BigDecimal_returnAmount() {
        final var result = analyticsService.getMonthlySpendingByCategory(bankAccount, BEAUTY_CATEGORY);
        assertEquals(TWO_DOLLARS, result);
    }

    @Test
    void getMonthlySpendingByCategory_BigDecimal_returnZeroAmountIfBankAccountIsNull() {
        final var result = analyticsService.getMonthlySpendingByCategory(null, BEAUTY_CATEGORY);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getMonthlySpendingByCategory_BigDecimal_returnZeroAmountIfCategoryIsNullOrDummy() {
        final var result1 = analyticsService.getMonthlySpendingByCategory(bankAccount, null);
        final var result2 = analyticsService.getMonthlySpendingByCategory(bankAccount, "dummy");
        assertAll(
                () -> assertEquals(BigDecimal.ZERO, result1),
                () -> assertEquals(BigDecimal.ZERO, result2)
        );
    }

    @Test
    void getTransactionHistorySortedByAmount() {
        final var result = analyticsService.getTransactionHistorySortedByAmount(user);

        Assertions.assertNotNull(result);
        assertEquals(1, result.get(BEAUTY_CATEGORY).size());
        assertEquals(1, result.get(HEALTH_CATEGORY).size());
        assertEquals(1, result.get(EDUCATION_CATEGORY).size());

        assertEquals(TWO_DOLLARS, result.get(BEAUTY_CATEGORY).get(0).getValue());
        assertEquals(ONE_DOLLAR, result.get(HEALTH_CATEGORY).get(0).getValue());
        assertEquals(TWO_DOLLARS, result.get(EDUCATION_CATEGORY).get(0).getValue());
    }
}
