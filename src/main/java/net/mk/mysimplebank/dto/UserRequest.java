package net.mk.mysimplebank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */

@Data
@AllArgsConstructor
public class UserRequest {
    private int accountId;
    private BigDecimal amount;
    private String paymentType;
    private Map<String, String> paymentDetails;
}
