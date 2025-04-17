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
package io.github.astrapisixtynine.markdownslugger.core;

import java.util.ArrayList;
import java.util.List;

import io.github.astrapisixtynine.markdownslugger.pipeline.AnchorIdInjector;
import io.github.astrapisixtynine.markdownslugger.pipeline.HeadingExtractor;
import io.github.astrapisixtynine.markdownslugger.pipeline.TocGenerator;
import io.github.astrapisixtynine.markdownslugger.slug.SlugMapper;
import io.github.astrapisixtynine.markdownslugger.slug.SlugStrategy;

/**
 * MarkdownProcessor executes a pipeline of MarkdownProcessingStep instances Each step modifies or
 * enriches the provided MarkdownContext
 *
 * This processor can be used to extract headings, generate slugs, create a TOC, inject anchor IDs,
 * and more
 */

public class MarkdownProcessor
{
	/**
	 * The ordered list of processing steps that make up the pipeline Each step operates on the
	 * MarkdownContext and may modify or enrich it
	 */
	private final List<MarkdownProcessingStep> steps = new ArrayList<>();

	/**
	 * Adds a new processing step to the Markdown pipeline
	 *
	 * @param step
	 *            the MarkdownProcessingStep to be added
	 * @return the MarkdownProcessor instance, enabling fluent chaining
	 */
	public MarkdownProcessor addStep(MarkdownProcessingStep step)
	{
		steps.add(step);
		return this;
	}

	/**
	 * Runs all configured processing steps in order on the given context
	 *
	 * @param context
	 *            the MarkdownContext to be processed and enriched
	 */
	public void process(MarkdownContext context)
	{
		for (MarkdownProcessingStep step : steps)
		{
			step.process(context);
		}
	}

	/**
	 * Creates a default pipeline of steps using the provided SlugStrategy Includes heading
	 * extraction, slug mapping, TOC generation, and anchor injection
	 *
	 * @param slugStrategy
	 *            the strategy to use for generating slugs
	 * @return a fully configured MarkdownProcessor
	 */
	public static MarkdownProcessor defaultPipeline(SlugStrategy slugStrategy)
	{
		return new MarkdownProcessor().addStep(new HeadingExtractor())
			.addStep(new SlugMapper(slugStrategy)).addStep(new TocGenerator())
			.addStep(new AnchorIdInjector());
	}
}
