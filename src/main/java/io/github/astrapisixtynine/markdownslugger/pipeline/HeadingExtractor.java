package io.github.astrapisixtynine.markdownslugger.pipeline;

import io.github.astrapisixtynine.markdownslugger.core.MarkdownContext;
import io.github.astrapisixtynine.markdownslugger.core.MarkdownProcessingStep;

/**
 * Processing step that extracts headings from the Markdown content Stores heading texts and their
 * corresponding levels in the context
 */
public class HeadingExtractor implements MarkdownProcessingStep
{

	/**
	 * Extracts headings and their levels from the Markdown content Updates the context with lists
	 * of heading texts and their levels
	 *
	 * @param context
	 *            the MarkdownContext containing the original Markdown content
	 */
	@Override
	public void process(MarkdownContext context)
	{
		String[] lines = context.originalContent.split("\n");
		for (String line : lines)
		{
			String trimmed = line.trim();
			if (trimmed.matches("^#{1,6} .+"))
			{
				int level = 0;
				while (level < trimmed.length() && trimmed.charAt(level) == '#')
				{
					level++;
				}
				context.headingLevels.add(level);
				context.headings.add(trimmed.replaceFirst("^#+ ", ""));
			}
		}
	}
}