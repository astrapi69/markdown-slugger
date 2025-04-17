package io.github.astrapisixtynine.markdownslugger.slug;

import java.text.Normalizer;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Default implementation of the SlugStrategy interface Converts headings to lowercase, removes
 * special characters, and replaces spaces with dashes
 */
public class DefaultSlugStrategy implements SlugStrategy
{
	/**
	 * Configuration object that defines the rules for converting headings into slugs Includes
	 * options such as case conversion, accent removal, custom replacements, whitespace handling,
	 * character stripping, and dash collapsing
	 */
	private final SlugifyConfig config;

	/**
	 * Constructs a DefaultSlugStrategy with the specified slugification configuration
	 *
	 * @param config
	 *            the SlugifyConfig that controls how headings are transformed into slugs
	 */
	public DefaultSlugStrategy(SlugifyConfig config)
	{
		this.config = config;
	}

	/**
	 * Converts the given heading to a slug Applies lowercase transformation, removes
	 * non-alphanumeric characters, and replaces spaces with dashes
	 *
	 * @param heading
	 *            the heading text to slugify
	 * @return a URL-safe slug string
	 */
	@Override
	public String toSlug(String heading)
	{
		String slug = heading;

		// 1. Apply custom replacements (e.g., ä → ae, é → e)
		for (Map.Entry<String, String> entry : config.getReplacements().entrySet())
		{
			slug = slug.replace(entry.getKey(), entry.getValue());
		}

		// 2. Optionally remove accents (Unicode normalization)
		if (config.isRemoveAccents())
		{
			slug = Normalizer.normalize(slug, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		}

		// 3. Convert to lowercase if enabled
		if (config.isToLowerCase())
		{
			slug = slug.toLowerCase();
		}

		// 4. Optionally remove non-alphanumeric characters (excluding space and dash)
		if (config.isStripNonAlphanumeric())
		{
			slug = slug.replaceAll(config.getAllowedCharactersRegex(), "");

		}

		// 5. Replace all whitespace with the configured replacement (e.g., "-")
		slug = slug.replaceAll("\\s+", config.getWhitespaceReplacement());

		// 6. Optionally collapse multiple separators into one
		if (config.isCollapseDashes())
		{
			String sep = Pattern.quote(config.getWhitespaceReplacement());
			slug = slug.replaceAll(sep + "{2,}", config.getWhitespaceReplacement());
		}

		// 7. Optionally trim leading/trailing separators
		if (config.isTrimEdges())
		{
			String sep = Pattern.quote(config.getWhitespaceReplacement());
			slug = slug.replaceAll("^" + sep + "+", "").replaceAll(sep + "+$", "");
		}

		return slug;
	}

}
