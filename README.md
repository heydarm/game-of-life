# Game of Life
Game of Life with a UI made using Swing

In this game you can create your universe with dead and alive cells
and look how much of them will stay alive after many generations.
This universe will work in the following rules:
- Each cell has 8 neighbors (even border cells);
- A cell survives if has 2 or 3 alive neighbors; otherwise, it dies of boredom (less than 2) or overpopulation (more than 3);
- A dead cell is reborn if it has exactly three alive neighbors;
- The universe is periodic, so top side cycled with bottom and left with right.