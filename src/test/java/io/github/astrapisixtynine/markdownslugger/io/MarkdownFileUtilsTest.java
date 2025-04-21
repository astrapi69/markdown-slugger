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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for {@link MarkdownFileUtils} Verifies correct reading, writing, and existence
 * checking of markdown files
 */
class MarkdownFileUtilsTest
{

	/** Path to the input test file */
	private Path inputFile;

	/** Path to the output file created during tests */
	private Path outputFile;

	/**
	 * Sets up test paths before each test method Initializes input and output file paths
	 */
	@BeforeEach
	void setUp()
	{
		inputFile = Paths.get("src/test/resources/test_input.md");
		outputFile = Paths.get("src/test/resources/test_output.md");
	}

	/**
	 * Deletes the output file after each test if it exists
	 *
	 * @throws IOException
	 *             if deletion fails
	 */
	@AfterEach
	void tearDown() throws IOException
	{
		if (outputFile != null && outputFile.toFile().exists())
		{
			java.nio.file.Files.delete(outputFile);
		}
	}

	/**
	 * Tests {@link MarkdownFileUtils#readLines(Path)} Ensures lines are read from a markdown file
	 * and contain expected content
	 *
	 * @throws IOException
	 *             if file reading fails
	 */
	@Test
	void testReadLines() throws IOException
	{
		List<String> lines = MarkdownFileUtils.readLines(inputFile);
		assertNotNull(lines);
		assertFalse(lines.isEmpty());
		assertTrue(lines.stream().anyMatch(line -> line.contains("Préface")));
	}

	/**
	 * Tests {@link MarkdownFileUtils#writeLines(Path, List)} Writes content to a file and verifies
	 * the content was written correctly
	 *
	 * @throws IOException
	 *             if writing or reading the file fails
	 */
	@Test
	void testWriteLines() throws IOException
	{
		List<String> content = List.of("# Written Title", "Line 1", "Line 2");
		MarkdownFileUtils.writeLines(outputFile, content);

		assertTrue(outputFile.toFile().exists());
		List<String> readBack = MarkdownFileUtils.readLines(outputFile);
		assertEquals(content.size(), readBack.size());
		assertEquals("# Written Title", readBack.get(0));
	}

	/**
	 * Tests {@link MarkdownFileUtils#fileExists(Path)} Verifies that file existence is correctly
	 * detected
	 */
	@Test
	void testFileExists()
	{
		assertTrue(MarkdownFileUtils.fileExists(inputFile));
		assertFalse(MarkdownFileUtils.fileExists(Paths.get("nonexistent.md")));
	}

	/**
	 * Tests {@link MarkdownFileUtils#processAndWriteMarkdown(Path, Path, boolean)} in dry-run mode
	 * Ensures the output file is not written and content is processed without side effects
	 *
	 * @throws IOException
	 *             if reading or writing files fails
	 */
	@Test
	void testProcessAndWriteMarkdownDryRun() throws IOException
	{
		Path dryRunInput = Paths.get("src/test/resources/test_input.md");
		Path dryRunOutput = Paths.get("build/tmp/dry_run_output.md");

		// Ensure the output file does not exist before test
		if (Files.exists(dryRunOutput))
		{
			Files.delete(dryRunOutput);
		}

		// Run in dry-run mode
		MarkdownFileUtils.processAndWriteMarkdown(dryRunInput, dryRunOutput, true);

		// Assert output file does not exist after dry-run
		assertFalse(Files.exists(dryRunOutput), "Dry-run should not write to output file");
	}

}
