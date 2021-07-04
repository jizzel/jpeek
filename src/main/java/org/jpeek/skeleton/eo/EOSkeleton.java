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
                    EOarray methodAttributesGet = new EOarray();
                    EOarray methodAttributesPut = new EOarray();
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
                    HashMap<String, HashSet<String>> attributes = new HashMap<String, HashSet<String>>();
                    HashSet<String> calls = new HashSet<String>();
                    for (XML _xml_1 : _xml.nodes("ops")) {
//                      list of fields/attributes used by this method
                        for(XML _xml_2 : _xml_1.nodes("op")){
                            String opCode =  _xml_2.xpath("@code").get(0);
                            if("call".equals(opCode)){
                                String callName = _xml_2.nodes("name").get(0).xpath("text()").get(0);
                                if(callName.startsWith(className + ".")){
                                    callName = callName.replaceFirst(className + ".","");
                                    if(!callName.contains(".")){
                                        for(XML _xml_3 : _xml_2.nodes("args").get(0).nodes("arg")){
                                            String argType = _xml_3.xpath("@type").get(0);
                                            if(!"".equals(argType))  callName = callName + "|" + argType;
                                        }
                                        calls.add(callName);
                                    }
                                }
                            }else{
                                String attName = _xml_2.xpath("text()").get(0).trim();
                                if(!attName.equals("")){
                                    HashSet<String> attOps = attributes.getOrDefault(attName, new HashSet<String>());
                                    attOps.add(opCode);
                                    attributes.putIfAbsent(attName, attOps);
                                }
                            }
                        }
                    }
                    for (String callName : calls) {
                        methodCalls = methodCalls.EOappend(new EOatt(new EOstring(callName)));
                    }

                    for (Map.Entry<String, HashSet<String>> attribute : attributes.entrySet()) {
                        String attName = attribute.getKey();
                        attName = attName.replaceFirst(className + ".","");
                        if(!attName.contains(".")){
                            boolean includeAtt = false;
                            for (String opCode: attribute.getValue()){
                                if("get".equals(opCode) || "get_static".equals(opCode)) {
                                    methodAttributesGet = methodAttributesGet.EOappend(new EOatt(new EOstring(attName)));
                                    includeAtt = true;
                                }
                                if("put".equals(opCode) || "put_static".equals(opCode)) {
                                    methodAttributesPut = methodAttributesPut.EOappend(new EOatt(new EOstring(attName)));
                                    includeAtt = true;
                                }
                            }
                            if(includeAtt) methodAttributes = methodAttributes.EOappend(new EOatt(new EOstring(attName)));
                        }

                    }
                    classMethods = classMethods.EOappend(
                            new EOmethod(
                                    new EOstring(methodName),
                                    methodArguments,
                                    methodAttributes,
                                    methodAttributesGet,
                                    methodAttributesPut,
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