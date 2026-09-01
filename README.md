# Core Async Quest

[![CircleCI](https://circleci.com/gh/punit-naik/core-async-quest/tree/main.svg?style=svg)](https://circleci.com/gh/punit-naik/core-async-quest/tree/main)
[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.punit-naik/core-async-quest.svg)](https://clojars.org/org.clojars.punit-naik/core-async-quest)

A small, visual learning lab for ClojureScript `core.async`. The first scaffold introduces the central metaphor: processes put messages into channels, and a learner advances the simulation one event at a time.

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

## Scaffold contents

- `src/cljs/core_async_quest/core.cljs` — re-frame state and the initial interactive channel lesson.
- `resources/public/index.html` — browser entry point.
- `resources/public/css/app.css` — visual shell.
- `project.clj` — Leiningen + `cljsbuild`; JavaScript packages use `:npm-deps`. No `deps.edn` or `shadow-cljs.edn`.

## Later visual tools

- **React Flow** (`@xyflow/react`) for draggable process/channel graphs.
- **Motion** for message movement and state transitions.
- **canvas-confetti** for success feedback and challenge completion.

The npm packages are available through `:npm-deps`, but are not yet imported by the small initial lesson.

## License

Copyright © 2026 [Punit Naik](https://github.com/punit-naik)

This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at [http://www.eclipse.org/legal/epl-2.0](http://www.eclipse.org/legal/epl-2.0).

This Source Code may also be made available under the following Secondary Licenses when the conditions for such availability set forth in the Eclipse Public License, v. 2.0 are satisfied: GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version, with the GNU Classpath Exception which is available at [https://www.gnu.org/software/classpath/license.html](https://www.gnu.org/software/classpath/license.html).
