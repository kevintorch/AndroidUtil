package com.torch.androidutil;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CollectionsUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <E> List<E> emptyOrList(List<E> list) {
        return list == null ? new ArrayList<>() : list;
    }

    public static <T> boolean equalsIgnoreOrdering(Collection<T> collection1,
                                                   Collection<T> collection2) {
        return collection1 != null && collection2 != null
                && (collection1.size() == collection2.size() && collection1.containsAll(
                collection2));
    }

    public static <T> boolean isEquals(Collection<T> collection1, Collection<T> collection2) {
        return collection1 != null && collection2 != null
                && (collection1.size() == collection2.size() && collection1.equals(collection2));
    }

    public static String toStringJoining(Collection<?> collection, String delimiter) {
        if (isEmpty(collection)) {
            return "";
        }
        return collection.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }

    public static <T> String[] toStringArray(Collection<T> collection) {
        if (isEmpty(collection)) return new String[0];
        return collection.stream().map(Objects::toString).toArray(String[]::new);
    }

    public static <T> List<T> removeFirstNElements(int n, List<T> list) {
        List<T> collection = new ArrayList<>();
        if (isEmpty(list)) return collection;

        do {
            collection.add(list.remove(0));
        } while (collection.size() != n && !list.isEmpty());

        return collection;
    }
}
