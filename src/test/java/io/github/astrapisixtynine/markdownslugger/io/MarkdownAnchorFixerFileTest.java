package io.github.astrapisixtynine.markdownslugger.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapisixtynine.markdownslugger.slug.SlugifyConfig;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyExtensions;

class MarkdownAnchorFixerFileTest
{
	@ParameterizedTest
	@CsvFileSource(resources = "/chapter3-headings.csv", numLinesToSkip = 1)
	@Disabled("test with your md file and enable this test case")
	void testExpectedAnchorExists(String heading) throws IOException
	{
		String expectedAnchor = SlugifyExtensions.slugify(heading);
		Path processedFile = Path.of("path/to/processed/chapter3.md");
		String content = Files.readString(processedFile);
		assertTrue(content.contains("id=\"" + expectedAnchor + "\""),
			() -> "Expected anchor not found for heading: " + heading);
	}

	@ParameterizedTest
	@MethodSource("provideHeadingsFromChapter3")
	@Disabled("test with your md file and enable this test case")
	void testAnchorInjectionFromRealHeadings(String headingText, String expectedSlug)
		throws Exception
	{
		String line = "## " + headingText;
		List<String> markdown = List.of(line);
		Set<String> fragmentIds = Set.of(expectedSlug);

		List<String> result = MarkdownAnchorFixer.addMissingHeadingIds(markdown, fragmentIds);

		String output = result.get(0);
		assertTrue(output.contains("{#" + expectedSlug + "}"),
			"Expected anchor not found in: " + output);
	}

	static Stream<org.junit.jupiter.params.provider.Arguments> provideHeadingsFromChapter3()
		throws Exception
	{
		Path file = Path.of("src/test/resources/chapter-03.md");

		List<String> lines = Files.readAllLines(file);
		List<String> headings = new ArrayList<>();
		Pattern headingPattern = Pattern.compile("^(#{1,6})\\s+(.*)$");

		for (String line : lines)
		{
			Matcher matcher = headingPattern.matcher(line);
			if (matcher.matches())
			{
				headings.add(matcher.group(2).trim());
			}
		}

		SlugifyConfig config = SlugifyConfig.builder()
			.replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES).toLowerCase(true)
			.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
			.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]")
			.build();

		return headings.stream().map(text -> org.junit.jupiter.params.provider.Arguments.of(text,
			SlugifyExtensions.slugify(text, config)));
	}
}
