## Status

I was running rn5.java on a c4.large AWS EC2 instance. Then it died. :(  It was looking for solutions to n=13 and it got to attempt #7,078,106,103,817 after 92,356.03 minutes without finding any. That's 1,277,323 configurations per second (!) By the way, as of this writing, a c4.large instance gets you a dual core CPU running at about 3 GHz. Anyhow, after running it for about two months without finding a solution, I think I'm done. 

I also was running pn5.java on my MacBook Air (1.3 GHz four core CPU) and it crashed after running for over a month looking for solutions to n=14. It got to attempt #5,497,558,138,889 after 52,465.41 minutes. That's 1,746,407 configurations per second which I think is none too shabby for a laptop. 

I also was running tr5.java on the same laptop for n=25. That would have taken months, maybe years, to find a solution. It had only reached attempt #2,405,181,685,769 after 52,584.90 minutes.

So that's it for now.
