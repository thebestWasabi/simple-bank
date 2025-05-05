package net.mk.mysimplebank.service.impl;

import lombok.RequiredArgsConstructor;
import net.mk.mysimplebank.entity.BankAccount;
import net.mk.mysimplebank.anatation.LogExecutionTime;
import net.mk.mysimplebank.entity.Transaction;
import net.mk.mysimplebank.entity.TransactionType;
import net.mk.mysimplebank.entity.User;
import net.mk.mysimplebank.service.AnalyticsServiceInterface;
import net.mk.mysimplebank.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

/**
 * Сервис предоставляет аналитику по операциям пользователей
 *
 * @author Maxim Khamzin
 * @link <a href="https://mkcoder.net">mkcoder.net</a>
 */

@RequiredArgsConstructor
public class AnalyticsService implements AnalyticsServiceInterface {
    private final TransactionService transactionService;

    /**
     * Вывод суммы потраченных средств на категорию за последний месяц
     *
     * @param bankAccount - счет
     * @param category    - категория
     */
    @Override
    @LogExecutionTime
    public BigDecimal getMonthlySpendingByCategory(final BankAccount bankAccount, final String category) {
        if (bankAccount == null || category == null || !transactionService.isValidCategory(category)) {
            return BigDecimal.ZERO;
        }

        final var oneMonthAgo = LocalDateTime.now().minusMonths(1L);

        return bankAccount.getTransactions().stream()
                .filter(transaction ->
                        transaction.getCategory() != null &&
                        transaction.getCategory().equals(category) &&
                        TransactionType.PAYMENT == transaction.getType() &&
                        !transaction.getCreatedDate().isBefore(oneMonthAgo))
                .map(Transaction::getValue)
                .reduce(BigDecimal.ZERO, (amount1, amount2) -> amount1.add(amount2));
    }

    /**
     * Вывод суммы потраченных средств на n категорий за последний месяц
     * со всех счетов пользователя
     *
     * @param user       - пользователь
     * @param categories - категории
     * @return мапа категория - сумма потраченных средств
     */
    @Override
    @LogExecutionTime
    public Map<String, BigDecimal> getMonthlySpendingByCategories(final User user, final Set<String> categories) {
        if (user == null || categories == null || categories.isEmpty()) {
            return new HashMap<>(); // тут надо кидать ошибку, но я тупа возвращаю пустую мапу
        }

        final var validCategories = transactionService.isValidCategories(categories);
        if (validCategories.isEmpty()) {
            return null; // то же самое
        }

        final var oneMonthAgo = LocalDateTime.now().minusMonths(1L);

        return user.getBankAccounts().stream()
                .flatMap(account -> account.getTransactions().stream())
                .filter(transaction ->
                        transaction.getCategory() != null &&
                        categories.contains(transaction.getCategory()) &&
                        TransactionType.PAYMENT == transaction.getType() &&
                        !transaction.getCreatedDate().isBefore(oneMonthAgo))
                .collect(groupingBy(Transaction::getCategory, reducing(BigDecimal.ZERO, Transaction::getValue, BigDecimal::add)));
    }

    /**
     * Вывод платежных операций по всем счетам и по всем категориям от наибольшей к наименьшей
     *
     * @param user - пользователь
     * @return мапа категория - все операции совершенные по ней, отсортированные от наибольшей к наименьшей
     */
    public LinkedHashMap<String, List<Transaction>> getTransactionHistorySortedByAmount(final User user) {
        if (user == null) return new LinkedHashMap<>();

        return user.getBankAccounts().stream()
                .flatMap(account -> account.getTransactions().stream())
                .filter(transaction -> TransactionType.PAYMENT == transaction.getType())
                .sorted(comparing(Transaction::getValue))
                .collect(groupingBy(Transaction::getCategory, LinkedHashMap::new, toList()));
    }


    /**
     * Вывод последних N транзакций пользователя
     *
     * @param user - пользователь
     * @param n    - количество последних транзакций
     * @return List<Transaction>, который содержит последние транзакции пользователя (кол-во равное n)
     */
    public List<Transaction> getLastNTransactions(final User user, final int n) {
        if (user == null) return new ArrayList<>();

        return user.getBankAccounts().stream()
                .flatMap(account -> account.getTransactions().stream())
                .filter(transaction -> TransactionType.PAYMENT == transaction.getType())
                .sorted(comparing(Transaction::getCreatedDate).reversed())
                .limit(n)
                .toList();
    }

    /**
     * Вывод топ-N самых больших платежных транзакций пользователя
     *
     * @param user - пользователь
     * @param n    - количество топовых транзакций
     * @return PriorityQueue, где транзакции хранятся в порядке убывания их значения
     */
    public PriorityQueue<Transaction> getTopNLargestTransactions(final User user, final int n) {
        if (user == null) return new PriorityQueue<>();

        return user.getBankAccounts().stream()
                .flatMap(account -> account.getTransactions().stream())
                .filter(transaction -> TransactionType.PAYMENT == transaction.getType())
                .sorted(comparing(Transaction::getValue).reversed())
                .limit(n)
                .collect(toCollection(() -> new PriorityQueue<>(comparing(Transaction::getValue).reversed())));
    }
}
