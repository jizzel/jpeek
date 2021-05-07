package org.jpeek.skeleton.eo;

import javassist.CtClass;
import org.eolang.EOarray;
import org.eolang.core.EOObject;
import org.eolang.core.data.EOData;

public class EOclass extends EOObject {
    private final EOObject eOclass;
    private final EOObject eOmethods;
    private final EOObject eOattr;

    public EOclass(EOObject className, EOObject eOmethods, EOObject eOattr){
        this.eOclass = className;
        this.eOmethods = eOmethods;
        this.eOattr = eOattr;
    }

    @Override
    public EOData _getData() {
        return eOclass._getData();
    }
}
