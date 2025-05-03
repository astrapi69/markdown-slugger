package io.github.astrapisixtynine.markdownslugger.io;

import io.github.astrapisixtynine.markdownslugger.slug.ReplacementRule;
import io.github.astrapisixtynine.markdownslugger.slug.SlugifyConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplaceFileAndGenerateTocTest {


    @Test
    void testAddMissingHeadingIdsInPlace() throws IOException
    {
        // Arrange
        Path original = Paths.get("src/test/resources/chapter-04.md");

        List<ReplacementRule> defaultReplacementRules = new ArrayList<>(SlugifyConfig.DEFAULT_REPLACEMENT_RULES);
        // Use default config with emoji/symbol replacement
        SlugifyConfig config = SlugifyConfig.builder()
                .replacementRules(defaultReplacementRules) // assuming already extended
                .toLowerCase(true)
                .stripNonAlphanumeric(true)
                .removeAccents(true)
                .collapseDashes(true)
                .whitespaceReplacement("-")
                .trimEdges(true)
                .allowedCharactersRegex("[^a-z0-9\\s-]")
                .build();

        // Act
        MarkdownAnchorFixer.addMissingHeadingIdsInPlace(original, config);

        List<String> toc = MarkdownAnchorFixer.generateMarkdownToc(original, config);

        toc.forEach(System.out::println);

    }


    @Test
    void testGenerateMarkdownToc() throws IOException
    {
        Path original = Paths.get("src/test/resources/chapter-03.md");
        SlugifyConfig config = SlugifyConfig.builder()
                .replacementRules(SlugifyConfig.DEFAULT_REPLACEMENT_RULES)
                .toLowerCase(true)
                .stripNonAlphanumeric(true)
                .removeAccents(true)
                .collapseDashes(true)
                .whitespaceReplacement("-")
                .trimEdges(true)
                .allowedCharactersRegex("[^a-z0-9\\s-]")
                .build();

        List<String> toc = MarkdownAnchorFixer.generateMarkdownToc(original, config);

        // Simple assertion to check known structure
        assertFalse(toc.isEmpty(), "TOC should not be empty");
        assertTrue(
                toc.stream().anyMatch(line -> line.contains("Chapitre 3") && line.contains("utiliser")),
                "Top heading should be present"
        );
        toc.forEach(System.out::println);
    }

}
