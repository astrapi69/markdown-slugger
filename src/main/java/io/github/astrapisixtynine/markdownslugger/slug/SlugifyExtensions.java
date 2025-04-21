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

import java.text.Normalizer;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;


/**
 * Utility class for converting strings to URL-friendly slugs
 *
 * <p>
 * Provides methods to transform text into slugs by:
 * <ul>
 * <li>Converting characters to lowercase</li>
 * <li>Replacing special characters with their ASCII equivalents</li>
 * <li>Removing or replacing whitespace</li>
 * <li>Stripping non-alphanumeric characters</li>
 * </ul>
 *
 * <p>
 * Includes default character replacements for common Western European characters and supports
 * custom configurations through {@link SlugifyConfig}
 * </p>
 */
public class SlugifyExtensions
{

	/**
	 * Converts text to a URL-friendly slug using default configuration
	 *
	 * @param text
	 *            The input text to convert
	 * @return The generated slug with:
	 *         <ul>
	 *         <li>Special characters replaced</li>
	 *         <li>Whitespace converted to hyphens</li>
	 *         <li>Non-alphanumeric characters removed</li>
	 *         </ul>
	 * @throws NullPointerException
	 *             if the input text is null
	 */
	public static String slugify(String text)
	{
		return slugify(text, SlugifyConfig.DEFAULT_CONFIG);
	}

	/**
	 * Converts the given text to a URL-friendly slug using custom configuration
	 *
	 * @param text
	 *            The input text to convert
	 * @param config
	 *            The configuration object specifying:
	 *            <ul>
	 *            <li>Character replacements</li>
	 *            <li>Case conversion</li>
	 *            <li>Whitespace handling</li>
	 *            <li>Edge trimming</li>
	 *            </ul>
	 * @return The generated slug according to the specified configuration
	 * @throws NullPointerException
	 *             if either text or config is null
	 */
	public static String slugify(String text, SlugifyConfig config)
	{
		Objects.requireNonNull(text);
		Objects.requireNonNull(config);

		String slug = text;

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