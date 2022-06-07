package com.torch.androidutil.java.iterator;

import java.util.Iterator;

public class ArrayIterator<E> implements Iterator<E> {

    private final E[] array;
    private int position = 0;

    public ArrayIterator(E[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return position < array.length && array[position] != null;
    }

    @Override
    public E next() {
        E element = array[position];
        position++;
        return element;
    }
}
