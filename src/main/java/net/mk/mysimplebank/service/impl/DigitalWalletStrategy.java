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
public class DigitalWalletStrategy implements PaymentStrategy {
    private final PaymentGatewayService paymentGatewayService;
    private final ExternalNotificationService externalNotificationService;

    @Override
    public void process(final BankAccount account, final BigDecimal amount, final Map<String, String> details) {
        // 1. Списание средств
        account.setBalance(account.getBalance().subtract(amount));

        // 2. Получение деталей платежа
        final var walletId = details.get("digitalWalletId");          // ID электронного кошелька
        final var merchant = details.get("merchantName");             // название продавца
        final var categoryCode = details.get("merchantCategoryCode"); // MCC-код
        final var category = details.get("category");                 // категория расходов

        // 3. Создание транзакции
        final var transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .value(amount)
                .type(TransactionType.PAYMENT)
                .category(category)
                .createdDate(LocalDateTime.now())
                .merchantName(merchant)
                .merchantCategoryCode(categoryCode)
                .digitalWalletId(walletId)
                .build();

        // 4. Обработка платежа
        System.out.printf("Processed digital wallet payment from %s using wallet %s%n", account.getId(), walletId);
        paymentGatewayService.authorize("Платеж через электронный кошелёк", amount);
        externalNotificationService.sendSms(account.getOwner().getPhoneNumber(), "Произошел платеж через кошелек");
        externalNotificationService.sendEmail(account.getOwner().getEmail(), "Информация о платеже", "Произошел платеж через электронный кошелёк");
    }
}
