# Overview:

All of the solution files depict solutions by displaying a number at each location on the board that corresponds to the size of the tile covering that location. That's all very well for a computer but impossible to visualize for a human. I wrote a program ("colorize.py") that creates a color .PNG image file of any particular solution. 

I also wrote a a colorizing porgam ("colorone.py") that a) looks at a file called "tocolor.txt" that contains a single solution (and not a list of solutions like, say, "8solutions.txt"), b) creates a "colorone.png" file from it, and c) outlines each piece in black so you can tell where each piece's edges are.

All the files in the "Images" directory are for the N=8 Partridge Puzzle. Although I created .PNG files for all 18,656 solutions, I am only including two of them, "00240.png" and "01021.png". I picked them because they show the most and least fragmented solutions in glorious color.

For the hell of it, I used ImageJ to turn those 18,656 .PNG files into a 24 frames per second .AVI movie. It is called "8sol24fps.avi" and is the most astoundingly boring thirteen minute video in the history of videos. 

