## Change log
----------------------

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
