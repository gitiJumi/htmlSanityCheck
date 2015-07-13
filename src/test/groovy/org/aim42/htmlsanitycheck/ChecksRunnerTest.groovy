package org.aim42.htmlsanitycheck

import org.aim42.htmlsanitycheck.check.BrokenCrossReferencesChecker
import org.aim42.htmlsanitycheck.collect.SinglePageResults
import org.junit.Before
import org.junit.Test

class ChecksRunnerTest extends GroovyTestCase {

    final static String HTML_HEAD = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"> <head></head><html>'

    private File fileToTest

    private ChecksRunner checksRunner

    @Before
    void setup() {

    }


    @Test
    public void testSingleCorrectHTMLFile() {
        String HTML = """$HTML_HEAD<body><title>hsc</title></body></html>"""

        // create file with proper html content
        fileToTest = File.createTempFile("testfile", ".html") << HTML

        // wrap fileToTest in Collection to comply to AllChecksRunner API
        checksRunner = new ChecksRunner(
                BrokenCrossReferencesChecker.class,
                fileToTest,
                fileToTest.getParentFile())

        SinglePageResults pageResults = checksRunner.performChecksForOneFile(fileToTest)

        // expectation:
        // 4 checks run
        // 0 items checked
        // 0 findings
        // title = "hsc"
        int expected = 5
        assertEquals("expected $expected kinds of checks", expected, pageResults.singleCheckResults.size())

        assertEquals("expected 0 items checked", 0, pageResults.nrOfItemsCheckedOnPage())

        assertEquals("expected 0 findings", 0, pageResults.nrOfFindingsOnPage())

        assertEquals("expected hsc title", "hsc", pageResults.pageTitle)

        String expectedFileName  = fileToTest.name
        assertEquals("expected $expectedFileName as fileName", expectedFileName, pageResults.pageFileName)
    }


    @Test
    public void testSingleBrokenHtmlFile() {
        String HTML = """$HTML_HEAD<body><title>Faulty Dragon</title></body>
                   <h1 id="aim42">dummy-heading-1</h1>
                   <h2 id="aim42">duplicate id</h2>
                   <a href="#nonexisting">broken cross reference</a>
                </html>"""

        // create file
        fileToTest = File.createTempFile("testfile", ".html") << HTML

        checksRunner = new ChecksRunner(
                BrokenCrossReferencesChecker.class,
                fileToTest,
                fileToTest.getParentFile())

        SinglePageResults pageResults = checksRunner.performChecksForOneFile(fileToTest)

        int expected = 5
        assertEquals("expected $expected kinds of checks", expected, pageResults.singleCheckResults.size())

        assertEquals("expected 2 findings", 2, pageResults.nrOfFindingsOnPage())

    }

}

/************************************************************************
 * This is free software - without ANY guarantee!
 *
 *
 * Copyright 2014, Dr. Gernot Starke, arc42.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *********************************************************************** */



