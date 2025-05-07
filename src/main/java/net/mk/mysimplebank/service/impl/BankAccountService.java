package net.mk.mysimplebank.service.impl;

import lombok.RequiredArgsConstructor;
import net.mk.mysimplebank.anatation.LogExecutionTime;
import net.mk.mysimplebank.entity.BankAccount;
import net.mk.mysimplebank.entity.Transaction;
import net.mk.mysimplebank.entity.TransactionType;
import net.mk.mysimplebank.entity.User;
import net.mk.mysimplebank.service.IBankAccountService;
import net.mk.mysimplebank.service.PaymentGatewayService;
import net.mk.mysimplebank.service.PaymentStrategy;
import net.mk.mysimplebank.service.notification.ExternalNotificationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Сервис отвечает за управление счетами, включая создание, удаление и пополнение
 *
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */

@RequiredArgsConstructor
public class BankAccountService implements IBankAccountService {
    private final PaymentGatewayService paymentGatewayService = PaymentGatewayService.getInstance();
    private final ExternalNotificationService externalNotificationService;

    public BankAccount findAccountById(final int accountId) {
        return createTestAccount(); // временно заглушка
    }

    public void withdraw(final BankAccount account, final BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
    }

    @Override
    @LogExecutionTime
    public void processPayment(final BankAccount account, final BigDecimal amount,
                               final PaymentStrategy strategy,
                               final Map<String,String> details) {
        strategy.process(account, amount, details);
    }

    public static BankAccount createTestAccount() {
        final var testUser = new User();
        testUser.setId("user123");
        testUser.setFirstName("John");
        testUser.setMiddleName("K");
        testUser.setLastName("Doe");
        testUser.setBirthDate(LocalDateTime.now().minusYears(25).toLocalDate());
        testUser.setEmail("john.doe@example.com");
        testUser.setPhoneNumber("+1234567890");

        final var account = new BankAccount();
        account.setId("acc123");
        account.setBalance(new BigDecimal("5000.00"));
        account.setOwner(testUser);

        final var transaction1 = Transaction.builder()
                .id("tx001")
                .value(new BigDecimal("100.00"))
                .type(TransactionType.PAYMENT)
                .category("Electronics")
                .createdDate(LocalDateTime.now().minusDays(5))
                .build();

        final var transaction2 = Transaction.builder()
                .id("tx002")
                .value(new BigDecimal("200.00"))
                .type(TransactionType.DEPOSIT)
                .category("Groceries")
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();

        account.getTransactions().addAll(List.of(transaction1, transaction2));

        return account;
    }
}
