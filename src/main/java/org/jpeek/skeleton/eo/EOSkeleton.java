package org.jpeek.skeleton.eo;

import com.jcabi.xml.XML;
import org.eolang.EOarray;
import org.eolang.core.EOObject;
import org.eolang.core.data.EODataObject;
import org.jpeek.calculus.eo.*;

/**
 * Class for getting list of methods and attributes of each class
 */
public final class EOSkeleton {

    /**
     * source class
     */
    private final XML xml;

    public EOSkeleton(final XML xml) {
        this.xml = xml;
    }

    /**
     * @return an {@code EOarray} object containing the classes each with it's lists of methods and it's lists of attributes
     */
    public EOObject getClassFieldsAndMethods(){
        EOObject classObjects = new EOarray();
        for (XML __xml : this.xml.nodes("//class")) {
            String className = this.xml.xpath("//package/@id").get(0) + "." + __xml.xpath("@id").get(0);
            EOarray classAttributes = new EOarray();
//            List of class attributes
            for (XML attr : __xml.nodes("attributes/attribute")) {
                classAttributes.EOappend(new EOatt(new EODataObject(attr.xpath("text()").get(0))));
            }
            EOarray classMethods = new EOarray();
//            list of class methods
            for (XML _xml : __xml.nodes("methods/method")) {
                EOarray methodAttributes = new EOarray();
                for (XML _xml_1 : _xml.nodes("ops")) {
//                    list of fields/attributes used by this method
                    for(XML _xml_2 : _xml_1.nodes("op")){
                        if(!_xml_2.xpath("text()").get(0).trim().equals(""))
                            methodAttributes.EOappend(new EOatt(new EODataObject(_xml_2.xpath("text()").get(0).trim())));
                    }
                }
                classMethods.EOappend(
                        new EOmethod(
                                new EODataObject(_xml.xpath("@name").get(0)),
                                methodAttributes
                        ));

            }
            ((EOarray) classObjects).EOappend(
                    new EOclass(
                            new EODataObject(className),
                            classMethods,
                            classAttributes
                    ));
        }
        return classObjects;
    }


}
