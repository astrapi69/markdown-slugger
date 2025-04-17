package io.github.astrapisixtynine.markdownslugger.pipeline;

import io.github.astrapisixtynine.markdownslugger.core.MarkdownContext;
import io.github.astrapisixtynine.markdownslugger.core.MarkdownProcessingStep;

/**
 * Processing step that injects anchor IDs into headings Modifies the Markdown content by appending
 * {#slug} to each heading Ensures internal links work in tools like Pandoc and EPUB readers
 */
public class AnchorIdInjector implements MarkdownProcessingStep
{
	/**
	 * Modifies the Markdown content by appending anchor IDs ({#slug}) to each heading Ensures the
	 * output is compatible with Pandoc and EPUB generators
	 *
	 * @param context
	 *            the MarkdownContext with headings and slugs
	 */
	@Override
	public void process(MarkdownContext context)
	{
		String[] lines = context.originalContent.split("\n");
		StringBuilder modified = new StringBuilder();

		int headingIndex = 0;
		for (String line : lines)
		{
			if (headingIndex < context.headings.size() && line.trim().matches("^#{1,6} .+"))
			{
				String headingText = context.headings.get(headingIndex);
				String slug = context.slugs.get(headingIndex);
				String hashPrefix = line.substring(0, line.indexOf(headingText)).trim();
				modified.append(hashPrefix).append(" ").append(headingText).append(" {#")
					.append(slug).append("}");
				headingIndex++;
			}
			else
			{
				modified.append(line);
			}
			modified.append("\n");
		}
		context.originalContent = modified.toString();
	}
}
