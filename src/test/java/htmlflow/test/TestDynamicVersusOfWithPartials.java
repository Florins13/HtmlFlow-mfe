/*
 * MIT License
 *
 * Copyright (c) 2014-18, mcarvalho (gamboa.pt)
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

package htmlflow.test;

import htmlflow.HtmlFlow;
import htmlflow.HtmlPage;
import htmlflow.HtmlView;
import htmlflow.test.model.Stock;
import htmlflow.test.views.HtmlDynamic;
import org.junit.Test;
import org.xmlet.htmlapifaster.Div;

import java.util.Iterator;

import static htmlflow.test.Utils.NEWLINE;
import static org.junit.Assert.assertEquals;

public class TestDynamicVersusOfWithPartials {

    @Test
    public void testRightDynamicWithTwoDifferentModels(){
        /*
         * First render with Stock.dummy3Items()
         */
        String actual = HtmlDynamic
            .stocksViewOk
            .render(Stock.dummy3Items());
        assertLines("stocks3items.html", actual);
        /*
         * Then render with Stock.dummy5Items()
         */
        actual = HtmlDynamic
            .stocksViewOk
            .render(Stock.dummy5Items());
        assertLines("stocks5items.html", actual);
    }

    private static void assertLines(String pathToExpected, String actual) {
        Iterator<String> iter = NEWLINE
            .splitAsStream(actual)
            .map(String::toLowerCase)
            .iterator();
        Utils
            .loadLines(pathToExpected)
            .map(String::toLowerCase)
            .forEach(expected -> {
                String line = iter.next();
                assertEquals(expected, line);
            });
    }

    /**
     * Main root HtmlView element cannot use dynamic.
     * It should be used with inner element such as Div.
     * Otherwise, it fails after preprocessing when it tries to clone the
     * Continuation and its Element that is being instantiated through reflection.
     * HtmlView as a different constructor and instantiated differently.
     */
    @Test(expected = ExceptionInInitializerError.class)
    public void testFragmentWithMainRootView() {
        HtmlLists.view.render(new HtmlLists.AModel());
    }
}

class HtmlLists {
    public static HtmlView view = HtmlFlow.view(HtmlLists::taskDetailsTemplate);

    public static class AModel {}

    public static void taskDetailsTemplate(HtmlPage view) {
        view
                .<AModel>dynamic((v, mod) -> v
                        .div()
                        .of(ignore -> HtmlLists.taskDetailsTemplateFregment(v.div()))
                        .__()
                );
    }

    public static void taskDetailsTemplateFregment(Div<?> view ) {
        view.p().text("test").__();
    }

}
