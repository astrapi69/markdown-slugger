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
package io.github.astrapisixtynine.markdownslugger.io;

import static io.github.astrapisixtynine.markdownslugger.io.MarkdownAnchorFixer.convertToFragmentIds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.astrapisixtynine.markdownslugger.slug.ReplacementRule;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyConfig;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyExtensions;

/**
 * Unit tests for {@link MarkdownAnchorFixer} Verifies slug generation, anchor injection, fragment
 * detection, and file patching behavior
 */
public class MarkdownAnchorFixerTest
{

	@Test
	void testAddMissingHeadingIdsFromFile() throws IOException
	{
		Path input = Paths.get("src/test/resources/chapter-03.md");

		String result = MarkdownAnchorFixer.addMissingHeadingIds(input);

		assertTrue(result.contains("{#"),
				"Expected at least one heading with an injected anchor ID");
	}

	/**
	 * Tests the complete anchor fix workflow using a real Markdown file Verifies that anchors are
	 * correctly injected and file is written to disk
	 *
	 * @throws IOException
	 *             if file I/O fails during setup or assertions
	 */
	@Test
	void testFixMarkdownFileAddsMissingHeadingIds() throws IOException
	{
		Path input = Paths.get("src/test/resources/chapter-03.md");
		Path output = Paths.get("build/tmp/chapter-03.md");
		Files.createDirectories(output.getParent());

		List<String> lines = Files.readAllLines(input);
		Set<String> fragmentIds = MarkdownAnchorFixer.extractFragmentLinks(lines);

		List<String> fixedLines = MarkdownAnchorFixer.addMissingHeadingIds(lines, fragmentIds);
		Files.write(output, fixedLines);

		assertTrue(Files.exists(output), "Output file should exist");

		boolean foundAnchor = fixedLines.stream().anyMatch(line -> line.matches(".*\\{#.*\\}.*"));
		assertTrue(foundAnchor, "At least one heading should have an injected anchor ID");
	}

	/**
	 * Tests fragment ID extraction from Markdown lines Verifies that only valid in-document
	 * fragment links are returned
	 */
	@Test
	void testExtractFragmentLinks()
	{
		List<String> markdownLines = Arrays.asList("- [Introduction](#introduction)",
			"- [How it works](#how-it-works)", "Some text without a link");

		Set<String> result = MarkdownAnchorFixer.extractFragmentLinks(markdownLines);

		assertEquals(2, result.size());
		assertTrue(result.contains("introduction"));
		assertTrue(result.contains("how-it-works"));
	}

	/**
	 * Tests the injection of anchor IDs into headings Ensures that only headings matching known
	 * fragment links are updated
	 */
	@Test
	void testAddMissingHeadingIds()
	{
		List<String> markdownLines = Arrays.asList("## Introduction", "Some content here.",
			"## How it works", "More text here.");

		Set<String> fragmentIds = new HashSet<>(Arrays.asList("introduction", "how-it-works"));

		List<String> result = MarkdownAnchorFixer.addMissingHeadingIds(markdownLines, fragmentIds);

		assertEquals("## Introduction {#introduction}", result.get(0));
		assertEquals("## How it works {#how-it-works}", result.get(2));
	}

	/**
	 * Tests slugification behavior for common accented or formatted strings Verifies correct
	 * replacement, sanitization, and transformation to slug format
	 */
	@Test
	void testSlugify()
	{
		List<ReplacementRule> customReplacements = new ArrayList<>();
		customReplacements
			.add(ReplacementRule.builder().pattern("☕").replacement("").regex(false).build()); // custom
																								// literal
																								// replacement
		customReplacements.addAll(SlugifyConfig.DEFAULT_REPLACEMENT_RULES); // default diacritic
																			// rules

		SlugifyConfig config = SlugifyConfig.builder().replacementRules(customReplacements)
			.toLowerCase(true).stripNonAlphanumeric(false).whitespaceReplacement("-")
			.trimEdges(true).removeAccents(false).collapseDashes(true)
			.allowedCharactersRegex("[^a-z0-9\\s-]").build();

		assertEquals("introduction-au-cafe",
			SlugifyExtensions.slugify("Introduction au café ☕", config));
		assertEquals("idees-creatives", SlugifyExtensions.slugify("Idées créatives"));
		assertEquals("section-1", SlugifyExtensions.slugify("Section 1"));
	}
}
