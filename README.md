# Core Async Quest

[![CircleCI](https://circleci.com/gh/punit-naik/core-async-quest/tree/main.svg?style=svg)](https://circleci.com/gh/punit-naik/core-async-quest/tree/main)
[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.punit-naik/core-async-quest.svg)](https://clojars.org/org.clojars.punit-naik/core-async-quest)

A small, visual learning lab for Clojure(script) `core.async`. It uses ClojureScript for the interactive experience, while teaching concepts that apply to both Clojure and ClojureScript.

## Chapter 1 — Messages, not waiting

Chapter 1 is an orientation to `core.async`. It explains:

- what `core.async` is and why message passing is useful;
- processes, channels, and messages;
- a visual, unbuffered handoff from producer to consumer;
- real-world case studies: responsive search, background email, data imports, and upload cancellation.

The chapter deliberately leaves `go` blocks, `>!`, `<!`, buffers, and back-pressure for later lessons.

## Run it

Compile once:

```sh
lein cljsbuild once dev
```

Or watch source changes:

```sh
lein cljsbuild auto dev
```

Serve `resources/public` with a static-file server and open `index.html`.

Build the ClojureScript unit and re-frame integration tests:

```sh
lein cljsbuild once test
```

Serve `resources/public` and open `test.html`. The browser page runs the ClojureScript test suite and exposes a `passed` or `failed` result in the page and browser console. The tests execute in a browser, not a Node test runner.

## Continuous integration

CircleCI compiles the browser test bundle, serves `resources/public`, and runs `test.html` in headless Chrome. The build fails unless the page reports a passing result. See [.circleci/config.yml](.circleci/config.yml) for the exact commands.

## GitHub Pages

The [Pages deployment workflow](.github/workflows/deploy-pages.yml) compiles the optimized `prod` build and deploys `resources/public` when changes reach `main`. In the repository's **Settings → Pages**, choose **GitHub Actions** as the publishing source.

## Styling

The lesson uses Reagent inline `:style` maps declared in `core.cljs`; there is no standalone application stylesheet.

## Linting

The project includes [.clj-kondo/config.edn](.clj-kondo/config.edn). It excludes `resources/` from linting because that directory contains static and generated browser assets rather than authored Clojure(source) code. The clj-kondo cache remains local and ignored by Git.

## Project layout

- `src/core_async_quest/core.cljs` — Reagent UI and inline style maps.
- `src/core_async_quest/lesson.cljs` — Chapter 1 content and pure lesson transitions.
- `src/core_async_quest/state.cljs` — re-frame events and subscriptions.
- `test/cljs/core_async_quest` — ClojureScript unit and re-frame integration tests.
- `resources/public/index.html` — browser entry point.
- `resources/public/test.html` — browser test harness used locally and in CI.
- `.clj-kondo/config.edn` — clj-kondo configuration that excludes generated resource files.
- `project.clj` — Leiningen + `cljsbuild`; browser packages are declared through `:npm-deps`. No `deps.edn` or `shadow-cljs.edn`.

## Later visual tools

- **React Flow** (`@xyflow/react`) for draggable process/channel graphs.
- **Motion** for message movement and state transitions.
- **canvas-confetti** for success feedback and challenge completion.

The npm packages are available through `:npm-deps`, but are not yet imported by the small initial lesson.

## License

Copyright © 2026 [Punit Naik](https://github.com/punit-naik)

This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at [http://www.eclipse.org/legal/epl-2.0](http://www.eclipse.org/legal/epl-2.0).

This Source Code may also be made available under the following Secondary Licenses when the conditions for such availability set forth in the Eclipse Public License, v. 2.0 are satisfied: GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version, with the GNU Classpath Exception which is available at [https://www.gnu.org/software/classpath/license.html](https://www.gnu.org/software/classpath/license.html).
