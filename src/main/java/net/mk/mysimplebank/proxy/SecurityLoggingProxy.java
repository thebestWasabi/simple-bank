package net.mk.mysimplebank.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;

/**
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
public class SecurityLoggingProxy {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final T target, final Class<T> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("interfaceClass должен быть интерфейсом");
        }

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new SecurityInvocationHandler(target)
        );
    }

    public static class SecurityInvocationHandler implements InvocationHandler {
        private final Object target;
        private final Random random = new Random();

        public SecurityInvocationHandler(final Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            System.out.println("Проверка доступа для выполнения операции...");

            final boolean accessGranted = random.nextBoolean();

            if (accessGranted) {
                System.out.println("Доступ разрешён. Выполняем операцию...");
                return method.invoke(target, args);
            }
            else {
                System.out.println("Доступ запрещён. Операция отменена");
                return null;
            }
        }
    }
}
