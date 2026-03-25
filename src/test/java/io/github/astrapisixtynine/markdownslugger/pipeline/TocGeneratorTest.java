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

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapisixtynine.markdownslugger.core.MarkdownContext;

/**
 * Parameterized unit tests for {@link TocGenerator}
 */
class TocGeneratorTest
{

	/**
	 * Provides test cases for a single heading: level, heading text, slug → expected TOC line
	 */
	static Stream<Arguments> provideSingleHeadingTocCases()
	{
		return Stream.of(
			// Level 1: no indentation (0 × 2 spaces)
			Arguments.of(1, "Introduction", "introduction", "- [Introduction](#introduction)"),
			// Level 2: 2 spaces indent
			Arguments.of(2, "Getting Started", "getting-started",
				"  - [Getting Started](#getting-started)"),
			// Level 3: 4 spaces indent
			Arguments.of(3, "Installation", "installation", "    - [Installation](#installation)"),
			// Level 4: 6 spaces indent
			Arguments.of(4, "Configuration", "configuration",
				"      - [Configuration](#configuration)"),
			// Level 5: 8 spaces indent
			Arguments.of(5, "Advanced", "advanced", "        - [Advanced](#advanced)"),
			// Level 6: 10 spaces indent
			Arguments.of(6, "Reference", "reference", "          - [Reference](#reference)"));
	}

	@ParameterizedTest(name = "[{index}] level={0} \"{1}\" \u2192 \"{3}\"")
	@MethodSource("provideSingleHeadingTocCases")
	void testTocLineForSingleHeading(int level, String heading, String slug, String expectedLine)
	{
		MarkdownContext context = new MarkdownContext();
		context.getHeadings().add(heading);
		context.getHeadingLevels().add(level);
		context.getSlugs().add(slug);

		new TocGenerator().process(context);

		assertTrue(context.getToc().contains(expectedLine),
			"TOC should contain: " + expectedLine + "\nActual TOC:\n" + context.getToc());
	}

	/**
	 * Provides test cases for multi-heading documents and their complete expected TOC
	 */
	static Stream<Arguments> provideMultiHeadingTocCases()
	{
		return Stream.of(
			// Three-level hierarchy
			Arguments.of(List.of("Title", "Subtitle", "Section"), List.of(1, 2, 3),
				List.of("title", "subtitle", "section"),
				"- [Title](#title)\n  - [Subtitle](#subtitle)\n    - [Section](#section)\n"),
			// Two headings at the same level
			Arguments.of(List.of("Alpha", "Beta"), List.of(1, 1), List.of("alpha", "beta"),
				"- [Alpha](#alpha)\n- [Beta](#beta)\n"),
			// Level 2 → Level 2 (flat structure)
			Arguments.of(List.of("A", "B", "C"), List.of(2, 2, 2), List.of("a", "b", "c"),
				"  - [A](#a)\n  - [B](#b)\n  - [C](#c)\n"));
	}

	@ParameterizedTest(name = "[{index}] multi-heading TOC generation")
	@MethodSource("provideMultiHeadingTocCases")
	void testTocForMultipleHeadings(List<String> headings, List<Integer> levels, List<String> slugs,
		String expectedToc)
	{
		MarkdownContext context = new MarkdownContext();
		context.getHeadings().addAll(headings);
		context.getHeadingLevels().addAll(levels);
		context.getSlugs().addAll(slugs);

		new TocGenerator().process(context);

		assertEquals(expectedToc, context.getToc());
	}

	@Test
	void testEmptyDocumentProducesEmptyToc()
	{
		MarkdownContext context = new MarkdownContext();

		new TocGenerator().process(context);

		assertEquals("", context.getToc());
	}
}
