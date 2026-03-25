/**
 * The MIT License
 *
 * Copyright (C) 2025 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapisixtynine.markdownslugger.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.astrapisixtynine.markdownslugger.core.MarkdownContext;

/**
 * Parameterized unit tests for {@link AnchorIdInjector}
 */
class AnchorIdInjectorTest
{

	/**
	 * Provides test cases: heading line + slug → expected output line containing {#slug}
	 */
	static Stream<Arguments> provideHeadingAnchorCases()
	{
		return Stream.of(Arguments.of("# Title", "title", "# Title {#title}"),
			Arguments.of("## Getting Started", "getting-started",
				"## Getting Started {#getting-started}"),
			Arguments.of("### Installation Steps", "installation-steps",
				"### Installation Steps {#installation-steps}"),
			Arguments.of("#### Configuration", "configuration",
				"#### Configuration {#configuration}"),
			Arguments.of("##### Advanced Usage", "advanced-usage",
				"##### Advanced Usage {#advanced-usage}"),
			Arguments.of("###### Reference", "reference", "###### Reference {#reference}"),
			// Heading with numbers
			Arguments.of("## Chapter 3", "chapter-3", "## Chapter 3 {#chapter-3}"),
			// Heading with accented characters (text preserved, slug is already normalized)
			Arguments.of("## Über den Wolken", "uber-den-wolken",
				"## Über den Wolken {#uber-den-wolken}"));
	}

	@ParameterizedTest(name = "[{index}] \"{0}\" + slug=\"{1}\" \u2192 \"{2}\"")
	@MethodSource("provideHeadingAnchorCases")
	void testAnchorInjectedIntoHeading(String headingLine, String slug, String expectedLine)
	{
		MarkdownContext context = new MarkdownContext();
		context.setOriginalContent(headingLine);
		// HeadingExtractor strips "## " prefix → extract the text part
		String headingText = headingLine.replaceFirst("^#+\\s+", "");
		context.getHeadings().add(headingText);
		context.getSlugs().add(slug);

		new AnchorIdInjector().process(context);

		String result = context.getOriginalContent().trim();
		assertEquals(expectedLine, result, "Expected anchor injected into heading: " + headingLine);
	}

	/**
	 * Lines that are not headings must pass through unmodified
	 */
	@ParameterizedTest(name = "[{index}] non-heading line unchanged: \"{0}\"")
	@ValueSource(strings = { "Plain text", "- list item", "> blockquote", "Some paragraph text.",
			"" })
	void testNonHeadingLinesAreNotModified(String line)
	{
		MarkdownContext context = new MarkdownContext();
		context.setOriginalContent(line);
		// No headings/slugs registered → injector has nothing to inject

		new AnchorIdInjector().process(context);

		// AnchorIdInjector appends \n to every line; strip only the trailing newline
		assertEquals(line, context.getOriginalContent().stripTrailing());
	}

	/**
	 * Verifies that anchor IDs are injected for all headings in a multi-heading document, and that
	 * non-heading lines remain unchanged
	 */
	static Stream<Arguments> provideMultiHeadingDocuments()
	{
		return Stream.of(
			Arguments.of("# A\nSome text.\n## B\n", new String[] { "A", "B" },
				new String[] { "a", "b" }, "# A {#a}", "## B {#b}", "Some text."),
			Arguments.of("# One\n# Two\n# Three\n", new String[] { "One", "Two", "Three" },
				new String[] { "one", "two", "three" }, "# One {#one}", "# Two {#two}",
				"# Three {#three}"));
	}

	@ParameterizedTest(name = "[{index}] multi-heading anchor injection")
	@MethodSource("provideMultiHeadingDocuments")
	void testAllHeadingsGetAnchors(String markdown, String[] headings, String[] slugs,
		String firstExpected, String secondExpected, String unchangedLine)
	{
		MarkdownContext context = new MarkdownContext();
		context.setOriginalContent(markdown);
		for (String h : headings)
			context.getHeadings().add(h);
		for (String s : slugs)
			context.getSlugs().add(s);

		new AnchorIdInjector().process(context);

		String result = context.getOriginalContent();
		assertTrue(result.contains(firstExpected),
			"Expected first anchor: " + firstExpected + "\nActual:\n" + result);
		assertTrue(result.contains(secondExpected),
			"Expected second anchor: " + secondExpected + "\nActual:\n" + result);
		assertTrue(result.contains(unchangedLine),
			"Unchanged line should still be present: " + unchangedLine);
		assertFalse(result.contains("{#" + slugs[0] + "} {#"),
			"No double-anchor should be injected");
	}
}
