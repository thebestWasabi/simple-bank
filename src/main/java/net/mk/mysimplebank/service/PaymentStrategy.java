package net.mk.mysimplebank.service;

import net.mk.mysimplebank.anatation.LogExecutionTime;
import net.mk.mysimplebank.entity.BankAccount;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
public interface PaymentStrategy {
    void process(BankAccount account, BigDecimal amount, Map<String, String> details);
}
