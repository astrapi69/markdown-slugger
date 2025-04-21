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
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Utility class for Markdown-related file I/O operations
 */
public class MarkdownFileUtils
{

	/**
	 * Reads all lines from a Markdown file
	 *
	 * @param path
	 *            the path to the Markdown file
	 * @return list of lines from the file
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static List<String> readMarkdown(Path path) throws IOException
	{
		return Files.readAllLines(path, StandardCharsets.UTF_8);
	}

	/**
	 * Writes lines to a Markdown file with optional overwrite
	 *
	 * @param path
	 *            the target path
	 * @param lines
	 *            the lines to write
	 * @param overwrite
	 *            if true, overwrites the file if it exists
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void writeMarkdown(Path path, List<String> lines, boolean overwrite)
		throws IOException
	{
		if (Files.exists(path) && !overwrite)
		{
			throw new FileAlreadyExistsException("File exists: " + path);
		}
		Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING);
	}

	/**
	 * Lists all Markdown files in a directory
	 *
	 * @param dir
	 *            the directory to scan
	 * @return list of Markdown file paths
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static List<Path> listMarkdownFiles(Path dir) throws IOException
	{
		if (!Files.isDirectory(dir))
		{
			throw new IllegalArgumentException("Not a directory: " + dir);
		}

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.md"))
		{
			return StreamSupport.stream(stream.spliterator(), false).collect(Collectors.toList());
		}
	}

	/**
	 * Creates a backup copy of the given file
	 *
	 * @param original
	 *            the original file path
	 * @return the path of the backup file
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static Path createBackup(Path original) throws IOException
	{
		Path backup = Paths.get(original.toString() + ".bak");
		return Files.copy(original, backup, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * Writes lines to a file or logs them in dry-run mode
	 *
	 * @param path
	 *            the path to write to
	 * @param lines
	 *            the lines to write
	 * @param dryRun
	 *            if true, does not write to disk but logs what would happen
	 * @throws IOException
	 *             if writing fails
	 */
	public static void writeToFile(Path path, List<String> lines, boolean dryRun) throws IOException
	{
		if (dryRun)
		{
			System.out.println("[Dry Run] Would write to file: " + path);
			lines.forEach(line -> System.out.println("  " + line));
		}
		else
		{
			Files.write(path, lines);
		}
	}

	/**
	 * Processes a markdown file and writes fixed output, optionally as dry-run
	 *
	 * @param inputPath
	 *            the input file
	 * @param outputPath
	 *            the output file
	 * @param dryRun
	 *            if true, does not write to disk
	 * @throws IOException
	 *             if I/O fails
	 */
	public static void processAndWriteMarkdown(Path inputPath, Path outputPath, boolean dryRun)
		throws IOException
	{
		List<String> lines = Files.readAllLines(inputPath);
		Set<String> fragmentIds = MarkdownAnchorFixer.extractFragmentLinks(lines);
		List<String> fixedLines = MarkdownAnchorFixer.addMissingHeadingIds(lines, fragmentIds);
		writeToFile(outputPath, fixedLines, dryRun);
	}

	/**
	 * Reads all lines from the specified file
	 *
	 * @param path
	 *            the file path
	 * @return list of lines
	 * @throws IOException
	 *             if reading fails
	 */
	public static List<String> readLines(Path path) throws IOException
	{
		return Files.readAllLines(path);
	}

	/**
	 * Writes lines to the specified file, overwriting existing content
	 *
	 * @param path
	 *            the file path
	 * @param lines
	 *            the lines to write
	 * @throws IOException
	 *             if writing fails
	 */
	public static void writeLines(Path path, List<String> lines) throws IOException
	{
		Files.write(path, lines);
	}

	/**
	 * Checks if a file exists at the given path
	 *
	 * @param path
	 *            the file path
	 * @return true if file exists, false otherwise
	 */
	public static boolean fileExists(Path path)
	{
		return Files.exists(path);
	}
}
