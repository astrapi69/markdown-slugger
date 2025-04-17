package io.github.astrapisixtynine.markdownslugger.slug;

/**
 * Strategy interface for generating slugs from headings Used to create URL-safe fragment
 * identifiers for Markdown links
 */
public interface SlugStrategy
{
	/**
	 * Converts the given heading text into a URL-friendly slug
	 *
	 * @param heading
	 *            the original heading text
	 * @return a slugified version of the heading suitable for anchor links
	 */
	String toSlug(String heading);
}
