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

import io.github.astrapisixtynine.markdownslugger.slug.SlugifyConfig;
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
		Pattern headingPattern = Pattern.compile("^(#{1,6})\\s+(.*)$");

		for (String line : lines)
		{
			Matcher matcher = headingPattern.matcher(line);
			if (matcher.matches())
			{
				String headingText = matcher.group(2).trim();
				// Strip leading icons/symbols like ✎, ✓
				headingText = headingText.replaceAll("^[^\\p{L}\\p{N}]+", "").trim();
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
		return addMissingHeadingIds(lines, ids, SlugifyConfig.DEFAULT_CONFIG);
	}

	/**
	 * Adds missing anchor IDs to headings that are referenced by links but do not yet have an ID
	 *
	 * @param lines
	 *            the original Markdown lines
	 * @param ids
	 *            the set of fragment IDs that should exist
	 * @param config
	 *            the slugify configuration used to normalize the heading text for comparison
	 * @return a list of lines with missing heading IDs injected where appropriate
	 */
	public static List<String> addMissingHeadingIds(List<String> lines, Collection<String> ids,
		SlugifyConfig config)
	{
		List<String> result = new ArrayList<>();
		Pattern headingPattern = Pattern.compile("^(#{1,6})\\s+(.*)$");

		for (String line : lines)
		{
			Matcher matcher = headingPattern.matcher(line);
			if (matcher.matches())
			{
				String headingText = matcher.group(2);
				String slug = SlugifyExtensions.slugify(headingText, config);
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
	 * Reads the content of a Markdown file, adds missing heading IDs, and returns the updated
	 * content
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
		List<String> toFragmentIds = convertToFragmentIds(headings);
		Set<String> fragmentIds = new HashSet<>(toFragmentIds);
		List<String> result = addMissingHeadingIds(markdownLines, fragmentIds);
		return String.join(System.lineSeparator(), result);
	}


	/**
	 * Reads the content of a Markdown file, adds missing heading IDs, and returns the updated
	 * content
	 *
	 * @param path
	 *            the path to the Markdown file
	 * @return the full content of the file with added heading IDs
	 * @throws IOException
	 *             if reading the file fails
	 */
	public static String addMissingHeadingIds(Path path, SlugifyConfig config) throws IOException
	{
		List<String> markdownLines = Files.readAllLines(path);
		List<String> headings = extractHeadingsWithoutHashes(path);
		List<String> toFragmentIds = convertToFragmentIds(headings, config);
		Set<String> fragmentIds = new HashSet<>(toFragmentIds);
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
		return convertToFragmentIds(headings, SlugifyConfig.DEFAULT_CONFIG);
	}

	/**
	 * Converts a list of heading texts to their corresponding slugified fragment IDs
	 *
	 * @param headings
	 *            the list of heading texts (without ##)
	 * @return a list of slugified fragment IDs
	 */
	public static List<String> convertToFragmentIds(List<String> headings, SlugifyConfig config)
	{
		if (headings == null || headings.isEmpty())
		{
			return Collections.emptyList();
		}

		List<String> fragmentIds = new ArrayList<>(headings.size());
		for (String heading : headings)
		{
			String slug = SlugifyExtensions.slugify(heading, config);
			fragmentIds.add(slug);
		}
		return fragmentIds;
	}

	/**
	 * Generates a nested Markdown table of contents (TOC) from headings in a Markdown file
	 *
	 * @param path
	 *            the path to the Markdown file
	 * @param config
	 *            the slugify configuration used to normalize fragment IDs
	 * @return a list of TOC lines as strings
	 * @throws IOException
	 *             if reading the file fails
	 */
	public static List<String> generateMarkdownToc(Path path, SlugifyConfig config)
		throws IOException
	{
		List<String> lines = Files.readAllLines(path);
		List<String> toc = new ArrayList<>();
		Pattern headingPattern = Pattern.compile("^(#{1,6})\\s*(.+?)(\\s*\\{#.*?})?$");

		for (String line : lines)
		{
			Matcher matcher = headingPattern.matcher(line);
			if (matcher.matches())
			{
				String hashes = matcher.group(1);
				String fullHeadingText = matcher.group(2).trim(); // e.g. "✎ Exemple 1 : ..."
				String anchorId = matcher.group(3); // e.g. "{#exemple-1}"

				// Generate slug from heading text if no anchor exists
				String slug;
				if (anchorId != null)
				{
					slug = anchorId.replaceAll("[{}#]", "").trim();
				}
				else
				{
					List<String> headingList = List.of(fullHeadingText);
					List<String> slugified = MarkdownAnchorFixer.convertToFragmentIds(headingList,
						config);
					slug = slugified.get(0);
				}

				int level = hashes.length();
				String indentation = "    ".repeat(level - 1);
				toc.add(String.format("%s- [%s](#%s)", indentation, fullHeadingText, slug));
			}
		}
		return toc;
	}

	/**
	 * Generates a nested Markdown table of contents (TOC) from headings in a Markdown file, without
	 * using regex
	 *
	 * @param path
	 *            the path to the Markdown file
	 * @param config
	 *            the slugify configuration used to normalize fragment IDs
	 * @return a list of TOC lines as strings
	 * @throws IOException
	 *             if reading the file fails
	 */
	public static List<String> generateMarkdownTocWithoutRegex(Path path, SlugifyConfig config)
		throws IOException
	{
		List<String> lines = Files.readAllLines(path);
		return generateMarkdownTocWithoutRegex(lines, config);
	}

	/**
	 * Generates a nested Markdown table of contents (TOC) from headings in a Markdown file, without
	 * using regex
	 *
	 * @param lines
	 *            the lines from the Markdown file
	 * @param config
	 *            the slugify configuration used to normalize fragment IDs
	 * @return a list of TOC lines as strings
	 */
	public static List<String> generateMarkdownTocWithoutRegex(List<String> lines,
		SlugifyConfig config)
	{
		List<String> toc = new ArrayList<>();
		for (String line : lines)
		{
			// Trim leading whitespace
			String trimmed = line.stripLeading();
			if (!trimmed.startsWith("#"))
			{
				continue; // Not a heading
			}

			// Count heading level
			int level = 0;
			while (level < trimmed.length() && trimmed.charAt(level) == '#')
			{
				level++;
			}
			if (level == 0 || level > 6 || trimmed.length() <= level
				|| trimmed.charAt(level) != ' ')
			{
				continue; // Not a valid heading (e.g., no space after #s)
			}

			// Extract heading text and optional anchor
			String headingLine = trimmed.substring(level + 1).trim(); // skip space after hashes
			String text;
			String anchorId = null;

			int anchorStart = headingLine.lastIndexOf("{#");
			if (anchorStart != -1 && headingLine.endsWith("}"))
			{
				text = headingLine.substring(0, anchorStart).trim();
				anchorId = headingLine.substring(anchorStart + 2, headingLine.length() - 1); // between
																								// {#
																								// and
																								// }
			}
			else
			{
				text = headingLine;
			}

			// Generate slug if anchor not present
			String slug;
			if (anchorId != null)
			{
				slug = anchorId;
			}
			else
			{
				slug = MarkdownAnchorFixer.convertToFragmentIds(List.of(text), config).get(0);
			}

			String indentation = "    ".repeat(level - 1);
			toc.add(String.format("%s- [%s](#%s)", indentation, text, slug));
		}
		return toc;
	}

	/**
	 * Adds a missing heading ID to a single Markdown heading line if not present
	 *
	 * @param line
	 *            the Markdown line to process
	 * @param config
	 *            the slugify configuration used for generating fragment IDs
	 * @return the line with an added heading ID if it was missing, otherwise unchanged
	 */
	public static String addMissingHeadingIdToLine(String line, SlugifyConfig config)
	{
		if (!line.startsWith("#"))
		{
			return line;
		}
		if (line.contains("{#"))
		{
			return line;
		}
		String headingText = line.replaceAll("^#+", "").trim();
		String slug = convertToFragmentIds(List.of(headingText), config).get(0);
		return line + " {#" + slug + "}";
	}


	/**
	 * Reads the content of a Markdown file, adds missing heading IDs, and overwrites the file with
	 * the updated content
	 *
	 * @param path
	 *            the path to the Markdown file
	 * @param config
	 *            the slugify configuration used for generating fragment IDs
	 * @throws IOException
	 *             if reading or writing the file fails
	 */
	public static void addMissingHeadingIdsInPlace(Path path, SlugifyConfig config)
		throws IOException
	{
		List<String> lines = Files.readAllLines(path);
		List<String> result = new ArrayList<>(lines.size());

		for (String line : lines)
		{
			result.add(addMissingHeadingIdToLine(line, config));
		}

		Files.write(path, result);
	}

}
