import sys
from Utils import *


args = sys.argv[1:]
argByMinusMInus = (' '.join(args)).lower().split('--')[1:]
commands = {}
for arg in argByMinusMInus:
    commandValue = list(filter(None, arg.split(' ')))
    
    if(len(commandValue) > 2):
        print("Nije moguće razrješiti argument: " + arg)
        exit()
    
    if(checkIfCommandExists(commandValue[0]) == False):
        print("Naredba: " + commandValue[0] + " nije pronađena.")
        exit()

    if(commands.get(commandValue[0] != None)):
        print("Argument: " + commandValue[0] + "se pojavljuje više puta, uzimam u obir prvo pojavljivanje")
        continue
    
    try:
        commands[commandValue[0]] = commandValue[1]
    except IndexError:
        commands[commandValue[0]] = 'None'

if(commands.get('ss') == None):
        print("Argument --ss mora biti naveden.")
        exit()

pathSS = commands['ss']
data = read_input(pathSS)
if(data == None):
    exit()
if(commands.get('h') != None):
    data = read_funcs(data, commands.get('h'))

if(commands.get('alg') != None):
    if(checkIfAlgorithmExists(commands['alg']) == False):
        print("Algoritam: " + commands['alg'] + "nije pronađen.")
        exit()

    if(commands['alg'] == 'astar' and commands.get('h') == None):
        print("Za algoritam A-STAR potrebno je navesti i putanju do datoteke s heurističkim funkcijama.")
        exit()


    
    podaci = searchAlghoritm(commands['alg'],data)
    print("# " + commands['alg'].upper() + (" " +commands['h'] if commands['alg'] == 'astar' else ""))
    if(podaci == None):
        print('[FOUND_SOLUTION]: no')
        exit()
    print('[FOUND_SOLUTION]: yes')
    print('[STATES_VISITED]: '+ str(podaci['states']))
    print('[PATH_LENGTH]: ' + str(len(podaci['put'])))
    print('[TOTAL_COST]: ' + str(podaci['cijena']))
    print('[PATH]: ' + ' => '.join(podaci['put']))
    exit()
    
if(commands.get('check-optimistic') != None or commands.get('check-consistent') != None ):
    if(commands.get('h') == None):
        print("Pri provjeri heurističkih funckija mora se navesti i putanja do same funkcije pomoću argumenta --h")
        exit()

    if(commands.get('check-optimistic') != None):
        print("# HEURISTIC-OPTIMISTIC " + commands['h'])
        heuristic_optimistic(data)

    if(commands.get('check-consistent') != None):
        print("# HEURISTIC-CONSISTENT " + commands['h'])
        heuristic_consistent(data)
    
    