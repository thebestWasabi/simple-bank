package net.mk.mysimplebank.service;

import net.mk.mysimplebank.entity.BankAccount;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
public interface IBankAccountService {
    void processPayment(BankAccount account, BigDecimal amount, PaymentStrategy strategy, Map<String, String> details);
}
