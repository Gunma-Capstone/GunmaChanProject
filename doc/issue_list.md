# Current Issues

This is a list of the current issues we are facing:

## Overloading the Memory Capacity

The game currently crashes randomly because too many screens overload the memory. This gets worse with more and more layers of screens. This problem came up towards the end of our time working on the project and we didn't have time to fully research and resolve this problem. It seems like more needs to be done to dispose of previous screens and animations. As the game gets bigger, this will become more of a problem and we aren't sure what the limit is to how many screens can be displayed.

There are a few current PR's that we are not able to merge because they overload the memory with too many screens displayed:

- [Leaderboard](https://github.com/sjodon/GunmaChanProject/pull/6) : This PR displays a leaderboard at the end of each level with the high scores. The added screen of the leaderboard causes the app to crash every time the leaderboard is displayed.

- [Special Rewards Based on User Performance](https://github.com/sjodon/GunmaChanProject/pull/11) : We were able to merge functionality into master that rewards the player with a new special Gunma-Chan skin every time the player gets three stars on a level. This PR also displays the new Gunma-Chan animation on the end of level screen with the stars. We had to remove this part from the other functionality because there is already so many layers of screens at the end of the level.

## Incorrect and Correct Image / Sound Displaying at the Same Time

Sometimes, when the player correctly says the word, the green circle and the red X appear at the same time and the incorrect and correct sound effects sound at the same time. We discovered this issue at the end of our last sprint and were unable to resolve it.
