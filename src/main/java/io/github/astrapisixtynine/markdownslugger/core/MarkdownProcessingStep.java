package io.github.astrapisixtynine.markdownslugger.core;

/**
 * Interface for processing steps that operate on a MarkdownContext Implementations should perform a
 * specific transformation or analysis on the Markdown document (e.g., heading extraction, TOC
 * generation)
 */
public interface MarkdownProcessingStep
{
	/**
	 * Processes and optionally modifies the provided MarkdownContext
	 *
	 * @param context
	 *            the MarkdownContext containing data to be analyzed or transformed
	 */
	void process(MarkdownContext context);
}
