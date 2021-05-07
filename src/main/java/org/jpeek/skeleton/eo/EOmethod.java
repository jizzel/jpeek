package org.jpeek.skeleton.eo;

import org.eolang.EOarray;
import org.eolang.core.EOObject;
import org.eolang.core.data.EOData;

public class EOmethod extends EOObject {
    private final EOObject eOmethod;
    private final EOattr eOattr;

    public EOmethod(EOObject methodName, EOattr eOattr){
        this.eOmethod = methodName;
        this.eOattr = eOattr;
    }

    @Override
    public EOData _getData() {
        return eOmethod._getData();
    }
}
