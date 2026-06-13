Responsive utilities for making pages adapt to smaller screens.

Branch name suggestions
- feature/responsive-layouts
- fix/responsive-meta
- chore/add-responsive-utils
- feat/mobile-friendly-pages
- refactor/responsive-css

What I added
- `client-extensions/responsive-utils/assets/responsive.css` — utility CSS with container, grid, and breakpoint helpers.

How to use
- Prefer adding this CSS into a theme's global CSS or a Client Extension that your pages load.
- To include from a client extension static path, add a link to `/o/client-extensions/responsive-utils/assets/responsive.css` (or use your bundling pipeline).

Example HTML usage
```html
<link rel="stylesheet" href="/o/client-extensions/responsive-utils/assets/responsive.css">
<div class="container">
  <div class="row">
    <div class="col col-6">Left column</div>
    <div class="col col-6">Right column</div>
  </div>
</div>
```

Quick tips
- Use `.responsive-img` (or simply rely on the fluid `img` rule) for media.
- Add `.stack-on-mobile` to `.row` to force columns to stack on narrow screens.
- Use `.hide-on-mobile` for secondary elements you want hidden on phones.

Next steps I can take
- Scan the repo for page templates or modules that render HTML and apply the utilities automatically.
- Create a small client-extension `client-extension.yaml` + `assets/index.js` that injects the stylesheet into Liferay pages.

Tell me which pages or modules you want updated and I can patch them to use these utilities.
