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
