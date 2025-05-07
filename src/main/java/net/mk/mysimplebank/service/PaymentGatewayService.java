package net.mk.mysimplebank.service;

import java.math.BigDecimal;

/**
 * Внешняя платежная система
 *
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
public class PaymentGatewayService {
    // Приватное статическое поле с единственным экземпляром
    private static final PaymentGatewayService INSTANCE = new PaymentGatewayService();

    // Приватный конструктор
    private PaymentGatewayService() {
        System.out.println("Создано новое подключение к платёжной системе...");
    }

    // Публичный метод для доступа к экземпляру
    public static PaymentGatewayService getInstance() {
        return INSTANCE;
    }

    public boolean authorize(final String txId, final BigDecimal amount) {
        //здесь эмуляция вызова внешней платежной системы
        System.out.println("Авторизация транзакции " + txId + " на сумму " + amount);
        return true;
    }

    public boolean refund(final String txId, final BigDecimal amount) {
        //здесь эмуляция вызова внешней платежной системы
        System.out.println("Осуществление возврата " + txId+ " на сумму " + amount);
        return true;
    }
}
