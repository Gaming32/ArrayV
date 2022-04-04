package io.github.arrayv.groovyapi;

import groovy.lang.MissingPropertyException;
import groovy.lang.Script;
import io.github.arrayv.sortdata.SortInfo;

/**
 * This is the base class for all Groovy scripts in ArrayV. It is primarily used for accessing sorts
 * directly using their internal name, instead of having to use {@link GroovyLocals#getSort(String)}.
 * This class may be extended in the future.
 */
public abstract class ArrayVScript extends Script {
    /**
     * Accesses sorts by internal name when the specified property isn't found.
     */
    @Override
    public Object getProperty(String property) {
        try {
            return super.getProperty(property);
        } catch (MissingPropertyException mpe) {
            SortInfo sort = GroovyLocals.getSort(property);
            if (sort == null) {
                throw new MissingPropertyException(property, getClass());
            }
            return sort;
        }
    }
}
