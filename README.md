# Partridge-Number
Solution, Visualization, and General Exploration of the "Partridge Number" 

Suppose you have a bunch of square tiles: you have N of the tiles that are N units on a side, N-1 of the tiles that are (N-1) x (N-1) in size, and so forth, down to 1 of the 1x1 tiles. Well, it's easy enough to compute the area of all those tiles taken together and it would be:
    Total Area = N^3 + (N-1)^3 + (N-2)^3 + ... + 1^3

And it turns out that that sum equals [ N x (N+1) / 2 ]^2

That's pretty interesting: the sum is expressed as a term (i.e., "N X (N+1)/2") that is *squared* and that raises the possibility that all those tiles could be arranged to fit inside a square that is N x (N+1)/2 units on each side. OK, but a quick look reveals that for small values of N (e.g., 2 or 3) you can't actually make the tiling work. So what is the smallest value of N where it *does* work? That minimum value is called the "Partridge Number" and here are the only links I've been able to find that discuss it:

http://www2.stetson.edu/~efriedma/papers/partridge.pdf

http://www2.stetson.edu/~efriedma/mathmagic/0802.html

http://www.mathpuzzle.com/partridge.html

Although the articles discuss Partridge Numbers for a variety of shapes (e.g., equilateral triangles, trapezoids, rectangles), I'm only concerned with squares (at least for now). The "literature" (all three articles of it) agree that the Partridge Number for square tiles is 8 and that there are 2332 solutions (attributed to Bill Cutler and Patrick Hamlyn). I couldn't find a comprehensive list of all possible solutions so I thought I would write a program that would generate them.

I came up with an exhaustive search algorithm and implemented it in Python. It has a triple nested loop that recurses so that makes it what, an O(N^N) beast? As of this writing, it's run for over a week, tried about 5 billion configurations, and found about 1700 solutions. That's about 5200 configurations per second.

Next steps:

1. Port this to Java or something that runs faster. This is taking forever.

2. Improve the board display routine to make better use of color. It's ugly as sin right now.

3. Eliminate duplicates. By duplicates I mean the three rotations and four reflections that each configuration has.

4. Score the solutions based on various criteria. For example, which solutions have the fewest number of regions composed of tiles of the same size (i.e., the least "fragmentation"), which solutions have the most.

5. Look for interesting patterns. It's obvious that the 1x1 tile can't be in any of the edge positions so what's the closest it can come? Are there locations in the interior that the 1x1 tile can never inhabit? Are there locations that provide the majority of solutions? And so forth.

6. Refactor this README into a Wiki, an Issues list, etc.

