package io.github.astrapisixtynine.markdownslugger.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class MarkdownHeadingExtractionTest
{

	@Test
	void testExtractAllHeadingLevels()
	{
		// Given Markdown lines with heading levels 1 through 6
		List<String> markdown = List.of("# Title Level 1", "## Section Level 2",
			"### Subsection Level 3", "#### Level 4 Heading", "##### Level 5 Heading",
			"###### Level 6 Heading", "Not a heading line", "#InvalidNoSpace", // Invalid: no space
																				// after #
			"###     Spaced Heading" // Valid
		);

		Pattern headingPattern = Pattern.compile("^(#{1,6})\\s+(.*)$");

		List<String> extractedHeadings = new ArrayList<>();
		List<Integer> headingLevels = new ArrayList<>();

		for (String line : markdown)
		{
			Matcher matcher = headingPattern.matcher(line);
			if (matcher.matches())
			{
				headingLevels.add(matcher.group(1).length());
				extractedHeadings.add(matcher.group(2));
			}
		}

		// Assert we found 7 valid headings (ignores non-heading and invalid ones)
		assertEquals(7, extractedHeadings.size());
		assertEquals("Title Level 1", extractedHeadings.get(0));
		assertEquals("Spaced Heading", extractedHeadings.get(6));
		assertEquals(List.of(1, 2, 3, 4, 5, 6, 3), headingLevels);
	}
}
