package net.mk.mysimplebank.service.impl;

import lombok.RequiredArgsConstructor;
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
public class BankTransferStrategy implements PaymentStrategy {
    private final PaymentGatewayService paymentGatewayService;
    private final ExternalNotificationService externalNotificationService;

    @Override
    public void process(final BankAccount account, final BigDecimal amount, final Map<String, String> details) {
        // 1. Списание средств
        account.setBalance(account.getBalance().subtract(amount));

        // 2. Получение деталей платежа
        final var bankName = details.get("bankName");                 // Название банка-получателя
        final var category = details.get("category");                 // Категория платежа
        final var merchant = details.get("merchantName");             // Название продавца или получателя
        final var categoryCode = details.get("merchantCategoryCode"); // MCC-код (если есть)

        // 3. Создание транзакции
        final var transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .value(amount)
                .type(TransactionType.PAYMENT)
                .category(category)
                .createdDate(LocalDateTime.now())
                .merchantName(merchant)
                .merchantCategoryCode(categoryCode)
                .bankName(bankName)
                .build();

        // 4. Логика обработки
        System.out.printf("Processed bank transfer from %s to bank %s%n", account.getId(), bankName);
        paymentGatewayService.authorize("Банковский перевод", amount);
        externalNotificationService.sendSms(account.getOwner().getPhoneNumber(), "Произведён банковский перевод");
        externalNotificationService.sendEmail(account.getOwner().getEmail(), "Информация о переводе", "Произведён банковский перевод");
    }
}
