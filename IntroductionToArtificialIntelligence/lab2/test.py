import sys
from Utils import *
import os

# from os import walk

# _, _, filenames = next(walk('lab2_files/resolution_examples'))

# for file in filenames:
#     el = 'lab2_files/resolution_examples/' + file 
#     sk = read_input(el)
#     first_parents = resolution(sk)
#     ispisKrozRoditelje(sk,first_parents)
#     print()
#     print()


sk = read_input('lab2_files/resolution_examples/coffee_or_tea.txt')
first_parents = resolution(sk)
ispisKrozRoditelje(sk,first_parents)