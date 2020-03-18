#!/usr/bin/python
# csvgen : Generate a CSV file of random words
# This is used as sample input by the multi-thread file reader project.

import sys
import random

#word_file = "/usr/share/dict/words"
# Too many words ending in 's for my tastes, so grep -v "\'s$" /usr/share/dict/words > nonpossessive-words.txt
word_file = "nonpossessive-words.txt"
WORDS = open(word_file).read().splitlines()
#print len(WORDS)

numlines = 100;
numFields = 3;
for i in range(0,numlines):
    for j in range(0,numFields):
        if (j>0):
            sys.stdout.write(',')
        sys.stdout.write(WORDS[random.randint(0,len(WORDS))])

    print
    sys.stdout.flush();
