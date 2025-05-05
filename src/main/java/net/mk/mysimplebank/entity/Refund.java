package net.mk.mysimplebank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Возврат по транзакции
 *
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
@Data
@AllArgsConstructor
public class Refund {
    private int id;
    private BigDecimal amount;
    private String description;
    private int transactionId;
}
