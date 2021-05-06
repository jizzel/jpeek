package org.jpeek.skeleton.eo;

import org.eolang.EOarray;
import org.eolang.core.EOObject;

public class EOmethods extends EOObject {
    private final EOObject eOmethods;
    private final EOarray eOattr;

    public EOmethods(EOObject methodName, EOarray eOattr){
        this.eOmethods = methodName;
        this.eOattr = eOattr;
    }
}
