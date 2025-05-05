## Change log
----------------------

Version 1.1-SNAPSHOT
-------------
## [Unreleased]

### Added
- New method `addMissingHeadingIdsInPlace` to inject heading anchor IDs directly into Markdown files
- Support for generating nested Markdown TOCs (Table of Contents) from headings:
  - `generateMarkdownToc` (regex-based)
  - `generateMarkdownTocWithoutRegex` (manual parsing)
- Utility method `addMissingHeadingIdToLine` for individual heading processing
- Unit tests for heading ID injection and TOC generation functionality

### Changed
- Expanded and reformatted `DEFAULT_REPLACEMENT_RULES` in `SlugifyConfig` for better readability
- Normalization logic in `SlugifyExtensions` cleaned up for consistency
- Gradle wrapper updated to `8.14`
- Upgraded `equalsverifier` from `3.19.3` to `4.0`

Version 1.0
-------------

ADDED:

All notable changes to this project will be documented in this file.

## [0.1.0] - 2025-04-17
### Added
- Initial implementation of `markdown-slugger` library
- `core` package with `MarkdownProcessor` and `MarkdownContext`
- `pipeline` steps:
    - `HeadingExtractor`
    - `SlugMapper`
    - `TocGenerator`
    - `AnchorIdInjector`
- `slug` package with `SlugStrategy`, `DefaultSlugStrategy`, and fully configurable `SlugifyConfig`
- Unit tests for default pipeline and slug strategy
- GitHub Actions CI, code coverage, Maven publishing
- Basic README and license



Notable links:
[keep a changelog](http://keepachangelog.com/en/1.0.0/) Don’t let your friends dump git logs into changelogs
