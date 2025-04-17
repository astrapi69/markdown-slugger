package io.github.astrapisixtynine.markdownslugger.slug;

import io.github.astrapisixtynine.markdownslugger.core.MarkdownContext;
import io.github.astrapisixtynine.markdownslugger.core.MarkdownProcessingStep;

/**
 * Processing step that generates slugs from extracted headings Uses the configured SlugStrategy to
 * create a list of slugs and stores them in the context
 */
public class SlugMapper implements MarkdownProcessingStep
{
	/**
	 * The strategy used to convert headings into URL-friendly slugs This allows customizable slug
	 * generation logic (e.g. GitHub-style, custom rules)
	 */
	private final SlugStrategy slugStrategy;

	/**
	 * Constructs a SlugMapper with the specified SlugStrategy
	 *
	 * @param slugStrategy
	 *            the strategy to use for generating slugs from headings
	 */
	public SlugMapper(SlugStrategy slugStrategy)
	{
		this.slugStrategy = slugStrategy;
	}

	/**
	 * Maps each extracted heading to a slug using the configured strategy Stores the slugs in the
	 * context
	 *
	 * @param context
	 *            the MarkdownContext with extracted headings
	 */
	@Override
	public void process(MarkdownContext context)
	{
		for (String heading : context.headings)
		{
			context.slugs.add(slugStrategy.toSlug(heading));
		}
	}
}
