package org.jpeek.skeleton;

import javassist.CtClass;
import org.eolang.EOarray;
import org.eolang.core.EOObject;
import org.eolang.core.data.EODataObject;

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
    public EOObject getClassFieldsAndMethods(){
        EOObject[] methods = Arrays.stream(this.srcClass.getDeclaredMethods())
                .map(method -> new EODataObject(method.getName()))
                .toArray(EODataObject[]::new);
        EOObject[] fields = Arrays.stream(this.srcClass.getDeclaredFields())
                .map(field -> new EODataObject(field.getName()))
                .toArray(EODataObject[]::new);


//        [name methods atts] > class
//        [name atts] > method
//        [name] > att
        return new EOarray(
                new EOarray(new EODataObject(this.srcClass.getName())),
                new EOarray(methods),
                new EOarray(fields));
    }


}
