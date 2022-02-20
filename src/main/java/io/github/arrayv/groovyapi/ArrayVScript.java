package io.github.arrayv.groovyapi;

import groovy.lang.MissingPropertyException;
import groovy.lang.Script;
import io.github.arrayv.sortdata.SortInfo;

public abstract class ArrayVScript extends Script {
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
