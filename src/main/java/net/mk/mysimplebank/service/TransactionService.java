package net.mk.mysimplebank.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
//        final Set<String> validCategories = new HashSet<>(categories.size());
//        for (final String category : categories) {
//            if (isValidCategory(category)) {
//                validCategories.add(category);
//            }
//        }

        return categories.stream()
                .filter(this::isValidCategory)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
