<div align="center">

[![Build Status](https://github.com/astrapi69/markdown-slugger/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/markdown-slugger/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/markdown-slugger/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/markdown-slugger)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/markdown-slugger.svg?style=flat)](https://github.com/astrapi69/markdown-slugger/issues)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/markdown-slugger?style=plastic)](https://search.maven.org/artifact/io.github.astrapi69/markdown-slugger)
[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/markdown-slugger.svg)](http://www.javadoc.io/doc/io.github.astrapi69/markdown-slugger)
[![MIT License](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Donate](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)
[![Hits Of Code](https://hitsofcode.com/github/astrapi69/markdown-slugger?branch=develop)](https://hitsofcode.com/github/astrapi69/markdown-slugger/view?branch=develop)

</div>

# markdown-slugger

**markdown-slugger** is a lightweight and extensible Java library for processing Markdown files.
It extracts headings, generates URL-friendly slugs, builds nested Tables of Contents (TOC), and injects anchor IDs for
compatibility with tools like Pandoc and EPUB.

### ✨ New Features

* 🧠 `addMissingHeadingIdsInPlace` automatically adds `{#...}` fragments in-place to any Markdown file
* 🔍 `generateMarkdownToc` builds a nested TOC structure with slugified anchors
* 🪄 `generateMarkdownTocWithoutRegex` for performance-optimized TOC generation using plain text parsing
* 🧪 Fine-grained test coverage including single-line slug injection testing

The current version includes core functionality with packages:

* `core` – for the processor and context
* `pipeline` – for modular processing steps
* `slug` – for configurable slugification logic
* `io` – for file utilities and TOC/anchor operations

📚 [Usage documentation is available on the wiki »](https://github.com/astrapi69/markdown-slugger/wiki)

---

> 💡 Please support this project by giving it a GitHub star ⭐
> Share it with your friends and colleagues — every bit helps!

---

## Quick Start

Here’s a short snippet to generate a TOC from a Markdown string:

```java
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

MarkdownContext context = new MarkdownContext();
context.originalContent =Files.

readString(Path.of("README.md"));
        MarkdownProcessor.

defaultPipeline(config).

process(context);

System.out.

println(context.toc);
```

To fix headings in a file:

```java
MarkdownAnchorFixer.addMissingHeadingIdsInPlace(Paths.get("chapter-04.md"),config);
```

To extract TOC:

```java
List<String> toc = MarkdownAnchorFixer.generateMarkdownToc(Paths.get("chapter-04.md"), config);
toc.

forEach(System.out::println);
```

---

## Import to Your Project

<details>
  <summary><b>Gradle (click to expand)</b></summary>

### Gradle dependency (recommended)

```kotlin
implementation("io.github.astrapi69:markdown-slugger:${latestVersion}")
```

With a `libs.versions.toml` catalog:

```toml
[versions]
markdown-slugger-version = "${latestVersion}"

[libraries]
markdown-slugger = { module = "io.github.astrapi69:markdown-slugger", version.ref = "markdown-slugger-version" }
```

Then use:

```kotlin
implementation(libs.markdown.slugger)
```

</details>

<details>
  <summary><b>Maven (click to expand)</b></summary>

### Maven dependency

```xml

<dependency>
    <groupId>io.github.astrapi69</groupId>
    <artifactId>markdown-slugger</artifactId>
    <version>${markdown-slugger.version}</version>
</dependency>
```

</details>

<details>
  <summary><b>Snapshots</b></summary>

Add this to your `repositories` block to use snapshots:

```groovy
maven {
    name = "Sonatype Snapshots"
    url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    mavenContent {
        snapshotsOnly()
    }
}
```

</details>

---

## 📚 Articles & Tutorials

Learn how this library was designed and built step-by-step:

*
✍️ [⚙️ Build a Java Markdown Library with ChatGPT](https://medium.com/@asterios-raptis/%EF%B8%8F-build-a-java-markdown-library-with-chatgpt-73e71c547dd6)
*Discover how the `markdown-slugger` project started with collaborative AI-driven development.*

*
✍️ [🧱 From Idea to Code: Implementing File I/O Utilities with Lombok and Clean Markdown Processing](https://medium.com/@asterios-raptis/%EF%B8%8F-from-idea-to-code-implementing-file-i-o-utilities-with-lombok-and-a-clean-markdown-processing-a4e528b1db45)
*Explore how file processing, heading injection, and clean architecture were implemented using modern Java practices.*

---

## Roadmap

* [x] Slug generation with flexible config
* [x] Heading extraction and level tracking
* [x] Markdown TOC generation
* [x] Anchor ID injection (for Pandoc/EPUB compatibility)
* [x] File I/O utilities with in-place
  replacement[Issue: Add reusable File I/O utilities for markdown processing](https://github.com/astrapi69/markdown-slugger/issues/1)
* [x] Regex-free TOC/heading processing
- [ ] CLI
  interface [Issue: Add CLI tool for processing markdown files](https://github.com/astrapi69/markdown-slugger/issues/2)
- [ ] HTML anchor
  inspection [Issue: HTML anchor inspection for broken internal links](https://github.com/astrapi69/markdown-slugger/issues/3)
- [ ] Custom slug presets (
  GitHub/Pandoc) [Issue: Support custom slug presets (GitHub/Pandoc/etc)](https://github.com/astrapi69/markdown-slugger/issues/4)

---

## License

markdown-slugger is released under the [MIT License](https://opensource.org/licenses/MIT).

---

## Donations

If this library saves you time or you like it, consider supporting via:

* [PayPal 💖](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

---

## Contributing

Pull requests, bug reports, and stars are always welcome!

* [Create an Issue](https://github.com/astrapi69/markdown-slugger/issues)
* [Fork the repo](https://github.com/astrapi69/markdown-slugger/fork)
* [Submit a Pull Request](https://github.com/astrapi69/markdown-slugger/pull/new/develop)

Don't forget to add unit tests when you contribute 🧪

---

## Credits

Huge thanks to:

* [Sonatype OSS](https://oss.sonatype.org) for hosting
* [Javadoc.io](https://javadoc.io) for documentation support
* [Codecov](https://codecov.io) for coverage tracking

---
