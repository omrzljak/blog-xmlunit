package com.osmanmrzljak.blog.xmltest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.xpath.JAXPXPathEngine;

/**
 * blogger - todo
 *
 * @author Osman Mrzljak
 * @since 23-02-2016
 */
public class CompareTest {

    private JAXPXPathEngine engine = new JAXPXPathEngine();

    public static final String WILDCARD_TOKEN = "*?*";

    /**
     * Difference evaluator that supports wildcard node value.
     */
    public static final DifferenceEvaluator WILD_CARD =
            (comparison, outcome) -> {
                if (outcome == ComparisonResult.EQUAL)
                    return outcome; // only evaluate differences.
                final Node controlNode = comparison.getControlDetails().getTarget();
                if (controlNode != null && WILDCARD_TOKEN.equals(controlNode.getNodeValue())) {
                    return ComparisonResult.EQUAL; // will evaluate this difference as equal
                }
                return outcome;
            };

    @Before
    public void setUp() {
        Map<String, String> prefix2Uri = new HashMap<>();
        prefix2Uri.put("osm", "http://osmanmrzljak.com/blog/xml");
        engine.setNamespaceContext(prefix2Uri);
    }

    @Test
    public void testCompareWithDifferences() {
        String xpath = "//osm:Sample/osm:SomeElement/osm:Contracting";
        // obtain a part of xml which will be compared with snippetXML.xml
        Node lastNode = getXMLPart(xpath);

        Diff myDiff = DiffBuilder.compare(Input.fromFile(getFile("snippetXML.xml")))
                .withTest(Input.fromNode(lastNode))
                .ignoreComments()
                .ignoreWhitespace()
                .withDifferenceEvaluator(DifferenceEvaluators.first(DifferenceEvaluators.Default, WILD_CARD))
                .build();

        // should not be the same, because NextReneval elements are not equal.
        Assert.assertTrue(myDiff.toString(), myDiff.hasDifferences());
    }

    @Test
    public void testCompareWithWildCard() {
        String xpath = "//osm:Sample/osm:SomeElement/osm:Contracting";
        // obtain a part of xml which will be compared with snippetXML_withWildcard.xml
        Node lastNode = getXMLPart(xpath);

        Diff myDiff = DiffBuilder.compare(Input.fromFile(getFile("snippetXML_withWildcard.xml")))
                .withTest(Input.fromNode(lastNode))
                .ignoreComments()
                .ignoreWhitespace()
                .withDifferenceEvaluator(DifferenceEvaluators.first(DifferenceEvaluators.Default, WILD_CARD))
                .build();
        // Should not contain differences, because NextReneval value contains wildcard *?*
        Assert.assertFalse(myDiff.toString(), myDiff.hasDifferences());
    }

    private File getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());

    }

    private Node getXMLPart(String xpath) {
        Node lastNode = null;
        Source source = Input.fromFile(getFile("sampleXML.xml")).build();
        int count = 0;

        for (Node node : engine.selectNodes(xpath, source)) {
            lastNode = node;
            count++;
        }
        // ensure we found exactly one node.
        Assert.assertEquals("xpath expression must select only 1 XML Node (Element)", count, 1);
        return lastNode;
    }
}
