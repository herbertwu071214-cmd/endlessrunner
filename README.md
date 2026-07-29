# Endless Runner

A fast-paced endless runner game inspired by Subway Surfers. The player runs forward through three lanes and must dodge obstacles by moving left, moving right, jumping, or sliding. The speed slowly increases, coins appear on the track, and the goal is to survive as long as possible while getting a high score.


## Why I made it

I wanted to make a simple but addictive reflex game. Subway Surfers gave me the main idea: three lanes, quick movement, coins, obstacles, and a speed that keeps getting harder over time.

I also wanted practice building a full game instead of just a small program. This project helped me work on player movement, collision detection, scoring, high scores, obstacle types, coins, keyboard input, and drawing the game screen.


## Tools

Main version: Java 21 + JavaFX. Uses Maven.

There is also an HTML version in one file with plain HTML, CSS, and JavaScript. It does not need any install.


## Run the Java version

Make sure Java 21 is installed. Then run this in the project folder:

- Mac/Linux: `./mvnw javafx:run`
- Windows: `mvnw.cmd javafx:run`


## Run the HTML version

Open `index.html` in any browser.


## Controls

- Left arrow or `A`: move left
- Right arrow or `D`: move right
- Up arrow, `W`, or space: jump
- Down arrow or `S`: slide
- `R`: restart after game over


## Obstacle meanings

- Red obstacle with `X`: do not jump or slide, move to another lane
- Orange obstacle with `^`: jump over it
- Purple obstacle with `v`: slide under it


## How to rebuild it yourself

1. Create a player with three possible lanes.
2. Let the player move left and right between lanes.
3. Add jump and slide actions.
4. Spawn obstacles far ahead of the player.
5. Move obstacles toward the player each frame.
6. Give each obstacle a different rule: avoid, jump, or slide.
7. Check collision only when an obstacle reaches the player.
8. Spawn coins in lanes and let the player collect them.
9. Increase the speed slowly as time passes.
10. Add score, high score, and a restart button/key.

In the Java version I split the game into model, view, controller, and input classes. The HTML version uses the same basic idea, but everything is inside one file so it is easier to open and test quickly.
