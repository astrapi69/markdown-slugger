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

import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * Configuration class for controlling how Markdown headings are converted into slugs
 *
 * Provides fine-grained control over character replacements, case transformation, accent removal,
 * whitespace handling, character filtering, and formatting behavior
 */
@Data
@Builder
public class SlugifyConfig
{

	/**
	 * Default map of common diacritic characters to ASCII replacements for slugification
	 * <p>
	 * Includes mappings for:
	 * <ul>
	 * <li>German umlauts (ä, ö, ü, ß)</li>
	 * <li>Common accented characters (à, á, â, ã, etc)</li>
	 * <li>Special characters (ç)</li>
	 * </ul>
	 */
	public static final Map<String, String> DEFAULT_REPLACEMENTS = Map.ofEntries(
		Map.entry("à", "a"), Map.entry("â", "a"), Map.entry("ä", "a"), Map.entry("á", "a"),
		Map.entry("ã", "a"),

		Map.entry("é", "e"), Map.entry("è", "e"), Map.entry("ê", "e"), Map.entry("ë", "e"),

		Map.entry("î", "i"), Map.entry("ï", "i"),

		Map.entry("ô", "o"), Map.entry("ö", "o"), Map.entry("ó", "o"),

		Map.entry("ù", "u"), Map.entry("û", "u"), Map.entry("ü", "u"),

		Map.entry("ç", "c"));


	/**
	 * Default configuration for slugification
	 * <p>
	 * Includes:
	 * <ul>
	 * <li>Default character replacements</li>
	 * <li>Lowercase conversion</li>
	 * <li>Non-alphanumeric stripping</li>
	 * <li>Whitespace replacement with hyphens</li>
	 * <li>Edge trimming</li>
	 * </ul>
	 */
	public static final SlugifyConfig DEFAULT_CONFIG = SlugifyConfig.builder()
		.replacements(DEFAULT_REPLACEMENTS).toLowerCase(true).stripNonAlphanumeric(false)
		.whitespaceReplacement("-").trimEdges(true).removeAccents(false).collapseDashes(true)
		.allowedCharactersRegex("[^a-z0-9\\s-]").build();

	/**
	 * Map of custom character replacements (e.g., ä → ae, é → e, ü → ue) -- GETTER --
	 *
	 * @return the map of custom character replacements
	 */
	private final Map<String, String> replacements;

	/**
	 * Whether to convert the resulting slug to lowercase -- GETTER --
	 *
	 * @return true if output should be converted to lowercase
	 */
	private final boolean toLowerCase;

	/**
	 * Whether to remove characters not matching the allowed regex -- GETTER --
	 *
	 * @return true if disallowed characters should be removed
	 */
	private final boolean stripNonAlphanumeric;

	/**
	 * The character used to replace whitespace (e.g., '-') -- GETTER --
	 *
	 * @return the character used to replace whitespace
	 */
	private final String whitespaceReplacement;

	/**
	 * Whether to trim leading and trailing separator characters -- GETTER --
	 *
	 * @return true if leading and trailing separators should be trimmed
	 */
	private final boolean trimEdges;

	/**
	 * Whether to remove accents using Unicode normalization (e.g., é → e) -- GETTER --
	 *
	 * @return true if accents should be removed
	 */
	private final boolean removeAccents;

	/**
	 * Whether to collapse multiple consecutive separators into one (e.g., -- → -) -- GETTER --
	 *
	 * @return true if multiple separators should be collapsed into one
	 */
	private final boolean collapseDashes;

	/**
	 * Regular expression that defines which characters are disallowed Characters matching this
	 * pattern are removed if {@code stripNonAlphanumeric} is true -- GETTER --
	 *
	 * @return the regex pattern for disallowed characters
	 * 
	 */
	private final String allowedCharactersRegex;

}
