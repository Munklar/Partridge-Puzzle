# Partridge-Puzzle
_Note: As of this writing (2/17/2017) I'm pretty much done. The code works and I've satisfied my curiosity about the solutions my programs found._ 

This repository is for the source code and related information needed to solve, visualize, and generally explore the "Partridge Puzzle" as described by Robert T. Wainwright in 1996 at the second "Gathering for Gardner". Note: I became interested in this puzzle because my last name is Partridge but its invention is entirely Bob Wainwright's. 

Suppose you have a bunch of square tiles: you have N of the tiles that are N units on each side, N-1 of the tiles that are (N-1) x (N-1) in size, and so forth, down to 1 of the 1x1 tiles. Well, it's easy enough to compute the area of all those tiles taken together and it would be:
    Total Area = N^3 + (N-1)^3 + (N-2)^3 + ... + 1^3

And it turns out that that sum equals [ N x (N+1) / 2 ]^2

That's pretty interesting: the sum is expressed as a term that is *squared* (namely "N X (N+1)/2") and that raises the possibility that all those tiles could be arranged to fit perfectly inside a square that is N x (N+1)/2 units on each side. OK, but a quick look reveals that for small values of N (e.g., 2 or 3) you can't actually make the tiling work. So what is the smallest value of N where it **does** work? That minimum value of N is sometimes called the "Partridge Number" and here are the only links I've been able to find that discuss it:

http://www.mathpuzzle.com/partridge.html

http://www2.stetson.edu/~efriedma/papers/partridge.pdf

http://www2.stetson.edu/~efriedma/mathmagic/0802.html

Although the articles discuss Partridge Numbers for a variety of shapes (e.g., equilateral triangles, trapezoids, rectangles), I'm only concerned with squares (at least for now). The three articles above all refer to work done by Bill Cutler and Patrick Hamlyn who apparently were able to determine that the Partridge Number for square tiles is 8 and that there are 2332 solutions. The articles don't say if there are 2332 *unique* solutions that exclude rotations and reflections and I couldn't find a comprehensive list of all possible solutions ... so I thought I would write a program and generate them myself.

I came up with an exhaustive search algorithm and implemented it in Python. It has a triple nested loop, each O(N^2), that recurses so that makes it what, an O(N^12) beast? I ran the Python version for over a week and it was checking about 5200 configurations per second. That was going much too slowly so I ported it to Java which clocked in at about 1.3 million configurations per second. It checked all 60+ billion configurations in less than a day.

The "8solutions.txt" file contains all 18,656 solutions, including rotations and reflections. Since there are 7 rotations and reflections for every solution, the total number of unique solutions is 2,332 - which agrees with Bill Cutler's and Patrick Hamlyn's result.

I was able to reach Bob Wainwright by email and he generously sent me copies of the paper he had written for Gathering for Gardner II in 1996 and for Gathering for Gardner III in 1998. They are titled, respectively, "Packing a Partridge in a Square Tree" and "Packing a Partridge in the Smallest Square Tree". The two papers pretty thoroughly discuss the Partridge Puzzles and their variants and include many solutions. I found one of the variants - I'll call it the Trapridge Puzzle - to be intriguing enough that I adapted my program to solve it.

With the Trapridge Puzzle, rather than have 1 of the 1x1 tiles, you have N, and rather than having N of the NxN tiles, you have 1. The idea was that since the Twelve Days of Christmas song mentions the one partridge twelve times and the twelve Lords-a-Leaping once, this was a more faithful interpretation of the lyrics. Unfortunately the sum of areas for 12 1x1 tiles, 11 2x2 tiles, ... and 1 12x12 tile is not the square of an integer. So the Trapridge puzzle has no solution for N=12. However, the sum of areas **is** a square of an integer for N=6, 25, 96, 361, etc. It was relatively easy to change my program to handle this variation and I have included it and the solutions it found.  

By the way, Martin Garnder refers to Bob Wainwright's work on this puzzle in one of his books. Look up "Wainwright" in the index of "Fractal Music, Hypercards and More..." by Martin Gardner, published in 1992 by W.H. Freeman. 


