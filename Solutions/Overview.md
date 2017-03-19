# Overview:

The Python and Java programs generated 18,656 solutions for the N=8 version of the Partridge Puzzle. They can be found in "8solutions.txt". For the fun of it, I ran pn5.java to find the first solutions for N=9, N=10, N=11, N=12, N=13, and N=14 (no solutions yet for N=14). 

I made rn5.java (a modified version of pn5.java) to search for the "last" solution. Here's what that means. As originally written, pn5.java puts down tiles in order of ascending size so, for example, the last solution for the N=8 puzzle would be one where rn5.java is placing the first 8x8 tile in the bottom left hand corner. I ran this "reversed" version of pn5.java to find the last solutions for N=9, N=10, N=11, N=12, and N=13 (no solution yet for N=13).

As mentioned in the README file, there is a variant of the Partridge Puzzle worth mentioning and I call it the "Trapridge Puzzle". Instead of having a single 1x1 tile and a total of N of the NxN tiles, the Trapridge Puzzle has N of the 1x1 tiles, N-1 of the 2x2 tiles, ... , and a single NxN tile. When the tiles sizes are chosen this way, their total area generally does not equal a perfect square (i.e., the square of an integer). Here are the few values of N for when they do:

      N       Square Size
      -       -----------
      1           1
      6           14
      25          195
      96          2716
      361         37829
      1350        526890
      5041        7338631

I modified pn5.java to solve this problem and called it tr5.java. It quickly (in less than a minute) found the 60,568 solutions for N=6 and they are in the file called "6back.txt". Finding even the first solution for N=25 is taking quite a bit longer (no solutions after 25 days).
