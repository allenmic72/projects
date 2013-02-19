ImageSearch (Working Title)
===========

Semester Project

Curently a prototype. Can take two images as input, attempts to match the first (the pattern) against the second
Handles a cropped pattern image and the following formats: PNG, JPEG, GIF
Final version will take any number of patterns and other images for matching, and will handle scaled images

Authors:
-------------
Jonathan Lane<br />
Jlane09sjp@gmail.com<br />
Mike Allen<br />
mwa020591@gmail.com<br />
Julio Maniratunga<br />
maniratunga.j@husky.neu.edu<br />
Taylor Johndrew<br />
taylorstheking@yahoo.com<br />

Example Usage
-------------

<code>python ImageSearch.py -p TestImages/morning.png -s TestImages/morning.png </code>

Should return MATCHES in the console output

To Make Spims File
-------------

Make sure the parser location declaration (located at the top of the ImageSearch.py file) is correct. The one already there should work on CCIS linux boxes. Then, make sure the file has execution permissions and rename the file to "spims". Run the program like the following example

<code>./spims -p TestImages/morning.png -s TestImages/morning.png </code>
