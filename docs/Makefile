OUT="The_SatMod-Lambda_Compendium_0.0.1-SNAPSHOT"
DIR=build
FLAGS=-s -S --toc -f markdown
SRC=$(sort $(wildcard *.md))
CC=pandoc

all: pdf

pdf:
	mkdir -p $(DIR)
	cat $(SRC) | $(CC) $(FLAGS) -o $(DIR)/$(OUT).pdf

debian-deps:
	sudo apt-get install pandoc texlive

fedora-deps:
	sudo yum -y install pandoc texlive

clean:
	rm -rf $(DIR)
