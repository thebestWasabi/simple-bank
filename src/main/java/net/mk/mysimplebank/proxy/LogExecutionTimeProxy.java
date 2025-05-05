package net.mk.mysimplebank.proxy;

import net.mk.mysimplebank.anatation.LogExecutionTime;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Класс-утилита для создания прокси-объектов, которые измеряют
 * время выполнения методов, аннотированных {@link LogExecutionTime}.
 * <p>
 * Использует JDK Dynamic Proxy и требует, чтобы целевой объект реализовывал интерфейс.
 *
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */
public class LogExecutionTimeProxy {

    /**
     * Создаёт прокси-объект, который оборачивает вызовы методов целевого объекта.
     * Если метод аннотирован {@link LogExecutionTime}, логируется время его выполнения.
     *
     * @param target         целевой объект, реализующий интерфейс
     * @param interfaceClass интерфейс, который реализует target
     * @param <T>            тип интерфейса
     * @return прокси-объект, реализующий тот же интерфейс и добавляющий логирование времени выполнения
     * @throws IllegalArgumentException если interfaceClass не является интерфейсом
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final T target, final Class<T> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("interfaceClass должен быть интерфейсом");
        }

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new ExecutionTimeHandler(target)
        );
    }

    /**
     * Внутренний класс-обработчик вызовов методов.
     * Если метод помечен аннотацией {@link LogExecutionTime}, логирует время выполнения.
     */
    private static class ExecutionTimeHandler implements InvocationHandler {
        private final Object target;

        public ExecutionTimeHandler(final Object target) {
            this.target = target;
        }

        /**
         * Перехватывает вызовы методов интерфейса и, если необходимо, логирует время их выполнения.
         *
         * @param proxy  прокси-объект
         * @param method метод интерфейса, который вызывается
         * @param args   аргументы метода
         * @return результат выполнения метода
         * @throws Throwable любые исключения, выброшенные целевым методом
         */
        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            // Получаем реальный метод из класса-реализации
            Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());

            if (targetMethod.isAnnotationPresent(LogExecutionTime.class)) {
                final var start = System.currentTimeMillis();
                System.out.println(method.getName() + " started");

                // Вызываем метод через отражение
                final var result = targetMethod.invoke(target, args);

                final var duration = System.currentTimeMillis() - start;
                System.out.println(method.getName() + " finished, duration: " + duration + " ms");

                return result;
            }

            // Если аннотации нет — просто вызываем метод
            return targetMethod.invoke(target, args);
        }
    }
}
