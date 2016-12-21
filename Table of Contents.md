This Repository contains:

1. This TOC file

2. A README file with a problem description, some helpful links, and a current status of the project.

3. Python and Java source code for figuring out how to tile a square that is Nx(N+1)/2 units on each side with 1 tile that is 1x1, 2 tiles that are 2x2, ... and N tiles that are NxN. There are versions for Python v2.7.5, Python v3.5.2, and Java 8.

4. A (large) text file called "8solutions.txt.gz" containing all 18,656 possible solutions for an N=8 square. 

5. Some small files, called "9solutions.txt", "10solutions.txt", and "11solutions.txt", that contain the first solution found by the program for N=9, N=10, and N=11. (I suppose I should not have named them "solutions" in the plural since they each contain only a single solution.)

6. A text file called "tile-dist.txt" that shows the average tile value for each position in the board. Not that interesting: the minimum value was 6.1 and the maximum value was 7.3.

7. A text file called "1freq-zero.txt" that shows the total number of times the 1x1 tile was located at each position of the board in all 18,656 solutions. The range was surprisingly large: a low of 1 and a high of 416. There's another file called "1freq-white.txt" that's the same only it doesn't use any zeroes so it's easier to read for humans (and slightly more difficult to parse for a computer program).

8. A text file called "transitions.txt" that is an only partially successful attempt at ranking all the solutions by how fragmented they are. For each solution, it scans each row and each column, one at a time. Each time it notices the tile size value change, it increments the transition counter. The file contains a list of all the solutions and their transition counts. They range from 243 to 341.

9. Python code ("postproc.py") for generating the data in items 6 through 8. It's pretty shabby code written with the assumption that it only needs to run once. (Sorry.)
