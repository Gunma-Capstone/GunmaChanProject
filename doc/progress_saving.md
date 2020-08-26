# Progress Saving

## Status

Currently, all progress is lost after the game is closed. All game variables that involve progress are stored in `../core/src/asu/gunma/ui/util/AssetManagement/GameAssets.java`.
We eventually hope to connect student profiles to their Google accounts so that progress information can be stored on the cloud 
and persist even after the game is closed.

## Context

It is important for students to be able to come back to the game on any device and have their progress still save.

## Future Plans

Once students can login to their Google account, the following unique information needs to be stored on 
each student's account in order for their game progress to be saved. These variables are currently stored in `GameAssets.java`:

- `level1Stars`, `level2Stars`, `level3Stars`, `level4Stars`, and `level5Stars` store the highest amount of stars that
the student has achieved for each level.

- `localeString` stores the student's prefered language.

- `availableGunmaAnimations` stores the avatar skins the student has unlocked to customize Gunma-Chan.

- `gunmaWalkAnimationActive` stores the student's active avatar skin.

- `userNickname` stores the student's chosen nickname to appear on the leaderboard (this is part of the [Leaderboard PR](https://github.com/sjodon/GunmaChanProject/pull/6) which has not been merged into master due to [memory issues](issue_list.md#overloading-the-memory-capacity)).

- `scores` stores the current high scores and associated nicknames for each level (this is part of the [Leaderboard PR](https://github.com/sjodon/GunmaChanProject/pull/6) which has not been merged into master due to [memory issues](issue_list.md#overloading-the-memory-capacity)).
