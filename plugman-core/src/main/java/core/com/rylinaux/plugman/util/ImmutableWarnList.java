package core.com.rylinaux.plugman.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ImmutableWarnList<T> extends ArrayList<T> {
    public ImmutableWarnList(Collection<? extends T> collection) {
        super(collection);
    }

    private static void warn() {
        throw new UnsupportedOperationException("This list is immutable. Please use appropriate API, if you wish to modify this list.");
    }

    @Override
    public T set(int index, T element) {
        warn();
        return null;
    }

    @Override
    public void clear() {
        warn();
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        warn();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        warn();
        return false;
    }

    @Override
    public void add(int index, T element) {
        warn();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        warn();
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        warn();
        return false;
    }

    @Override
    public void addFirst(T element) {
        warn();
    }

    @Override
    public void addLast(T element) {
        warn();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        warn();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        warn();
        return false;
    }

    @Override
    public T removeFirst() {
        warn();
        return null;
    }

    @Override
    public boolean remove(Object o) {
        warn();
        return false;
    }

    @Override
    public T remove(int index) {
        warn();
        return null;
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        warn();
        return false;
    }

    @Override
    public T removeLast() {
        warn();
        return null;
    }

    @Override
    public boolean add(T ignored) {
        warn();
        return false;
    }
}
