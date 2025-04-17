package io.github.astrapisixtynine.markdownslugger.core;

import java.util.ArrayList;
import java.util.List;

/**
 * MarkdownContext holds the state used and modified throughout the Markdown processing pipeline
 *
 * It contains the original Markdown content, extracted heading texts and their levels, generated
 * slugs, and a generated table of contents. This object is passed through each processing step and
 * enriched with relevant data.
 */
public class MarkdownContext
{
	/**
	 * The original Markdown content to be processed
	 */
	public String originalContent;

	/**
	 * A list of all heading texts extracted from the Markdown content
	 */
	public List<String> headings = new ArrayList<>();

	/**
	 * A list of heading levels corresponding to each heading (e.g., 1 for '#', 2 for '##')
	 */
	public List<Integer> headingLevels = new ArrayList<>();

	/**
	 * A list of slugs generated from each heading, used for anchors and links
	 */
	public List<String> slugs = new ArrayList<>();

	/**
	 * The generated table of contents in Markdown format
	 */
	public String toc = "";
}
