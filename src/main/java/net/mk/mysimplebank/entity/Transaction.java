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
    private String merchantName;         // Название магазина или поставщика услуг
    private String merchantCategoryCode; // MCC-код продавца
    private String cardNumber;           // Последние 4 цифры номера карты (при оплате картой)
    private String bankName;             // Название банка (при банковском переводе)
    private String digitalWalletId;      // Идентификатор электронного кошелька (при оплате через электронные кошельки)
}
