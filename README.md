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
It extracts headings, generates URL-friendly slugs, builds nested Tables of Contents (TOC), and injects anchor IDs for compatibility with tools like Pandoc and EPUB.

The current version includes core functionality with packages:
- `core` – for the processor and context
- `pipeline` – for modular processing steps
- `slug` – for configurable slugification logic

More features (e.g. file I/O, CLI, HTML export) will come in future releases.

📚 [Usage documentation is available on the wiki »](https://github.com/astrapi69/markdown-slugger/wiki)

---

> 💡 Please support this project by giving it a GitHub star ⭐
> Share it with your friends and colleagues — every bit helps!

---

## Quick Start

Here’s a short snippet to generate a TOC from a Markdown string:

```java
SlugifyConfig config = new SlugifyConfig(
    Map.of("ü", "ue", "ä", "ae"), true, true, "-", true, true, true, "[^a-z0-9\s-]"
);

SlugStrategy slugStrategy = new DefaultSlugStrategy(config);
MarkdownProcessor processor = MarkdownProcessor.defaultPipeline(slugStrategy);

MarkdownContext context = new MarkdownContext();
context.originalContent = Files.readString(Path.of("README.md"));
processor.process(context);

System.out.println(context.toc);
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

## Roadmap

- [x] Slug generation with flexible config
- [x] Heading extraction and level tracking
- [x] Markdown TOC generation
- [x] Anchor ID injection (for Pandoc/EPUB compatibility)
- [ ] File I/O utilities
- [ ] CLI interface
- [ ] HTML anchor inspection
- [ ] Custom slug presets (GitHub/Pandoc)

---

## License

markdown-slugger is released under the [MIT License](https://opensource.org/licenses/MIT).

---

## Donations

If this library saves you time or you like it, consider supporting via:

- [PayPal 💖](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

---

## Contributing

Pull requests, bug reports, and stars are always welcome!

- [Create an Issue](https://github.com/astrapi69/markdown-slugger/issues)
- [Fork the repo](https://github.com/astrapi69/markdown-slugger/fork)
- [Submit a Pull Request](https://github.com/astrapi69/markdown-slugger/pull/new/develop)

Don't forget to add unit tests when you contribute 🧪

---

## Credits

Huge thanks to:
- [Sonatype OSS](https://oss.sonatype.org) for hosting
- [Javadoc.io](https://javadoc.io) for documentation support
- [Codecov](https://codecov.io) for coverage tracking

---
