import sys
from Utils import *


args = sys.argv[1:]
commands = {}
if args[0] == 'resolution':
    if(len(args) != 2):
        print('Moraju biti točno 2 argumenta')
        exit()
    sk = read_input(args[1])
    first_parents = resolution(sk)
    ispisKrozRoditelje(sk,first_parents)
    exit()
if args[0] == 'cooking':
    if(len(args) != 3):
        print('Moraju biti točno 3 argumenta')
        exit()
    cooking_assistant(args[1], args[2])
    exit()

print('Naredba: '+args[0]+' nije pronađena.')
   