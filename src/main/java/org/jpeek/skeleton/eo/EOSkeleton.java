package org.jpeek.skeleton.eo;

import com.jcabi.xml.XML;
import org.eolang.EOarray;
import org.eolang.EOstring;
import org.eolang.core.EOObject;
import org.jpeek.calculus.eo.EOatt;
import org.jpeek.calculus.eo.EOmethod;
import org.jpeek.calculus.eo.EOclass;


import java.util.*;


/**
 * Class for getting list of methods and attributes of each class
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
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
    public List<EOObject> getClassFieldsAndMethods(){
        List<EOObject> classObjects = new ArrayList<EOObject>();
        for (XML __xml_package : this.xml.nodes("//package")) {
            String packageName = __xml_package.xpath("@id").get(0);
            for (XML  __xml_class : __xml_package.nodes("//package[@id='"+packageName+"']//class")){
                String className = packageName + "." + __xml_class.xpath("@id").get(0);
                EOarray classAttributes = new EOarray();
//              List of class attributes
                for (XML attr : __xml_class.nodes("attributes/attribute")) {
                    String attName = attr.xpath("text()").get(0);
                    classAttributes = classAttributes.EOappend(new EOatt(new EOstring(attName)));
                }

                EOarray classMethods = new EOarray();

//              list of class methods
                for (XML _xml : __xml_class.nodes("methods/method")) {
                    EOarray methodAttributes = new EOarray();
                    String methodName = _xml.xpath("@name").get(0);
                    for (XML _xml_1 : _xml.nodes("ops")) {
//                      list of fields/attributes used by this method
                        for(XML _xml_2 : _xml_1.nodes("op")){
                            String attName = _xml_2.xpath("text()").get(0).trim();
                            if(!attName.equals("")){
                                methodAttributes = methodAttributes.EOappend(new EOatt(new EOstring(attName)));
                            }

                        }
                    }


                    classMethods = classMethods.EOappend(
                            new EOmethod(
                                    new EOstring(methodName),
                                    methodAttributes
                            ));

                }
                classObjects.add(new EOclass(
                        new EOstring(className),
                        classMethods,
                        classAttributes
                ));
            }
        }
        return classObjects;
    }


}