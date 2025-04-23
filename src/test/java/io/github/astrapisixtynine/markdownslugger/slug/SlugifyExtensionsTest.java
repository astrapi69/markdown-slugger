package io.github.astrapisixtynine.markdownslugger.slug;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.astrapisixtynine.markdownslugger.test.object.factory.TestObjectFactory;

/**
 * Unit tests for {@link SlugifyExtensions}
 */
class SlugifyExtensionsTest
{

	/**
	 * Tests slug generation for a French Markdown heading that includes a colon and an apostrophe.
	 *
	 * <p>
	 * This verifies that:
	 * <ul>
	 * <li>The colon and space are replaced with a hyphen</li>
	 * <li>The apostrophe is removed</li>
	 * <li>Multiple adjacent hyphens are preserved when configured not to collapse</li>
	 * </ul>
	 */
	@Test
	void testFrenchHeadingWithColonAndApostrophe()
	{
		// Given
		String heading = "Chapitre 6 : Augmenter votre productivité avec l’IA";
		String expectedSlug = "chapitre-6--augmenter-votre-productivité-avec-lia";

		List<ReplacementRule> replacementRules = TestObjectFactory.getReplacementRules();
		SlugifyConfig slugifyConfig = TestObjectFactory.getSlugifyConfig(replacementRules);

		// When
		String actualSlug = SlugifyExtensions.slugify(heading, slugifyConfig);

		// Then
		assertEquals(expectedSlug, actualSlug,
			"Slug should preserve the double hyphen after removing punctuation");
	}

	/**
	 * Test heading with symbol and colon.
	 */
	@Test
	void testHeadingWithSymbolAndColon()
	{
		// Given
		String heading = "✎ Exemple 1 : Organiser sa journée";
		String expectedSlug = "-exemple-1--organiser-sa-journée";

		List<ReplacementRule> replacementRules = TestObjectFactory.getReplacementRules();
		SlugifyConfig slugifyConfig = TestObjectFactory.getSlugifyConfig(replacementRules);

		// When
		String actualSlug = SlugifyExtensions.slugify(heading, slugifyConfig);

		// Then
		assertEquals(expectedSlug, actualSlug,
			"Slug should preserve symbol as dash and handle colon-space with double hyphen");
	}

}
