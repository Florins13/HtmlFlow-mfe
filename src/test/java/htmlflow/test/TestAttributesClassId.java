/*
 * MIT License
 *
 * Copyright (c) 2015-16, Mikael KROK
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
/**
 *
 */
package htmlflow.test;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;
import htmlflow.test.views.HtmlLists;
import org.junit.Test;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Html;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.logging.Logger;

import static htmlflow.test.Utils.NEWLINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Mikael KROK
 *
 */
public class TestAttributesClassId {

    private static final Logger LOGGER = Logger.getLogger("htmlflow.test");
    private static final String DIV_NAME = Div.class.getSimpleName().toLowerCase();

    @Test
    public void testGetElementName() {
        Div<Body<Html<HtmlView<Object>>>> div = StaticHtml.view().html().body().div();
        assertEquals(DIV_NAME, div.getName());
    }

    @Test
    public void testIdAndClassAttributeStaticHtmlWithConsumer() {
        ByteArrayOutputStream mem = new ByteArrayOutputStream();
        StaticHtml
            .view(new PrintStream(mem), HtmlLists::taskView)
            .write();
        assertHtml(mem.toString());
    }

    @Test
    public void testIdAndClassAttribute() {
        String actual = HtmlLists
            .taskView(StaticHtml.view())
            .render();
        assertHtml(actual);
    }

    @Test
    public void testIdAndClassAttributeInViewWithPrintStream() {
        ByteArrayOutputStream mem = new ByteArrayOutputStream();
        HtmlLists
            .taskView(StaticHtml.view(new PrintStream(mem)))
            .write();
        assertHtml(mem.toString());
    }

    private void assertHtml(String actual){
        // System.out.println(result);
        assertTrue(actual.contains("<div"));
        assertTrue(actual.contains("</div>"));
        assertTrue(actual.contains(HtmlLists.divClass));
        assertTrue(actual.contains(HtmlLists.divId));
        assertTrue(actual.contains("toto=\"tutu\""));
        assertTrue("should contains <script type=\"text/javascript\" src=\"test.css\">",
            actual.contains("<script type=\"text/javascript\" src=\"test.css\">"));

        Iterator<String> iter = NEWLINE.splitAsStream(actual).iterator();
        Utils
            .loadLines("testIdAndClassAttribute.html")
            .forEach(expected -> assertEquals(expected, iter.next()));
    }
}
