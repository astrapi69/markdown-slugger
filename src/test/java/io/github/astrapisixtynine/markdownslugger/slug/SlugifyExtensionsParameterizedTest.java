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

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapisixtynine.markdownslugger.test.object.factory.TestObjectFactory;

/**
 * Parameterized unit tests for {@link SlugifyExtensions}
 */
class SlugifyExtensionsParameterizedTest
{

	/** Strict config: accent removal + stripping of non-alphanumeric characters */
	static final SlugifyConfig STRICT_CONFIG = SlugifyConfig.builder()
		.replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES).toLowerCase(true)
		.stripNonAlphanumeric(true).removeAccents(true).collapseDashes(true)
		.whitespaceReplacement("-").trimEdges(true).allowedCharactersRegex("[^a-z0-9\\s-]").build();

	/**
	 * Provides test cases for slugification with the default configuration Input headings and their
	 * expected slug output
	 */
	static Stream<Arguments> provideDefaultConfigTestCases()
	{
		return Stream.of(
			// Basic lowercase + whitespace → dash
			Arguments.of("Section 1", "section-1"), Arguments.of("Hello World", "hello-world"),
			Arguments.of("Introduction", "introduction"),
			// Accented chars replaced via DEFAULT_REPLACEMENT_RULES
			Arguments.of("idées créatives", "idees-creatives"),
			Arguments.of("Über-cool", "uber-cool"),
			// Multiple consecutive spaces collapsed after whitespace→dash + collapseDashes
			Arguments.of("  multiple   spaces  ", "multiple-spaces"),
			// Colon is removed by the punctuation step (step 3)
			Arguments.of("Title: Subtitle", "title-subtitle"),
			// Emoji removed via DEFAULT_REPLACEMENT_RULES
			Arguments.of("🚀 Launch", "launch"),
			// German umlauts replaced via DEFAULT_REPLACEMENT_RULES (ß NOT in rules → stays)
			Arguments.of("Über den Wolken", "uber-den-wolken"),
			Arguments.of("Schöne Grüße", "schone-grusse"));
	}

	@ParameterizedTest(name = "[{index}] \"{0}\" \u2192 \"{1}\"")
	@MethodSource("provideDefaultConfigTestCases")
	void testSlugifyWithDefaultConfig(String input, String expected)
	{
		assertEquals(expected, SlugifyExtensions.slugify(input));
	}

	/**
	 * Provides test cases for slugification with a strict configuration that enables accent removal
	 * and non-alphanumeric stripping
	 */
	static Stream<Arguments> provideStrictConfigTestCases()
	{
		return Stream.of(
			// Simple cases
			Arguments.of("Hello World", "hello-world"), Arguments.of("Section 1", "section-1"),
			// Accented characters removed via NFD normalization (after replacement rules)
			Arguments.of("Café au lait", "cafe-au-lait"),
			// Colon removed in punctuation step; surrounding spaces collapse to single dash
			Arguments.of("Section 1: Introduction", "section-1-introduction"),
			// Apostrophe and special chars removed via stripNonAlphanumeric
			Arguments.of("What's New in v2.0?", "whats-new-in-v20"),
			// Ampersand and special chars stripped, multiple spaces collapse
			Arguments.of("Über-cool Stuff & Features", "uber-cool-stuff-features"),
			// Full French sentence: à→a via replacement, apostrophe stripped, colon removed
			Arguments.of("Un point de vue personnel : du chaos à l'organisation",
				"un-point-de-vue-personnel-du-chaos-a-lorganisation"),
			// Only digits
			Arguments.of("Chapter 42", "chapter-42"),
			// Leading/trailing whitespace trimmed
			Arguments.of("  trimmed  ", "trimmed"));
	}

	@ParameterizedTest(name = "[{index}] \"{0}\" \u2192 \"{1}\"")
	@MethodSource("provideStrictConfigTestCases")
	void testSlugifyWithStrictConfig(String input, String expected)
	{
		assertEquals(expected, SlugifyExtensions.slugify(input, STRICT_CONFIG));
	}

	/**
	 * Provides test cases for slugification with the custom TestObjectFactory configuration, which
	 * maps specific symbols (✎, ?, ❖) and punctuation (colon-space, apostrophe) to dashes or empty
	 * string
	 */
	static Stream<Arguments> provideCustomConfigTestCases()
	{
		List<ReplacementRule> replacementRules = TestObjectFactory.getReplacementRules();
		SlugifyConfig config = TestObjectFactory.getSlugifyConfig(replacementRules);
		return Stream.of(
			// Colon-space → "-", right single quote (U+2019) removed by custom rule
			Arguments.of("Chapitre 6 : Augmenter votre productivit\u00e9 avec l\u2019IA",
				"chapitre-6--augmenter-votre-productivit\u00e9-avec-lia", config),
			// ✎ symbol → "" (empty), colon-space → "-"
			Arguments.of("✎ Exemple 1 : Organiser sa journ\u00e9e",
				"-exemple-1--organiser-sa-journ\u00e9e", config));
	}

	@ParameterizedTest(name = "[{index}] \"{0}\" \u2192 \"{1}\"")
	@MethodSource("provideCustomConfigTestCases")
	void testSlugifyWithCustomConfig(String input, String expected, SlugifyConfig config)
	{
		assertEquals(expected, SlugifyExtensions.slugify(input, config), "Slug for: " + input);
	}

	/**
	 * Provides test cases that verify NullPointerException is thrown for null inputs
	 */
	static Stream<Arguments> provideNullInputTestCases()
	{
		return Stream.of(Arguments.of((Object)null, SlugifyConfig.DEFAULT_CONFIG, "text"),
			Arguments.of("some text", (Object)null, "config"));
	}

	@ParameterizedTest(name = "[{index}] null {2} should throw NullPointerException")
	@MethodSource("provideNullInputTestCases")
	void testSlugifyThrowsOnNullInput(String text, SlugifyConfig config, String label)
	{
		org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class,
			() -> SlugifyExtensions.slugify(text, config),
			"Expected NullPointerException for null " + label);
	}
}
