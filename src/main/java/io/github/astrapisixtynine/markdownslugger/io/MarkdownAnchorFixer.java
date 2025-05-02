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
package io.github.astrapisixtynine.markdownslugger.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.astrapisixtynine.markdownslugger.slug.SlugifyExtensions;

/**
 * Utility class for processing Markdown files by fixing missing heading anchor IDs Extracts all
 * internal fragment links, generates slugified IDs, and injects missing anchor IDs into heading
 * lines that reference those links
 */
public class MarkdownAnchorFixer
{

	/**
	 * Extracts all headings (from level 2 to 6) from a Markdown file and removes the hash symbols
	 *
	 * @param path
	 *            the path to the Markdown file
	 * @return a list of heading texts without the leading ## markers
	 * @throws IOException
	 *             if reading the file fails
	 */
	public static List<String> extractHeadingsWithoutHashes(Path path) throws IOException
	{
		List<String> lines = Files.readAllLines(path);
		List<String> headings = new ArrayList<>();
		Pattern headingPattern = Pattern.compile("^(#{2,6})\\s+(.*)$");

		for (String line : lines)
		{
			Matcher matcher = headingPattern.matcher(line);
			if (matcher.matches())
			{
				String headingText = matcher.group(2).trim();
				headings.add(headingText);
			}
		}
		return headings;
	}

	/**
	 * Extracts all internal Markdown fragment link IDs from the given lines
	 *
	 * @param lines
	 *            the list of lines to scan
	 * @return a set of all extracted fragment link IDs (e.g., from [text](#fragment-id))
	 */
	public static Set<String> extractFragmentLinks(List<String> lines)
	{
		Set<String> ids = new HashSet<>();
		Pattern linkPattern = Pattern.compile("\\[[^\\]]+\\]\\(#([^)]+)\\)");
		for (String line : lines)
		{
			Matcher matcher = linkPattern.matcher(line);
			while (matcher.find())
			{
				ids.add(matcher.group(1));
			}
		}
		return ids;
	}

	/**
	 * Adds missing anchor IDs to headings that are referenced by links but do not yet have an ID
	 *
	 * @param lines
	 *            the original Markdown lines
	 * @param ids
	 *            the set of fragment IDs that should exist
	 * @return a list of lines with missing heading IDs injected where appropriate
	 */
	public static List<String> addMissingHeadingIds(List<String> lines, Collection<String> ids)
	{
		List<String> result = new ArrayList<>();
		Pattern headingPattern = Pattern.compile("^(#{2,6})\\s+(.*)$");

		for (String line : lines)
		{
			Matcher matcher = headingPattern.matcher(line);
			if (matcher.matches())
			{
				String headingText = matcher.group(2);
				String slug = SlugifyExtensions.slugify(headingText);
				if (ids.contains(slug) && !line.contains("{#"))
				{
					line += " {#" + slug + "}";
				}
			}
			result.add(line);
		}

		return result;
	}
	/**
	 * Reads the content of a Markdown file, adds missing heading IDs, and returns the updated content
	 *
	 * @param path
	 *            the path to the Markdown file
	 * @return the full content of the file with added heading IDs
	 * @throws IOException
	 *             if reading the file fails
	 */
	public static String addMissingHeadingIds(Path path) throws IOException
	{
		List<String> markdownLines = Files.readAllLines(path);
		List<String> headings = extractHeadingsWithoutHashes(path);
		Set<String> fragmentIds = new HashSet<>(convertToFragmentIds(headings));
		List<String> result = addMissingHeadingIds(markdownLines, fragmentIds);
		return String.join(System.lineSeparator(), result);
	}

	/**
	 * Converts a list of heading texts to their corresponding slugified fragment IDs
	 *
	 * @param headings
	 *            the list of heading texts (without ##)
	 * @return a list of slugified fragment IDs
	 */
	public static List<String> convertToFragmentIds(List<String> headings)
	{
		List<String> fragmentIds = new ArrayList<>();
		for (String heading : headings)
		{
			String slug = SlugifyExtensions.slugify(heading);
			fragmentIds.add(slug);
		}
		return fragmentIds;
	}

}
