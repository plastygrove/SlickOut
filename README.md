SlickOut
========

Code for the excellent tutorial on game dev using Slick2D (which uses LWJGL) done by Tiago “Spiegel” Costa. I've added more features and made some changes to the game and am putting it here so that anyone can download and have a ready eclipse project to use.


The full tutorial is available at:
http://slick.cokeandcode.com/wiki/doku.php?id=03_-_slickout


To try this out, you need to have eclipse with a valid workspace. Do a git clone using 

git clone https://github.com/plastygrove/SlickOut.git

in your workspace. Then import it using File->Import->General->Existing Projects into Workspace

Additional Features over tutorial
=================================

1. Ball direction can be controlled depending on where it lands on the paddle. Middle = perfect reflective bounce. Edges alters the intended ball direction by 30 degrees with varying values between center and either edges
2. Bricks change colour depending on how many hits they've got left. This only happens on collision. Initial colour still remains
3. Added Sounds

