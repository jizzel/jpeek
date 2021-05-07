package org.jpeek.calculus.eo;

import com.jcabi.xml.*;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.FormattedText;
import org.cactoos.text.TextOf;
import org.eolang.core.EOObject;
import org.eolang.core.data.EODataObject;
import org.jpeek.Header;
import org.jpeek.calculus.Calculus;
import org.jpeek.skeleton.eo.EOattr;
import org.jpeek.skeleton.eo.EOmethod;
import org.xembly.Directives;
import org.xembly.Xembler;

import java.io.IOException;
import java.util.Map;

public class EOCalculus implements Calculus {
    @Override
    public XML node(String metric, Map<String, Object> params, XML skeleton) throws IOException {
        System.out.println("==== EO code execution ====");
        EOObject obj = new org.jpeek.calculus.eo.EOtest(new EODataObject("Hello from EO"));
        obj._getData();
        System.out.println("==== EO code execution ====");
        if("EO_LCOM".equals(metric)) metric = "LCOM";
        XML res = new XSLDocument(
                new TextOf(
                        new ResourceOf(
                                new FormattedText("org/jpeek/metrics/%s.xsl", metric)
                        )
                ).asString(),
                Sources.DUMMY,
                params
        ).transform(skeleton);
        final XML res2 =
                new XMLDocument(
                        new Xembler(
                                new Directives()
                                        .add("metric")
                                        .append(new Header())
                                        .append(
                                                () -> new Directives()
                                                        .attr("schema", "xsd/skeleton.xsd")
                                                        .iterator()
                                        )
                                        /*.append(
                                                () -> new Directives()
                                                .attr("xsi:noNamespaceSchemaLocation", "xsd/metric.xsd")
                                                .iterator())*/
                                        .add("title")
                                        .set(metric)
                                        .up()
                                        .add("description")
                                        .set("Metric description")
                                        .up()
                                        .add("app")
                                        .attr("id", skeleton.xpath("//app/@id").get(0))
                                        /*.append(
                                                new Joined<>(
                                                        new Mapped<>(
                                                                ent -> new Directives()
                                                                        .add("package")
                                                                        .attr("id", ent.getKey())
                                                                        .append(ent.getValue())
                                                                        .up(),
                                                                skeleton.getP
                                                        )
                                                )
                                        )*/
                        ).xmlQuietly()
                );
        System.out.println(res2.toString());
        return res;
    }
}
