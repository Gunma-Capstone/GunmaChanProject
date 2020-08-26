# Japanese Font

## Status

Currently, all text in the game can be displayed in Japanese by selecting the language in settings. The font, however, was set up specifically for the current character set used, but any characters not used in `MyResources_jp.java` will not be displayed. 

## Context

When adding the localization functionality, we had a lot of difficulty getting japanese characters to display. The words on the flashcards are displayed in `GlyphLayout`s, but Japanese text in other formats like buttons or labels does not show up.

## Decision

After a lot of research, the solution we came to was to create our own font using [`Hiero`](https://github.com/libgdx/libgdx/wiki/Hiero) which is an application that allows you to create a custom font. The only way to get the Japanese characters to show was to copy all characters used in `MyResources_jp.java` into the sample text of `Hiero`. This creates a font that will display these characters even in `LibGDX` buttons and labels. 

## Consequences

This solution requires a new custom font to be created every time new text to be localized is added to the game. This is obviously not ideal, but it is the only solution we have found so far.
