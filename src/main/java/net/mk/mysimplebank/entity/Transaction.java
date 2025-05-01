package net.mk.mysimplebank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Информация о совершенной банковской транзакции
 *
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private String id;
    private BigDecimal value;
    private TransactionType type;
    private String category;
    private LocalDateTime createdDate;
}
