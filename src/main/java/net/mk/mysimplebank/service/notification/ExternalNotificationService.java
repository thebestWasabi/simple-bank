package net.mk.mysimplebank.service.notification;

/**
 * Внешний API уведомлений (эмулирует внешний сервис)
 *
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
public class ExternalNotificationService {
    public void sendSms(String phone, String msg) {
        System.out.println("Отправка SMS на " + phone + ": " + msg);
    }

    public void sendEmail(String email, String subject, String body) {
        System.out.println("Отправка Email на " + email + ": " + subject + " - " + body);
    }
}
