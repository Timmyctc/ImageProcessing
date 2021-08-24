# ImageProcessing
Basic Java Kernel Convolution

1. Process Image from System 
(Allows user to process an image located on their system with the currently selected program settings, 
allows for adding and combining multiple filters
The required user Inputs are the file name(extension isn't required but is acceptable as the program will truncate as necessary
and the directory of the image i.e. "C:\Users\username\Desktop" trailing whitespace should be trimmed and the delimiter should adjust according to the OS used)

2. Display available filters 
(Displays list of all Kernels to the user, useful for when a user wants to change the current filter in use)

3. Select a filter 
(Allows user to input the name of a filter, non case-sensitive, and change the current filter)

4. Change Multiplication Factor and Bias 
(Multi Factor multiplies the values at each coord in the kernel, multiplying the effects of the kernel, can result in a washed out/ darkened image so should be offset with bias(a flat value added/subtracted from the individual color channels)

5. Displays Current Settings
(Displays to user current Filter, Multifactor and Bias settings)

6. Process Image from URL 
(Prompts user for URL link and attempts to process an image at the given address)

7. Quit (Terminates program) 
