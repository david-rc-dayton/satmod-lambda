# SatMod-&#x3bb;

Satellite constellation analysis software. The purpose of this program is to model satellite coverage, and possibly other useful constellation management metrics. Download links can be found on the [releases](https://github.com/david-rc-dayton/satmod-lambda/releases) page.

![satmod_screenshot](https://raw.githubusercontent.com/david-rc-dayton/satmod-lambda/master/screenshots/satmod_screenshot.png)

*This program requires an installed [Java Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html), version 7 or higher.*

*For non-Windows operating systems, the Fortran runtime library must be installed. This can be accomplised in Debian/Ubuntu by running:*

    sudo apt-get install libgfortran3

## Building & Running

To download the required dependencies and build from source using [Leiningen](http://leiningen.org/), run the following command in the project's root directory:

    lein uberjar

To run the stand-alone JAR file, execute:

    java -jar satmod-lambda-XXX-standalone.jar

replacing `XXX` with the version number.

## License

The MIT License (MIT)

Copyright (c) 2014 David RC Dayton

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
