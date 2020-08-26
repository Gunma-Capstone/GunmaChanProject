# Asset Management

## Context

We decided to globalize asset variables so that, as the application grows, we can more easily change asset images. This was also required to implement seasonal asset changes.

## Status

Currently, a `GameAssets` object is initialized in the first screen and passed throughout the game as a parameter in each screen. This is for the purpose of saving changes to the object which is necessary for [saving the game progress](progress_saving.md) while the game is open. In the future, if [specific variables](progress_saving.md#future-plans) are saved to users' Google accounts, it will no longer be necessary to pass a `GameAssets` object throughout the screens. `GameAssets` can instead be made static and called like `GameAssets.asset_name`.

## Structure

The following is the structure of `../core/src/asu/gunma/ui/util/AssetManagement`:

```bash
├── AssetManagement
│   ├── monthlyAssets
│   │   ├── Month.java
│   │   ├── January.java
│   │   ├── February.java
│   │   ├── March.java
│   │   ├── April.java
│   │   ├── May.java
│   │   ├── June.java
│   │   ├── July.java
│   │   ├── August.java
│   │   ├── September.java
│   │   ├── October.java
│   │   ├── November.java
│   │   └── December.java
│   ├── seasonalAssets
│   │   ├── Season.java
│   │   ├── Spring.java
│   │   ├── Summer.java
│   │   ├── Fall.java
│   │   └── Winter.java
│   ├── GameAssets.java
│   ├── MyResources.java
└── └── MyResources_jp.java
```

Most asset paths are listed in `GameAssets.java`. Any seasonal or monthly assets are located in their respective season or month files. To create a new asset path, see instructions and examples in `GameAssets.java`, `Month.java`, and `Season.java`. 

All asset paths in the project are saved here so that individual calls to those paths are simply `gameAssets.asset_path_name`.
