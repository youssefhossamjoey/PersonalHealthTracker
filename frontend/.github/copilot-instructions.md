## Purpose

This file gives an AI coding agent the minimal, concrete context needed to be productive in this repo: the app shape, key files, developer commands, and project-specific conventions discovered in the codebase.

## Quick snapshot

- Type: Single-page React app using Vite (ES modules, JSX files under `src/`).
- Dev server: Vite; configured to run on port 3000 (`vite.config.js`).
- Entry: `src/main.jsx` mounts `<App />` into `#root` via `createRoot`.
- Linting: ESLint configured in `eslint.config.js` (note `dist` is ignored); run `npm run lint`.

## Quickstart (PowerShell examples)

```powershell
# Start dev server with HMR (port 3000)
npm run dev

# Build production bundle
npm run build

# Preview a local production build
npm run preview

# Run ESLint
npm run lint
```

## Files to read first

- `index.html` – app shell; loads `/src/main.jsx`.
- `src/main.jsx` – app entry using `createRoot` and `StrictMode`.
- `src/App.jsx` – top-level component; simple useState example and how assets/CSS are imported.
- `vite.config.js` – Vite plugins and `server.port` (3000).
- `eslint.config.js` – lint rules and global ignores.
- `package.json` – scripts and dependencies.

## Architecture & intent

This is a small, client-only React SPA scaffolded with Vite for quick iteration. There are no backend/API integration files in this repository. The design choices are minimal and deliberate: fast HMR (Vite + @vitejs/plugin-react), ES modules, and a single entry point.

## Concrete patterns & examples

- Entry render: `src/main.jsx` mounts the app:

  createRoot(document.getElementById('root')).render(
    <StrictMode>
      <App />
    </StrictMode>
  )

- Component example: `src/App.jsx` uses hooks and default export:

  function App() {
    const [count, setCount] = useState(0)
    return (/* JSX */)
  }
  export default App

- Asset imports: two patterns are used. Use module imports for local assets (e.g. `import reactLogo from './assets/react.svg'`) and absolute root paths for files in `public/` (e.g. `/vite.svg`).

## ESLint and code style

- Lint config: `eslint.config.js` applies to `**/*.{js,jsx}` and includes `react-hooks` and `react-refresh` configs. There is a specific rule:
  - `no-unused-vars` ignores variables that match `^[A-Z_]` (useful for global constants or intentionally unused names).

## Build/preview notes

- Production build output is placed in `dist` by Vite (`dist` is globally ignored by ESLint config). After `npm run build`, run `npm run preview` to serve the production bundle locally.

## Missing / absent pieces (observed)

- No tests or test runner present in the repository. If you add tests, also add test script(s) to `package.json` and CI plumbing.
- No router, global state manager, or API client files exist yet — the app is currently a single component scaffold.

## When making changes

- Keep using `.jsx` files and ES module imports.
- If you add TypeScript, update `package.json` and add tsconfig; the repo currently has `@types/*` dev dependencies but is JS-first.
- Run `npm run lint` before committing. Start the dev server to verify HMR and UI changes (`npm run dev`).

## Where to look for more context

- `vite.config.js` (server options, plugins)
- `eslint.config.js` (rules & ignores)
- `src/` for component patterns and asset imports

If any of these sections are unclear or you want more detail (for example: adding routing, tests, or CI steps), tell me which area to expand and I'll update this file.
