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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
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
	void testAddMissingHeadingIdToLine()
	{
		SlugifyConfig config = SlugifyConfig.builder()
			.replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES).toLowerCase(true)
			.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
			.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]")
			.build();

		String line = "### ✎ Exemple 1 : Élargir son vocabulaire";
		String expected = "### ✎ Exemple 1 : Élargir son vocabulaire {#exemple-1-elargir-son-vocabulaire}";

		String actual = MarkdownAnchorFixer.addMissingHeadingIdToLine(line, config);

		assertEquals(expected, actual);
	}


	@Test
	void testPromptAdviceLanguageWithSingleHash() throws IOException
	{

		SlugifyConfig config = SlugifyConfig.builder()
			.replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES).toLowerCase(true)
			.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
			.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]")
			.build();
		String line = "# Chapitre 4: Apprentissage et développement des compétences avec l'IA";
		String expected = "# Chapitre 4: Apprentissage et développement des compétences avec l'IA {#chapitre-4-apprentissage-et-developpement-des-competences-avec-lia}";
		String actual = MarkdownAnchorFixer.addMissingHeadingIdToLine(line, config);

		assertEquals(expected, actual);
	}


	@Test
	@Disabled("test with your md file and enable this test case")
	void testAddMissingHeadingIdsFromFile() throws IOException
	{
		Path input;
		String result;

		input = Paths.get("src/test/resources/chapter-03.md");

		result = MarkdownAnchorFixer.addMissingHeadingIds(input);

		System.out.println(result);

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
	@Disabled("test with your md file and enable this test case")
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

	@Test
	void testSlugifyNormalization()
	{
		String heading = "Un point de vue personnel : du chaos à l'organisation";

		SlugifyConfig config = SlugifyConfig.builder()
			.replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES).toLowerCase(true)
			.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
			.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]")
			.build();

		String slug = SlugifyExtensions.slugify(heading, config);

		System.out.println("Slug: " + slug);

		assertEquals("un-point-de-vue-personnel-du-chaos-a-lorganisation", slug);
	}


	@Test
	@Disabled("test with your md file and enable this test case")
	void testAddMissingHeadingIdsFromFileWithSlugifyConfig() throws IOException
	{
		Path input;
		String result;
		SlugifyConfig config;

		input = Paths.get("src/test/resources/chapter-03.md");

		config = SlugifyConfig.builder().replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES)
			.toLowerCase(true).stripNonAlphanumeric(true) // ✅ Removes ':', "'", etc.
			.removeAccents(true).collapseDashes(true).whitespaceReplacement("-").trimEdges(true)
			.allowedCharactersRegex("[^a-z0-9\\s-]").build();
		result = MarkdownAnchorFixer.addMissingHeadingIds(input, config);

		assertTrue(result.contains("{#"),
			"Expected at least one heading with an injected anchor ID");
	}


	@Test
	@Disabled("test with your md file and enable this test case")
	void testAddMissingHeadingIdsFromFile2() throws IOException
	{
		Path input;
		String result;
		SlugifyConfig config;
		List<String> fragmentIds;
		input = Paths.get("src/test/resources/chapter-03.md");

		// Step 1: read original Markdown content
		List<String> markdownLines = Files.readAllLines(input);

		// Step 2: extract headings (##, ###, etc.) and remove hash symbols
		List<String> headingsWithoutHashes = MarkdownAnchorFixer
			.extractHeadingsWithoutHashes(input);

		// Step 3: convert to properly normalized slugs using strict config
		List<ReplacementRule> defaultReplacementRules = new ArrayList<>(
			SlugifyConfig.DEFAULT_REPLACEMENT_RULES);
		config = SlugifyConfig.builder().replacementRules(defaultReplacementRules).toLowerCase(true)
			.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
			.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]")
			.build();

		fragmentIds = MarkdownAnchorFixer.convertToFragmentIds(headingsWithoutHashes, config);

		// Step 4: inject missing anchor IDs into headings
		List<String> lines = MarkdownAnchorFixer.addMissingHeadingIds(markdownLines,
			new HashSet<>(fragmentIds));
		lines.stream().filter(line -> line.contains("{#")).forEach(System.out::println);
		String resultText = String.join(System.lineSeparator(), lines);

		// Step 5: assert that at least one anchor ID was injected
		boolean hasAnchors = resultText.contains("{#");
		assertTrue(hasAnchors, "Expected at least one heading with an injected anchor ID");

		// Optional: check for a known slug you expect
		assertTrue(resultText.contains("{#un-point-de-vue-personnel-du-chaos-a-lorganisation}"),
			"Expected cleaned and slugified anchor ID to be present");
	}

	/**
	 * Tests the injection of anchor IDs into headings Ensures that only headings matching known
	 * fragment links are updated
	 */
	@Test
	void testAddMissingHeadingIds()
	{
		List<String> markdownLines;
		List<String> result;
		Set<String> fragmentIds;

		markdownLines = Arrays.asList("## Introduction", "Some content here.", "## How it works",
			"More text here.");

		fragmentIds = new HashSet<>(Arrays.asList("introduction", "how-it-works"));

		result = MarkdownAnchorFixer.addMissingHeadingIds(markdownLines, fragmentIds);

		assertEquals("## Introduction {#introduction}", result.get(0));
		assertEquals("## How it works {#how-it-works}", result.get(2));

		markdownLines = Arrays.asList("# Title One", "## Subtitle Two",
			"### Subtitle Three {#subtitle-three}", "Not a heading", "###### Final Heading");

		// Slugs of headings we want to target
		fragmentIds = new HashSet<>(Arrays.asList("title-one", // should be injected
			"subtitle-two", // should be injected
			"subtitle-three", // already exists, skip
			"final-heading" // should be injected
		));

		result = MarkdownAnchorFixer.addMissingHeadingIds(markdownLines, fragmentIds);

		assertEquals("# Title One {#title-one}", result.get(0));
		assertEquals("## Subtitle Two {#subtitle-two}", result.get(1));
		assertEquals("### Subtitle Three {#subtitle-three}", result.get(2)); // unchanged
		assertEquals("Not a heading", result.get(3)); // untouched
		assertEquals("###### Final Heading {#final-heading}", result.get(4));
	}

	@Test
	@Disabled("test with your md file and enable this test case")
	void testAddMissingHeadingIdsInPlace() throws IOException
	{
		// Arrange
		Path original = Paths.get("src/test/resources/chapter-03.md");
		Path tempFile = Files.createTempFile("chapter-03-processed", ".md");
		Files.copy(original, tempFile, StandardCopyOption.REPLACE_EXISTING);

		List<ReplacementRule> defaultReplacementRules = new ArrayList<>(
			SlugifyConfig.DEFAULT_REPLACEMENT_RULES);
		// Use default config with emoji/symbol replacement
		SlugifyConfig config = SlugifyConfig.builder().replacementRules(defaultReplacementRules) // assuming
																									// already
																									// extended
			.toLowerCase(true).stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
			.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]")
			.build();

		// Act
		MarkdownAnchorFixer.addMissingHeadingIdsInPlace(original, config);

		// Assert
		String updatedContent = Files.readString(tempFile);
		assertTrue(updatedContent.contains("{#un-point-de-vue-personnel-du-chaos-a-lorganisation}"),
			"Expected known slug to be injected");
		assertTrue(updatedContent.contains("{#resume}") || updatedContent.matches(".*\\{#.*}.*"),
			"Expected at least one heading anchor to be injected");

		// Clean up
		Files.deleteIfExists(tempFile);
	}


	@Test
	@Disabled("test with your md file and enable this test case")
	void testGenerateMarkdownToc() throws IOException
	{
		Path original = Paths.get("src/test/resources/chapter-03.md");
		SlugifyConfig config = SlugifyConfig.builder()
			.replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES).toLowerCase(true)
			.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
			.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]")
			.build();

		List<String> toc = MarkdownAnchorFixer.generateMarkdownToc(original, config);

		// Simple assertion to check known structure
		assertFalse(toc.isEmpty(), "TOC should not be empty");
		assertTrue(
			toc.stream().anyMatch(line -> line.contains("Chapitre 3") && line.contains("utiliser")),
			"Top heading should be present");
		toc.forEach(System.out::println);
	}

}
