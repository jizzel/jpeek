package org.jpeek.skeleton.eo;

import org.eolang.core.EOObject;

public class EOattr extends EOObject {

    private final EOObject[] eoObject;

    public EOattr(EOObject... eoObject){
        this.eoObject = eoObject;
    }
}
