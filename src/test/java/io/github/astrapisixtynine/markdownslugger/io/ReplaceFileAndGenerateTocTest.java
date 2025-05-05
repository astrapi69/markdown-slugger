package io.github.astrapisixtynine.markdownslugger.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.file.write.StoreFileExtensions;
import io.github.astrapisixtynine.markdownslugger.slug.ReplacementRule;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyConfig;

public class ReplaceFileAndGenerateTocTest
{


	@Test
	@Disabled("test with your md file and enable this test case")
	void testAddMissingHeadingIdsInPlace() throws IOException
	{
		String chapterNumber = "epilogue";
		String markdownFilename = "" + chapterNumber + ".md";
		String tocFilename = "toc-" + chapterNumber + ".md";

		// Arrange
		Path original = Paths.get("src/test/resources/" + markdownFilename);
		Path originalToc = Paths.get("src/test/resources/" + tocFilename);

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

		List<String> toc = MarkdownAnchorFixer.generateMarkdownToc(original, config);

		StoreFileExtensions.toFile(originalToc.toFile(), toc);

		toc.forEach(System.out::println);

	}

	@Test
	@Disabled("test with your md file and enable this test case")
	void testAddMissingHeadingIdsInPlace2() throws IOException
	{
		// Arrange
		Path original = Paths.get("src/test/resources/chapter-04.md");

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

	@Test
	void testExemple1Vocabulary() throws IOException
	{
		SlugifyConfig config = SlugifyConfig.builder()
			.replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES).toLowerCase(true)
			.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
			.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]")
			.build();
		String line = "### ✎ Exemple 1 : Élargir son vocabulaire";
		List<String> lines = List.of(line);
		List<String> toc = MarkdownAnchorFixer.generateMarkdownTocWithoutRegex(lines, config);
		assertFalse(toc.isEmpty(), "TOC should not be empty for: " + line);
		System.out.println(toc.get(0));
	}

}
