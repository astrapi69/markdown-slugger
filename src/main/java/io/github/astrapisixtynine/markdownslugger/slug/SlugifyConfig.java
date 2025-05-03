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

import java.util.List;

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
	 * Default list of common diacritic character replacements for slugification
	 *
	 * <p>
	 * Includes mappings for:
	 * <ul>
	 * <li>German umlauts (ä, ö, ü, ß)</li>
	 * <li>Common accented characters (à, á, â, ã, etc.)</li>
	 * <li>Special characters (ç)</li>
	 * </ul>
	 */
	public static final List<ReplacementRule> DEFAULT_REPLACEMENT_RULES = List.of(
			// Accented characters (lowercase and uppercase)
			new ReplacementRule("à", "a", false), new ReplacementRule("À", "A", false),
			new ReplacementRule("â", "a", false), new ReplacementRule("Â", "A", false),
			new ReplacementRule("ä", "a", false), new ReplacementRule("Ä", "A", false),
			new ReplacementRule("á", "a", false), new ReplacementRule("Á", "A", false),
			new ReplacementRule("ã", "a", false), new ReplacementRule("Ã", "A", false),

			new ReplacementRule("é", "e", false), new ReplacementRule("É", "E", false),
			new ReplacementRule("è", "e", false), new ReplacementRule("È", "E", false),
			new ReplacementRule("ê", "e", false), new ReplacementRule("Ê", "E", false),
			new ReplacementRule("ë", "e", false), new ReplacementRule("Ë", "E", false),

			new ReplacementRule("î", "i", false), new ReplacementRule("Î", "I", false),
			new ReplacementRule("ï", "i", false), new ReplacementRule("Ï", "I", false),

			new ReplacementRule("ô", "o", false), new ReplacementRule("Ô", "O", false),
			new ReplacementRule("ö", "o", false), new ReplacementRule("Ö", "O", false),
			new ReplacementRule("ó", "o", false), new ReplacementRule("Ó", "O", false),

			new ReplacementRule("ù", "u", false), new ReplacementRule("Ù", "U", false),
			new ReplacementRule("û", "u", false), new ReplacementRule("Û", "U", false),
			new ReplacementRule("ü", "u", false), new ReplacementRule("Ü", "U", false),

			new ReplacementRule("ç", "c", false), new ReplacementRule("Ç", "C", false),

			// Emojis and symbols (replaced with "")
			new ReplacementRule("✅", "", false),
			new ReplacementRule("✨", "", false),
			new ReplacementRule("🧠", "", false),
			new ReplacementRule("📌", "", false),
			new ReplacementRule("🚀", "", false),
			new ReplacementRule("🎯", "", false),
			new ReplacementRule("👋", "", false),
			new ReplacementRule("🛠️", "", false),
			new ReplacementRule("⚙️", "", false),
			new ReplacementRule("🛠", "", false),
			new ReplacementRule("🧩", "", false),
			new ReplacementRule("🤖", "", false),
			new ReplacementRule("🔎", "", false),
			new ReplacementRule("✍️", "", false),
			new ReplacementRule("📝", "", false),
			new ReplacementRule("✏️", "", false),
			new ReplacementRule("🔁", "", false),
			new ReplacementRule("💡", "", false),
			new ReplacementRule("💬", "", false),
			new ReplacementRule("🧾", "", false),
			new ReplacementRule("⚠️", "", false),
			new ReplacementRule("🚫", "", false),
			new ReplacementRule("🧱", "", false),
			new ReplacementRule("🧳", "", false),
			new ReplacementRule("❌", "", false),
			new ReplacementRule("⏱", "", false),
			new ReplacementRule("🎨", "", false),
			new ReplacementRule("📧", "", false),
			new ReplacementRule("🔑", "", false),
			new ReplacementRule("🧑‍💻", "", false),
			new ReplacementRule("🧑‍", "", false),
			new ReplacementRule("💼", "", false),
			new ReplacementRule("🌐", "", false),
			new ReplacementRule("📎", "", false),
			new ReplacementRule("🧍", "", false),
			new ReplacementRule("👩‍💼", "", false),
			new ReplacementRule("👨‍👩‍👧‍👦", "", false),
			new ReplacementRule("📣", "", false),
			new ReplacementRule("📖", "", false),
			new ReplacementRule("🔍", "", false),
			new ReplacementRule("📋", "", false),
			new ReplacementRule("🗝", "", false),
			new ReplacementRule("📘", "", false),
			new ReplacementRule("🧭", "", false),
			new ReplacementRule("🆕", "", false),
			new ReplacementRule("⏰", "", false),
			new ReplacementRule("🎭", "", false),
			new ReplacementRule("⛔", "", false),
			new ReplacementRule("⏩", "", false),
			new ReplacementRule("🔗", "", false),
			new ReplacementRule("📚", "", false),
			new ReplacementRule("⏲", "", false),
			new ReplacementRule("👜", "", false),
			new ReplacementRule("💻", "", false),
			new ReplacementRule("⧖", "", false)
	);


	/**
	 * Default configuration for slugification
	 *
	 * <p>
	 * Includes:
	 * <ul>
	 * <li>Default character replacements for diacritics and special characters</li>
	 * <li>Lowercase conversion</li>
	 * <li>No stripping of non-alphanumeric characters (unless configured)</li>
	 * <li>Whitespace replacement with hyphens</li>
	 * <li>Trimming of leading and trailing separators</li>
	 * <li>Collapse of multiple consecutive hyphens</li>
	 * </ul>
	 */
	public static final SlugifyConfig DEFAULT_CONFIG = SlugifyConfig.builder()
		.replacementRules(DEFAULT_REPLACEMENT_RULES) // now using the List<ReplacementRule>
		.toLowerCase(true).stripNonAlphanumeric(false).whitespaceReplacement("-").trimEdges(true)
		.removeAccents(false).collapseDashes(true).allowedCharactersRegex("[^a-z0-9\\s-]").build();

	List<ReplacementRule> replacementRules;

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
