package io.github.arrayv.sortdata;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.function.Supplier;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class NewSortInstance implements Supplier<Sort> {
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.publicLookup();
    private static final MethodType CONSTRUCTOR_TYPE = MethodType.methodType(void.class, ArrayVisualizer.class);

    private final MethodHandle mh;

    public NewSortInstance(MethodHandle mh) {
        this.mh = mh;
    }

    public NewSortInstance(Constructor<? extends Sort> constructor) throws IllegalAccessException {
        this(LOOKUP.unreflectConstructor(constructor));
    }

    public NewSortInstance(Class<? extends Sort> sortClass) throws NoSuchMethodException, IllegalAccessException {
        this(getMh(sortClass));
    }

    public NewSortInstance(Sort sort) throws IllegalAccessException, NoSuchMethodException {
        this(sort.getClass());
    }

    private static MethodHandle getMh(Class<? extends Sort> sortClass) throws NoSuchMethodException, IllegalAccessException {
        return LOOKUP.findConstructor(sortClass, CONSTRUCTOR_TYPE);
    }

    public MethodHandle getConstructorHandle() {
        return mh;
    }

    @Override
    public Sort get() {
        try {
            return (Sort)mh.invoke(ArrayVisualizer.getInstance());
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
