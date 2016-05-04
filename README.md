This is a java/JOGL port of the code offered by Daniel Beard on his old post:

https://danielbeard.wordpress.com/2009/06/20/doom-md5-model-loader/

It's not an exact replica, but it's really close.  In particular, it is not as memory efficient due to how the FloatBuffers work and all the flipping that is required (or that I think is required) for JOGL.  Please feel free to use this code in your projects license free.  All I really ask is credit for the port and credit for Mr. Beard for originally making this work public.
