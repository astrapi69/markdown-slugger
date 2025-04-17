package io.github.astrapisixtynine.markdownslugger.core;

import io.github.astrapisixtynine.markdownslugger.pipeline.AnchorIdInjector;
import io.github.astrapisixtynine.markdownslugger.pipeline.HeadingExtractor;
import io.github.astrapisixtynine.markdownslugger.pipeline.TocGenerator;
import io.github.astrapisixtynine.markdownslugger.slug.SlugMapper;
import io.github.astrapisixtynine.markdownslugger.slug.SlugStrategy;

import java.util.ArrayList;
import java.util.List;

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
