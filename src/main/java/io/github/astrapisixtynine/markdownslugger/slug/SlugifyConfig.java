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

/**
 * Configuration class for controlling how Markdown headings are converted into slugs
 *
 * Provides fine-grained control over character replacements, case transformation, accent removal,
 * whitespace handling, character filtering, and formatting behavior
 */
public class SlugifyConfig
{
	/** Map of custom character replacements (e.g., ä → ae, é → e, ü → ue) */
	private final Map<String, String> replacements;

	/** Whether to convert the resulting slug to lowercase */
	private final boolean toLowerCase;

	/** Whether to remove characters not matching the allowed regex */
	private final boolean stripNonAlphanumeric;

	/** The character used to replace whitespace (e.g., '-') */
	private final String whitespaceReplacement;

	/** Whether to trim leading and trailing separator characters */
	private final boolean trimEdges;

	/** Whether to remove accents using Unicode normalization (e.g., é → e) */
	private final boolean removeAccents;

	/** Whether to collapse multiple consecutive separators into one (e.g., -- → -) */
	private final boolean collapseDashes;

	/**
	 * Regular expression that defines which characters are disallowed Characters matching this
	 * pattern are removed if {@code stripNonAlphanumeric} is true
	 */
	private final String allowedCharactersRegex;

	/**
	 * Constructs a new slugification configuration
	 *
	 * @param replacements
	 *            custom character replacements (e.g., ä → ae)
	 * @param toLowerCase
	 *            whether to convert the result to lowercase
	 * @param stripNonAlphanumeric
	 *            whether to remove characters not matching the allowed regex
	 * @param whitespaceReplacement
	 *            character used to replace whitespace
	 * @param trimEdges
	 *            whether to trim leading/trailing separators
	 * @param removeAccents
	 *            whether to remove accents (e.g., é → e)
	 * @param collapseDashes
	 *            whether to collapse multiple separators into one
	 * @param allowedCharactersRegex
	 *            regex pattern for disallowed characters
	 */
	public SlugifyConfig(Map<String, String> replacements, boolean toLowerCase,
		boolean stripNonAlphanumeric, String whitespaceReplacement, boolean trimEdges,
		boolean removeAccents, boolean collapseDashes, String allowedCharactersRegex)
	{
		this.replacements = replacements;
		this.toLowerCase = toLowerCase;
		this.stripNonAlphanumeric = stripNonAlphanumeric;
		this.whitespaceReplacement = whitespaceReplacement;
		this.trimEdges = trimEdges;
		this.removeAccents = removeAccents;
		this.collapseDashes = collapseDashes;
		this.allowedCharactersRegex = allowedCharactersRegex;
	}

	/** @return the map of custom character replacements */
	public Map<String, String> getReplacements()
	{
		return replacements;
	}

	/** @return true if output should be converted to lowercase */
	public boolean isToLowerCase()
	{
		return toLowerCase;
	}

	/** @return true if disallowed characters should be removed */
	public boolean isStripNonAlphanumeric()
	{
		return stripNonAlphanumeric;
	}

	/** @return the character used to replace whitespace */
	public String getWhitespaceReplacement()
	{
		return whitespaceReplacement;
	}

	/** @return true if leading and trailing separators should be trimmed */
	public boolean isTrimEdges()
	{
		return trimEdges;
	}

	/** @return true if accents should be removed */
	public boolean isRemoveAccents()
	{
		return removeAccents;
	}

	/** @return true if multiple separators should be collapsed into one */
	public boolean isCollapseDashes()
	{
		return collapseDashes;
	}

	/** @return the regex pattern for disallowed characters */
	public String getAllowedCharactersRegex()
	{
		return allowedCharactersRegex;
	}
}
