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
package io.github.astrapisixtynine.markdownslugger.core;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.astrapisixtynine.markdownslugger.slug.DefaultSlugStrategy;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyConfig;

/**
 * Unit tests for the MarkdownProcessor pipeline Validates heading extraction, slug generation, TOC
 * creation, and anchor ID injection
 */
class MarkdownProcessorTest
{

	/**
	 * Tests the default pipeline on inline markdown text with 3 heading levels Verifies extracted
	 * headings, heading levels, slugs, TOC generation, and anchor ID injection
	 */
	@Test
	void testDefaultPipelineWithBasicMarkdown()
	{
		String markdown = """
			    # Title One
			    Some text here.

			    ## Subtitle Two
			    More text.

			    ### Final Section
			    Even more text.
			""".stripIndent();

		MarkdownContext context = new MarkdownContext();
		context.originalContent = markdown;

		SlugifyConfig config = new SlugifyConfig(Map.of(), // replacements
			true, // toLowerCase
			true, // stripNonAlphanumeric
			"-", // whitespaceReplacement
			true, // trimEdges
			true, // removeAccents
			true, // collapseDashes
			"[^a-z0-9\\s-]" // allowedCharactersRegex
		);

		MarkdownProcessor processor = MarkdownProcessor
			.defaultPipeline(new DefaultSlugStrategy(config));
		processor.process(context);

		// Assert headings
		List<String> headings = context.headings;
		assertEquals(3, headings.size());
		assertEquals("Title One", headings.get(0));
		assertEquals("Subtitle Two", headings.get(1));
		assertEquals("Final Section", headings.get(2));

		// Assert heading levels
		assertEquals(List.of(1, 2, 3), context.headingLevels);

		// Assert slugs
		List<String> slugs = context.slugs;
		assertEquals("title-one", slugs.get(0));
		assertEquals("subtitle-two", slugs.get(1));
		assertEquals("final-section", slugs.get(2));

		// Assert TOC content with indentation
		String expectedToc = String.join("\n", "- [Title One](#title-one)",
			"  - [Subtitle Two](#subtitle-two)", "    - [Final Section](#final-section)");

		assertEquals(expectedToc.trim(), context.toc.trim());

		// Assert anchor IDs injected
		assertTrue(context.originalContent.contains("# Title One {#title-one}"));
		assertTrue(context.originalContent.contains("## Subtitle Two {#subtitle-two}"));
		assertTrue(context.originalContent.contains("### Final Section {#final-section}"));
	}

	/**
	 * Tests the pipeline with a temporary external markdown file Verifies correct extraction, slug
	 * generation, and anchor ID injection
	 */
	@Test
	void testWithExternalMarkdownFile() throws Exception
	{
		Path tempFile = Files.createTempFile("test-markdown", ".md");
		String content = """
			    # Heading A

			    ## Heading B

			    ### Heading C
			""".stripIndent();
		Files.writeString(tempFile, content);

		MarkdownContext context = new MarkdownContext();
		context.originalContent = Files.readString(tempFile);
		SlugifyConfig config = new SlugifyConfig(Map.of(), // replacements
			true, // toLowerCase
			true, // stripNonAlphanumeric
			"-", // whitespaceReplacement
			true, // trimEdges
			true, // removeAccents
			true, // collapseDashes
			"[^a-z0-9\\s-]" // allowedCharactersRegex
		);
		MarkdownProcessor processor = MarkdownProcessor
			.defaultPipeline(new DefaultSlugStrategy(config));

		processor.process(context);

		assertEquals(List.of("Heading A", "Heading B", "Heading C"), context.headings);
		assertEquals(List.of("heading-a", "heading-b", "heading-c"), context.slugs);

		// Check rewritten markdown includes anchor IDs
		assertTrue(context.originalContent.contains("# Heading A {#heading-a}"));
		assertTrue(context.originalContent.contains("## Heading B {#heading-b}"));
		assertTrue(context.originalContent.contains("### Heading C {#heading-c}"));
	}

	/**
	 * Tests the Markdown processor with a specific external markdown file
	 *
	 * Verifies that the file exists, reads its contents, processes it through the full pipeline,
	 * and ensures that headings are extracted, slugs are generated, and anchor IDs are injected
	 *
	 * This is useful for regression testing against real-world markdown files (e.g., from a book or
	 * article)
	 *
	 * @throws Exception
	 *             if file reading fails
	 */
	@Test
	@Disabled("test with your md file and enable this test case")
	void testWithSpecificMarkdownFile() throws Exception
	{
		Path mdFilePath = Paths.get("src/test/resources/ia_pour_tous_livre.gfm");
		assertTrue(Files.exists(mdFilePath), "Markdown file must exist");

		MarkdownContext context = new MarkdownContext();
		context.originalContent = Files.readString(mdFilePath);
		SlugifyConfig config = new SlugifyConfig(Map.of(), // replacements
			true, // toLowerCase
			false, // stripNonAlphanumeric
			"-", // whitespaceReplacement
			true, // trimEdges
			false, // removeAccents
			true, // collapseDashes
			"[^a-z0-9\\s-]" // allowedCharactersRegex
		);
		MarkdownProcessor processor = MarkdownProcessor
			.defaultPipeline(new DefaultSlugStrategy(config));

		processor.process(context);

		// Optional: Add assertions to verify that the pipeline is not empty
		assertFalse(context.headings.isEmpty(), "Headings should be extracted");
		assertEquals(context.headings.size(), context.slugs.size(),
			"Each heading should have a corresponding slug");
		assertTrue(context.originalContent.contains("{#"),
			"Modified content should include anchor IDs");

		Path tocPath = Paths.get("src/test/resources/toc.md");
		Files.createDirectories(tocPath.getParent()); // make sure the directory exists
		Files.writeString(tocPath, context.toc);
	}


	/**
	 * Tests headings with special characters and verifies correct slug conversion and heading level
	 * detection
	 */
	@Test
	void testHeadingsWithSpecialCharacters()
	{
		String markdown = """
			    # Welcome to the Jungle!
			    ## What's New in v2.0?
			    ### Über-cool Stuff & Features
			""".stripIndent();

		MarkdownContext context = new MarkdownContext();
		context.originalContent = markdown;
		SlugifyConfig config = new SlugifyConfig(Map.of(), // replacements
			true, // toLowerCase
			true, // stripNonAlphanumeric
			"-", // whitespaceReplacement
			true, // trimEdges
			true, // removeAccents
			false, // collapseDashes <-- set to false
			"[^a-z0-9\\s-]");

		MarkdownProcessor processor = MarkdownProcessor
			.defaultPipeline(new DefaultSlugStrategy(config));

		processor.process(context);

		assertEquals(
			List.of("Welcome to the Jungle!", "What's New in v2.0?", "Über-cool Stuff & Features"),
			context.headings);
		assertEquals(List.of(1, 2, 3), context.headingLevels);
		assertEquals(
			List.of("welcome-to-the-jungle", "whats-new-in-v20", "uber-cool-stuff-features"),
			context.slugs);

	}
}
