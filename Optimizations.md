# Optimizations:

To get pn5.java to find solutions for larger and larger N, I added some optimizations. To measure how much benefit they provided, I ran pn5 on N=8 to see how many times the optimizations were hit during the course of finding the 18,656 solutions. Here is the output:

>
>Solution for N = 8
>
>Tried 24,079,146,150 configurations in 137.25 minutes
>
>Optimization 1: 2,251,486,709  
>Optimization 2: 6,229,416,002  
>Optimization 3: 96,513,530  
>Optimization 4: 1,499,814,511  
>Optimization 5: 2,194,534,969  
>

I didn't do any precise profiling but the optimizations seemed to cut the running time by 70% - 80%.
