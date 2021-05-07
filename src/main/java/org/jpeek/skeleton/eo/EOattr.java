package org.jpeek.skeleton.eo;

import org.eolang.core.EOObject;
import org.eolang.core.data.EOData;

public class EOattr extends EOObject {

    private final EOObject eoObject;

    public EOattr(EOObject eoObject){
        this.eoObject = eoObject;
    }

    @Override
    public EOData _getData() {
        return eoObject._getData();
    }
}
