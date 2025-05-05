package net.mk.mysimplebank.service;

import net.mk.mysimplebank.entity.Transaction;
import net.mk.mysimplebank.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

/**
 * Сервис отвечает за управление платежами и переводами
 *
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */

public class TransactionService {
    //реализация данного Set - неизменяемый стрим, не допускающий передачу null значений
    public static final Set<String> TRANSACTION_CATEGORIES = Set.of("Health", "Beauty", "Education");

    public boolean isValidCategory(final String category) {
        return TRANSACTION_CATEGORIES.contains(category);
    }

    public Set<String> isValidCategories(final Set<String> categories) {
        return categories.stream()
                .filter(this::isValidCategory)
                .collect(toCollection(() -> new HashSet<>(categories.size())));
    }

    /**
     * Фильтрует транзакции пользователя с использованием Predicate
     *
     * @param user      - пользователь
     * @param predicate - условие фильтрации
     * @return список транзакций, удовлетворяющих условию
     */
    public List<Transaction> filterTransactions(final User user, final Predicate<Transaction> predicate) {
        Objects.requireNonNull(user, "User must not be null");

        return user.getBankAccounts().stream()
                .flatMap(transaction -> transaction.getTransactions().stream())
                .filter(predicate)
                .toList();
    }

    /**
     * Преобразует транзакции пользователя с использованием Function
     *
     * @param user     - пользователь
     * @param function - функция преобразования
     * @return список строковых представлений транзакций
     */
    public List<String> transformTransactions(final User user, final Function<Transaction, String> function) {
        Objects.requireNonNull(user, "User must not be null");

        return user.getBankAccounts().stream()
                .flatMap(transaction -> transaction.getTransactions().stream())
                .map(function)
                .toList();
    }

    /**
     * Обрабатывает транзакции пользователя с использованием Consumer
     *
     * @param user     - пользователь
     * @param consumer - функция обработки
     */
    public void processTransactions(final User user, final Consumer<Transaction> consumer) {
        Objects.requireNonNull(user, "User must not be null");

        user.getBankAccounts().stream()
                .flatMap(transaction -> transaction.getTransactions().stream())
                .forEach(consumer);
    }

    /**
     * Создаёт список транзакций с использованием Supplier
     *
     * @param supplier - поставщик
     * @return созданный список транзакций
     */
    public List<Transaction> createTransactionList(final Supplier<List<Transaction>> supplier) {
        return supplier.get();
    }

    /**
     * Объединяет два списка транзакций с использованием BiFunction / BinaryOperator
     *
     * @param list1  - первый список транзакций
     * @param list2  - второй список транзакций
     * @param merger - функция объединения списков
     * @return объединённый список транзакций
     */
    public List<Transaction> mergeTransactionLists(final List<Transaction> list1, final List<Transaction> list2,
                                                   final BinaryOperator<List<Transaction>> merger)
    {
        Objects.requireNonNull(list1, "transaction1 must not be null");
        Objects.requireNonNull(list2, "transaction2 must not be null");

        return merger.apply(list1, list2);
    }
}
