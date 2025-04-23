package io.github.astrapisixtynine.markdownslugger.slug;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a text replacement rule used during slugification.
 *
 * <p>
 * Each rule consists of a pattern (regex or literal), the string it should be replaced with, and a
 * flag to determine whether the pattern should be interpreted as a regular expression.
 */
@Data
@Builder
public class ReplacementRule
{

	/**
	 * The pattern to match in the input text. Interpreted as either a literal or regex.
	 */
	private String pattern;

	/**
	 * The replacement string to insert where the pattern matches.
	 */
	private String replacement;

	/**
	 * If true, the pattern is interpreted as a regular expression. If false, the pattern is treated
	 * as a literal string.
	 */
	private boolean regex;
}
