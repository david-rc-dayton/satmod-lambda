# Overview

*SatMod-$\lambda$* is a satellite coverage analysis tool, used to generate 
data for constellation management and space situational awareness. The code is
open sourced under the [MIT License][0001], and currently hosted on
[GitHub][0002]. The software is written in [Clojure][0003], using
[Nightcode][0004] as the preferred Integrated Development Environment. *The 
SatMod-$\lambda$ Compendium* is written in [Markdown][0008], and converted to 
PDF using [Pandoc][9] and \LaTeX\ implemented through [TeX Live][0010]. A
*Makefile* has been created to largely automate the conversion process, and
install the document generation dependencies on [Debian][0012] or [Fedora][0013]
operating systems.

The software is free to distribute and modify, and involvement from the
space and open source communities are encouraged. Improvements are managed
through GitHub using the *fork & pull model*; information on how to
accomplish this can be found at [GitHub Help][0005]. One notable exception
is the satellite propagator library [predict4java][0006] included in the
*SatMod-$\lambda$* GitHub repository in binary form, which is licensed under
the [GNU General Public License, version 2][0007]. Code improvements to the
propagator library are subject to the GNU General Public License, and
should be handled through the predict4java repository.

\pagebreak

