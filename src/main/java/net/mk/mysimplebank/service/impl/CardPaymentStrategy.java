package net.mk.mysimplebank.service.impl;

import lombok.RequiredArgsConstructor;
import net.mk.mysimplebank.anatation.LogExecutionTime;
import net.mk.mysimplebank.entity.BankAccount;
import net.mk.mysimplebank.entity.Transaction;
import net.mk.mysimplebank.entity.TransactionType;
import net.mk.mysimplebank.service.PaymentGatewayService;
import net.mk.mysimplebank.service.PaymentStrategy;
import net.mk.mysimplebank.service.notification.ExternalNotificationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
@RequiredArgsConstructor
public class CardPaymentStrategy implements PaymentStrategy {
    private final PaymentGatewayService paymentGatewayService;
    private final ExternalNotificationService externalNotificationService;

    @Override
    public void process(final BankAccount account, final BigDecimal amount, final Map<String, String> details) {
        // 1. Списание средств
        account.setBalance(account.getBalance().subtract(amount));

        // 2. Получение деталей платежа
        final var merchant = details.get("merchantName");             // название продавца
        final var categoryCode = details.get("merchantCategoryCode"); // MCC-код продавца
        final var cardNumber = details.get("cardNumber");             // последние 4 цифры карты покупателя
        final var category = details.get("category");

        // 3. Создание транзакции
        final var transaction = Transaction.builder()
                .id(UUID.randomUUID().toString()) // уникальный ID
                .value(amount)
                .type(TransactionType.PAYMENT)
                .category(category)
                .createdDate(LocalDateTime.now())
                .merchantName(merchant)
                .merchantCategoryCode(categoryCode)
                .cardNumber(cardNumber)
                .build();

        // 4. Сохранение транзакции в аккаунт
        account.getTransactions().add(transaction);

        // 5. Платёж и уведомления
        System.out.printf("Processed card payment from %s for merchant %s%n", account.getId(), merchant);
        paymentGatewayService.authorize("Платеж по карте", amount);
        externalNotificationService.sendSms(account.getOwner().getPhoneNumber(), "Произошел платеж по карте");
        externalNotificationService.sendEmail(account.getOwner().getEmail(), "Информация о платеже", "Произошел платеж по карте");
    }
}
