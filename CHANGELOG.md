# Changelog

All notable changes to this project will be documented in this file.

## [0.3.0] - 2026-09-01

### Added

- Chapter 2, “Go, then park,” covering `go`, `<!`, `>!`, parking,
  resumption, and the limits of go-block coordination.
- Top chapter controls and a bottom previous/next pager with the current
  chapter number.
- A versioned pre-commit hook that runs cljstyle from the Leiningen `+dev`
  profile.

## [0.2.0] - 2026-09-01

### Added

- Chapter 1, “Messages, not waiting,” an introductory `core.async` lesson
  covering processes, channels, messages, and an unbuffered handoff simulation.
- Detailed application case studies for responsive search, background email,
  data imports, and upload cancellation.
- ClojureScript unit tests and re-frame integration tests, plus a browser test
  harness.
- Headless-Chrome CircleCI test execution and browser tooling setup.
- Project-level clj-kondo configuration that excludes generated and static files
  under `resources/` from linting.
- GitHub Actions workflow that compiles and deploys `resources/public` to GitHub
  Pages.

### Changed

- Reworked the lesson copy to apply to both Clojure and ClojureScript, while
  reserving `go` blocks, put/take operations, buffers, and back-pressure for
  later chapters.
- Moved application styling from a standalone stylesheet to inline Reagent style
  maps.
- Added a dedicated `cljsbuild` test build that produces a browser test bundle.

## [0.1.0] - 2026-09-01

### Added

- Initial project scaffolding.
