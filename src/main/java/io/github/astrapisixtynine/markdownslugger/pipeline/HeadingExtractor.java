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