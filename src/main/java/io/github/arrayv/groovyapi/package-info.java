/**
 * <p>This package defines the Groovy API.
 * This package contains both things intended to be used from Groovy
 * scripts and things intended to be used for the usage of Groovy scripts.</p>
 *
 * <p>All the classes in this package are automatically imported from Groovy scripts.</p>
 *
 * <p>The contents of this package is summarized here:</p>
 * <ul>
 *   <li>{@link GroovyLocals} &mdash; The contents of this is imported statically into all Groovy scripts.</li>
 *   <li>{@link ScriptManager} &mdash; This is primarily a Java-facing API intended for loading Groovy scripts.</li>
 *   <li>{@link RunSortBuilder} &mdash; This is used for running sorting algorithms. Instances can be created through
 *       any of the {@code run} methods of {@link GroovyLocals}.</li>
 *   <li>{@link ArrayVScript} &mdash; This is the base class for all Groovy scripts in ArrayV. It is primarily used
 *       for accessing sorts directly using their internal name (instead of having to use
 *       {@link GroovyLocals#getSort(String)}), although it may be extended in the future.</li>
 *   <li>{@link ArrayVEventHandler} &mdash; This is used for registering event handlers with the Groovy API's event
 *       system. The event system doesn't have that much content yet, though.</li>
 *   <li>{@link RunGroupContext} &mdash; This class is used for holding run group thread local information.</li>
 * </ul>
 */
package io.github.arrayv.groovyapi;
