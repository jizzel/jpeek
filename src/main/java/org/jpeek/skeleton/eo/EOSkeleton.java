package org.jpeek.skeleton.eo;

import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import org.eolang.EOarray;
import org.eolang.core.EOObject;
import org.eolang.core.data.EODataObject;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Class for getting list of methods and attributes of each class
 */
public final class EOSkeleton {

    /**
     * source class
     */
    private final CtClass srcClass;

    public EOSkeleton(final CtClass srcClass) {
        this.srcClass = srcClass;
    }

    /**
     * @return an {@code EOarray} object containing the class, the lists of methods and the lists of attributes
     */
    public EOclass getClassFieldsAndMethods(){
        EOObject[] methods = Arrays.stream(this.srcClass.getDeclaredMethods())
                .map(method -> new EOmethod(
                        new EODataObject(method.getName()),
                        new EODataObject("someAttribute")
                ))
                .toArray(EOObject[]::new);
        EOObject[] attr = Arrays.stream(this.srcClass.getDeclaredFields())
                .map(field -> new EODataObject(field.getName()))
                .toArray(EOObject[]::new);
        return new EOclass(
                new EODataObject(this.srcClass.getName()),
                new EOarray(methods),
                new EOarray(attr)
        );
    }


}
