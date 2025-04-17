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
