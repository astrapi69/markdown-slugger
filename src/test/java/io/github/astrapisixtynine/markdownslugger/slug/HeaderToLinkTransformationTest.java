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
package io.github.astrapisixtynine.markdownslugger.slug;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Focused unit tests demonstrating the complete header-to-link transformation
 *
 * Given a Markdown heading line like {@code ## My Header}, this test verifies the full chain:
 * <ol>
 * <li>Extract heading text from the Markdown line</li>
 * <li>Generate a slug from the heading text</li>
 * <li>Build the Markdown anchor link {@code [My Header](#my-header)}</li>
 * </ol>
 */
class HeaderToLinkTransformationTest
{

	/** Strict config matching the pipeline default: accent removal + stripping */
	static final SlugifyConfig STRICT_CONFIG = SlugifyConfig.builder()
		.replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES).toLowerCase(true)
		.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
		.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]").build();

	/**
	 * Extracts the heading text from a Markdown heading line by stripping leading '#' characters and
	 * whitespace
	 *
	 * @param markdownLine
	 *            a line like "## My Header"
	 * @return the heading text, e.g. "My Header"
	 */
	static String extractHeadingText(String markdownLine)
	{
		return markdownLine.replaceFirst("^#{1,6}\\s+", "");
	}

	/**
	 * Builds a Markdown anchor link from heading text and its slug
	 *
	 * @param headingText
	 *            the display text, e.g. "My Header"
	 * @param slug
	 *            the URL-safe slug, e.g. "my-header"
	 * @return the Markdown link, e.g. "[My Header](#my-header)"
	 */
	static String buildMarkdownLink(String headingText, String slug)
	{
		return "[" + headingText + "](#" + slug + ")";
	}

	/**
	 * Provides test cases: Markdown heading line, expected heading text, expected slug, expected
	 * Markdown link
	 */
	static Stream<Arguments> provideHeaderToLinkCases()
	{
		return Stream.of(
			// Basic headings at different levels
			Arguments.of("# Introduction", "Introduction", "introduction",
				"[Introduction](#introduction)"),
			Arguments.of("## Getting Started", "Getting Started", "getting-started",
				"[Getting Started](#getting-started)"),
			Arguments.of("### Installation Guide", "Installation Guide", "installation-guide",
				"[Installation Guide](#installation-guide)"),

			// German umlauts
			Arguments.of("## Uber den Wolken", "Uber den Wolken", "uber-den-wolken",
				"[Uber den Wolken](#uber-den-wolken)"),

			// French accented characters
			Arguments.of("## Idees creatives", "Idees creatives", "idees-creatives",
				"[Idees creatives](#idees-creatives)"),

			// Special characters stripped
			Arguments.of("# Welcome to the Jungle!", "Welcome to the Jungle!",
				"welcome-to-the-jungle", "[Welcome to the Jungle!](#welcome-to-the-jungle)"),
			Arguments.of("## What's New in v2.0?", "What's New in v2.0?", "whats-new-in-v20",
				"[What's New in v2.0?](#whats-new-in-v20)"),

			// Colons removed
			Arguments.of("## Section 1: Introduction", "Section 1: Introduction",
				"section-1-introduction",
				"[Section 1: Introduction](#section-1-introduction)"),

			// Multiple spaces collapsed
			Arguments.of("# Multiple   Spaces   Here", "Multiple   Spaces   Here",
				"multiple-spaces-here",
				"[Multiple   Spaces   Here](#multiple-spaces-here)"),

			// Emoji removed (with strict config / removeAccents)
			Arguments.of("# Launch", "Launch", "launch", "[Launch](#launch)"),

			// Deep nesting
			Arguments.of("###### Deep Section", "Deep Section", "deep-section",
				"[Deep Section](#deep-section)"));
	}

	@ParameterizedTest(name = "[{index}] \"{0}\" -> {3}")
	@MethodSource("provideHeaderToLinkCases")
	void testHeaderToLinkTransformation(String markdownLine, String expectedHeading,
		String expectedSlug, String expectedLink)
	{
		// Step 1: Extract heading text from Markdown line
		String headingText = extractHeadingText(markdownLine);
		assertEquals(expectedHeading, headingText, "Heading extraction for: " + markdownLine);

		// Step 2: Generate slug
		String slug = SlugifyExtensions.slugify(headingText, STRICT_CONFIG);
		assertEquals(expectedSlug, slug, "Slug generation for: " + headingText);

		// Step 3: Build Markdown anchor link
		String link = buildMarkdownLink(headingText, slug);
		assertEquals(expectedLink, link, "Link generation for: " + headingText);
	}

	/**
	 * Provides test cases using the default config (without stripNonAlphanumeric)
	 */
	static Stream<Arguments> provideDefaultConfigHeaderToLinkCases()
	{
		return Stream.of(
			Arguments.of("# Hello World", "Hello World", "hello-world",
				"[Hello World](#hello-world)"),
			Arguments.of("## Schone Grusse", "Schone Grusse", "schone-grusse",
				"[Schone Grusse](#schone-grusse)"),
			Arguments.of("# Section 1", "Section 1", "section-1", "[Section 1](#section-1)"));
	}

	@ParameterizedTest(name = "[{index}] default config: \"{0}\" -> {3}")
	@MethodSource("provideDefaultConfigHeaderToLinkCases")
	void testHeaderToLinkWithDefaultConfig(String markdownLine, String expectedHeading,
		String expectedSlug, String expectedLink)
	{
		String headingText = extractHeadingText(markdownLine);
		assertEquals(expectedHeading, headingText);

		String slug = SlugifyExtensions.slugify(headingText);
		assertEquals(expectedSlug, slug);

		String link = buildMarkdownLink(headingText, slug);
		assertEquals(expectedLink, link);
	}
}
