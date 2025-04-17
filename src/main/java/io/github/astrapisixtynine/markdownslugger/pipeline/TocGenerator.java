package io.github.astrapisixtynine.markdownslugger.pipeline;

import io.github.astrapisixtynine.markdownslugger.core.MarkdownContext;
import io.github.astrapisixtynine.markdownslugger.core.MarkdownProcessingStep;

/**
 * Processing step that builds a Markdown table of contents (TOC) Uses heading levels and slugs to
 * create an indented list of links
 */
public class TocGenerator implements MarkdownProcessingStep
{
	/**
	 * Generates a Markdown-formatted table of contents (TOC) Uses the headings, their levels, and
	 * slugs from the context The result is stored in context.toc
	 *
	 * @param context
	 *            the MarkdownContext with heading and slug data
	 */
	@Override
	public void process(MarkdownContext context)
	{
		StringBuilder tocBuilder = new StringBuilder();
		for (int i = 0; i < context.headings.size(); i++)
		{
			String heading = context.headings.get(i);
			String slug = context.slugs.get(i);
			int level = context.headingLevels.get(i);
			String indent = "  ".repeat(Math.max(0, level - 1));
			tocBuilder.append(indent).append("- [").append(heading).append("](#").append(slug)
				.append(")\n");
		}
		context.toc = tocBuilder.toString();
	}
}
