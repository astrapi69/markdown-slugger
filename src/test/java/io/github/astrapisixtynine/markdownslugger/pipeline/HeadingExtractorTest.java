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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.astrapisixtynine.markdownslugger.core.MarkdownContext;

/**
 * Parameterized unit tests for {@link HeadingExtractor}
 */
class HeadingExtractorTest
{

	/**
	 * Provides test cases: single heading line → expected level and expected heading text
	 */
	static Stream<Arguments> provideHeadingTestCases()
	{
		return Stream.of(
			// All six heading levels
			Arguments.of("# Heading 1", 1, "Heading 1"),
			Arguments.of("## Heading 2", 2, "Heading 2"),
			Arguments.of("### Heading 3", 3, "Heading 3"),
			Arguments.of("#### Heading 4", 4, "Heading 4"),
			Arguments.of("##### Heading 5", 5, "Heading 5"),
			Arguments.of("###### Heading 6", 6, "Heading 6"),
			// Heading text with special characters and numbers
			Arguments.of("## Introduction to v2.0", 2, "Introduction to v2.0"),
			Arguments.of("### Was ist Künstliche Intelligenz?", 3,
				"Was ist Künstliche Intelligenz?"),
			Arguments.of("# 42 — The Answer", 1, "42 — The Answer"),
			// Leading whitespace in content (trimmed before extraction)
			Arguments.of("  ## Indented Heading", 2, "Indented Heading"));
	}

	@ParameterizedTest(name = "[{index}] \"{0}\" \u2192 level={1}, text=\"{2}\"")
	@MethodSource("provideHeadingTestCases")
	void testHeadingExtractionLevelAndText(String markdownLine, int expectedLevel,
		String expectedText)
	{
		MarkdownContext context = new MarkdownContext();
		context.setOriginalContent(markdownLine);

		new HeadingExtractor().process(context);

		assertEquals(1, context.getHeadings().size(), "Exactly one heading should be extracted");
		assertEquals(expectedText, context.getHeadings().get(0), "Heading text mismatch");
		assertEquals(expectedLevel, context.getHeadingLevels().get(0), "Heading level mismatch");
	}

	/**
	 * Lines that must NOT be recognized as headings: no space after '#', 7 hashes, empty, or plain
	 * text
	 */
	@ParameterizedTest(name = "[{index}] non-heading: \"{0}\"")
	@ValueSource(strings = { "#NoSpace", "##NoSpace", "####### Seven hashes", "", "Not a heading",
			"# ", "plain text" })
	void testNonHeadingLinesAreIgnored(String markdownLine)
	{
		MarkdownContext context = new MarkdownContext();
		context.setOriginalContent(markdownLine);

		new HeadingExtractor().process(context);

		assertTrue(context.getHeadings().isEmpty(),
			"No heading should be extracted from: " + markdownLine);
	}

	/**
	 * Multiple headings in a single document: verifies count, order, and level accuracy
	 */
	static Stream<Arguments> provideMultiHeadingDocuments()
	{
		return Stream.of(
			// Two headings at different levels
			Arguments.of("# Title\n## Sub", new int[] { 1, 2 }, new String[] { "Title", "Sub" }),
			// Mixed content with non-heading lines
			Arguments.of("# A\nSome text.\n## B\nMore text.\n### C", new int[] { 1, 2, 3 },
				new String[] { "A", "B", "C" }),
			// All same level
			Arguments.of("# One\n# Two\n# Three", new int[] { 1, 1, 1 },
				new String[] { "One", "Two", "Three" }));
	}

	@ParameterizedTest(name = "[{index}] multi-heading document")
	@MethodSource("provideMultiHeadingDocuments")
	void testMultipleHeadingsExtracted(String markdown, int[] expectedLevels,
		String[] expectedTexts)
	{
		MarkdownContext context = new MarkdownContext();
		context.setOriginalContent(markdown);

		new HeadingExtractor().process(context);

		assertEquals(expectedLevels.length, context.getHeadings().size(), "Heading count mismatch");
		for (int i = 0; i < expectedLevels.length; i++)
		{
			assertEquals(expectedTexts[i], context.getHeadings().get(i),
				"Text mismatch at index " + i);
			assertEquals(expectedLevels[i], (int)context.getHeadingLevels().get(i),
				"Level mismatch at index " + i);
		}
	}
}
