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
                    EOarray methodGetAttributes = new EOarray();
                    EOarray methodPutAttributes = new EOarray();
                    EOarray methodArguments = new EOarray();
                    EOarray methodCalls = new EOarray();
                    String methodName = _xml.xpath("@name").get(0);
                    for (XML _xml_1 : _xml.nodes("args")) {
//                      list of types of arguments
                        for(XML _xml_2 : _xml_1.nodes("arg")){
                            String argType = _xml_2.xpath("@type").get(0);
                            if(!argType.equals("")){
                                methodArguments = methodArguments.EOappend(new EOstring(argType));
                            }

                        }
                    }
                    for (XML _xml_1 : _xml.nodes("ops")) {
//                      list of fields/attributes used by this method
                        // call put get
                        for(XML _xml_2 : _xml_1.nodes("op")){
                            String opCode =  _xml_2.xpath("@code").get(0);
                            if("call".equals(opCode)){
                                String callName = _xml_2.nodes("name").get(0).xpath("text()").get(0);
                                if(callName.startsWith(className + ".")){
                                    callName = callName.replaceFirst(className + ".","");
                                    methodCalls = methodCalls.EOappend(new EOatt(new EOstring(callName)));
                                }
                            }else if("put".equals(opCode)){
                                String attName = _xml_2.xpath("text()").get(0).trim();
                                if(!attName.equals("")){
                                    methodPutAttributes = methodPutAttributes.EOappend(new EOatt(new EOstring(attName)));
                                    methodAttributes = methodAttributes.EOappend(new EOatt(new EOstring(attName)));
                                }
                            }else if ("get".equals(opCode)){
                                String attName = _xml_2.xpath("text()").get(0).trim();
                                if(!attName.equals("")){
                                    methodGetAttributes = methodGetAttributes.EOappend(new EOatt(new EOstring(attName)));
                                    methodAttributes = methodAttributes.EOappend(new EOatt(new EOstring(attName)));
                                }
                            }
                        }
                    }
                    classMethods = classMethods.EOappend(
                            new EOmethod(
                                    new EOstring(methodName),
                                    methodArguments,
                                    methodAttributes,
                                    methodGetAttributes,
                                    methodPutAttributes,
                                    methodCalls
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