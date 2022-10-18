package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */

public record CedarInstanceListNode(@JsonIgnore List<CedarInstanceNode> elements) implements CedarInstanceNode, List<CedarInstanceNode> {

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    @Override
    public Iterator<CedarInstanceNode> iterator() {
        return elements.iterator();
    }

    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return elements.toArray(a);
    }

    @Override
    public boolean add(CedarInstanceNode cedarInstanceNode) {
        return elements.add(cedarInstanceNode);
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends CedarInstanceNode> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends CedarInstanceNode> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public CedarInstanceNode get(int index) {
        return elements.get(index);
    }

    @Override
    public CedarInstanceNode set(int index, CedarInstanceNode element) {
        return elements.set(index, element);
    }

    @Override
    public void add(int index, CedarInstanceNode element) {

    }

    @Override
    public CedarInstanceNode remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<CedarInstanceNode> listIterator() {
        return elements.listIterator();
    }

    @Override
    public ListIterator<CedarInstanceNode> listIterator(int index) {
        return elements.listIterator(index);
    }

    @Override
    public List<CedarInstanceNode> subList(int fromIndex, int toIndex) {
        return elements.subList(fromIndex, toIndex);
    }
}
