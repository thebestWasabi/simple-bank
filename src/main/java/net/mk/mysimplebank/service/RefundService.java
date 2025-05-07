package net.mk.mysimplebank.service;

import lombok.RequiredArgsConstructor;
import net.mk.mysimplebank.entity.Refund;

/**
 * Сервис осуществляющий возвраты по платежам
 *
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
@RequiredArgsConstructor
public class RefundService {
    private final PaymentGatewayService paymentGatewayService = PaymentGatewayService.getInstance();

    public void createRefund(final Refund refund) {
        paymentGatewayService.refund(refund.getDescription(), refund.getAmount());
    }
}
