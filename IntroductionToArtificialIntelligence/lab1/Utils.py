import heapq
comm = [
    'alg',
    'ss',
    'h',
    'check-optimistic',
    'check-consistent',
]

args = [
    'bfs',
    'ucs',
    'astar'
]

def checkIfCommandExists(command):
    return command in comm

def checkIfAlgorithmExists(arg):
    return arg in args

def read_input(path_prostor_stanja) :
    try:
        with open(path_prostor_stanja, encoding="utf-8") as f:
            content = f.readlines()
    except Exception:
        print("Datoteka ne postoji na disku ili joj nije moguće pristupiti.")
        return None
    
    data = {}

    for line in content:
        if(line[0] == '#'):
            continue
        
        words_in_line = line.split()
        
        if(data.get(' start') == None):    
            if(len(words_in_line) != 1):
                print("Redak koji definira startno mjesto mora sadržavati 1 argument, a sadrži: ",len(words_in_line))
                return None
            
            data[' start'] = words_in_line[0]
            continue

        if(data.get(' end') == None):                
            data[' end'] = words_in_line
            continue
        
        arg1 = words_in_line[0]
        if(arg1[-1] != ':'):
            print("zapis funkcije prijelaza započinje s [ime_mesta]:, a ucitano je: " + arg1)
            return None

        mjesto = arg1[0:-1]
        prijelazi = {}
        for prijelaz in words_in_line[1:]:
            mjestoUdaljenost = prijelaz.split(',')
            if(len(mjestoUdaljenost) != 2):
                print("Prijelaz mora biti zadatn u obliku [mjesto],[udaljenost], a zadano je ", +prijelaz)
                return None

            try:
                udaljenost = float(mjestoUdaljenost[1])
            except ValueError:
                print("Nije moguće pretvoriti: " +mjestoUdaljenost[1]+ " u broj")
                return None
            
            prijelazi[mjestoUdaljenost[0]] = udaljenost
        
        prijelazi = dict(sorted(prijelazi.items()))
        data[mjesto] = {}
        data[mjesto]['prijelazi'] = prijelazi
    return data

def read_funcs(data,path_heuristicka_func):
    try:
        with open(path_heuristicka_func, encoding="utf-8") as f:
            content = f.readlines()
    except Exception:
        print("Datoteka ne postoji na disku ili joj nije moguće pristupiti.")
        return None
    
    sva_mjesta = [x for x in list(data.keys()) if not x.startswith(' ')]

    for line in content:
        words_in_line = line.split()
        if(len(words_in_line) != 2):
            print("Heuristicke funckije se zadaju s dva argumenta, a primljeno je: " + len(words_in_line))
            return None
        
        arg1 = words_in_line[0]
        if(arg1[-1] != ':'):
            print("Zapis heuristicke funkcije započinje s [ime_mesta]:, a ucitano je: " + arg1)
            return None

        mjesto = arg1[0:-1]

        if(data.get(mjesto) == None):
            print("Nije pronadeno mjesto: " + mjesto)
            return None

        if mjesto not in sva_mjesta:
            print("Vec je unesena heuristicka funckija za ovo mjesto")
            return None

        try:
            func = float(words_in_line[1])
        except ValueError:
            print("Nije moguće pretvoriti: " +words_in_line[1]+ " u broj.")
            return None

        data[mjesto][' func'] = func
        sva_mjesta.remove(mjesto)
    
    if sva_mjesta:
        print("Nije unesena heursticka funckija za ova mjesta: " + sva_mjesta)
        return None

    
    return data

def BFS_search(data):
    posjeceni = set()
    lista = [
        {
            'mjesto': data[' start'], 
            'dubina': 0, 
            'cijena': 0.0,
            'put': [] 
        }
    ]
    cilj = None
    counter = 0
    while len(lista) != 0 :
        trenutni  = lista.pop(0)
        
        if trenutni['mjesto'] in posjeceni:
            continue

        posjeceni.add(trenutni['mjesto'])
        counter += 1

        if trenutni['mjesto'] in data[' end']:
            cilj = trenutni
            cilj['cijena'] = round(cilj['cijena'], 1)
            cilj['states'] = counter
            cilj['put'] = cilj['put'] + [trenutni['mjesto']]
            break


        lista.extend(
            {
                'mjesto': i,
                'dubina': trenutni['dubina'] + 1,
                'cijena': trenutni['cijena'] + data[trenutni['mjesto']]['prijelazi'][i],
                'put': trenutni['put'] + [trenutni['mjesto']]
            }
            for i in data[trenutni['mjesto']]['prijelazi']
        )

    return cilj

class UCS_item:
    def __init__(self,mjesto,dubina,cijena,put):
        self.mjesto = mjesto
        self.dubina = dubina
        self.cijena = cijena
        self.put = put

    def __lt__(self, other):
        if(self.cijena == other.cijena):
            return self.mjesto < other.mjesto
        return self.cijena < other.cijena
    



def UCS_search(data):
    posjeceni = set()
    lista = [
        UCS_item(data[' start'], 0,  0.0, [] )
    ]
    heapq.heapify(lista)
    cilj = None
    counter = 0
    while len(lista) != 0 :
        trenutni  = heapq.heappop(lista)
        
        if trenutni.mjesto in posjeceni:
            continue
        
        posjeceni.add(trenutni.mjesto)
        counter += 1

        if trenutni.mjesto in data[' end']:
            cilj = {'mjesto' : trenutni.mjesto, 'cijena' : trenutni.cijena, 'dubina' : trenutni.dubina, 'put' : trenutni.put} 
            cilj['cijena'] = round(cilj['cijena'], 1)
            cilj['states'] = counter
            cilj['put'] = cilj['put'] + [trenutni.mjesto]
            break

        for i in data[trenutni.mjesto]['prijelazi']:
            if i in posjeceni:
                continue
            heapq.heappush(
                lista, 
                UCS_item(
                    i,
                    trenutni.dubina + 1,
                    trenutni.cijena + data[trenutni.mjesto]['prijelazi'][i],
                    trenutni.put + [trenutni.mjesto]
                )
            )


    return cilj

class ASTAR_item:
    def __init__(self,mjesto,dubina,cijena,func,put):
        self.mjesto = mjesto
        self.dubina = dubina
        self.cijena = cijena
        self.func = func
        self.put = put

    def __lt__(self, other):
        if(self.func == other.func):
            return self.mjesto < other.mjesto
        return self.func < other.func
    

def ASTAR_search(data):
    posjeceni = set()
    lista = [
        ASTAR_item(data[' start'], 0,  0.0, 0, [] )
    ]
    heapq.heapify(lista)
    cilj = None
    counter = 0
    while len(lista) != 0 :
        trenutni  = heapq.heappop(lista)

        if trenutni.mjesto in posjeceni:
            continue
        
        posjeceni.add(trenutni.mjesto)
        counter += 1
        
        if trenutni.mjesto in data[' end']:
            cilj = {'mjesto' : trenutni.mjesto, 'cijena' : trenutni.cijena, 'dubina' : trenutni.dubina, 'put' : trenutni.put} 
            cilj['cijena'] = round(cilj['cijena'], 1)
            cilj['states'] = counter
            cilj['put'] = cilj['put'] + [trenutni.mjesto]
            break

        for i in data[trenutni.mjesto]['prijelazi']:
            if i in posjeceni:
                continue
            heapq.heappush(
                lista, 
                ASTAR_item(
                    i,
                    trenutni.dubina + 1,
                    trenutni.cijena + data[trenutni.mjesto]['prijelazi'][i],
                    trenutni.cijena + data[trenutni.mjesto]['prijelazi'][i] + data[i][' func'],
                    trenutni.put + [trenutni.mjesto]
                )
            )


    return cilj

def heuristic_consistent(data):
    sva_mjesta = [x for x in list(data.keys()) if not x.startswith(' ')]
    consistent = True
    for mjesto in sva_mjesta:
        susjedi = data[mjesto]['prijelazi']
        for susjed in susjedi:
            cijena = susjedi[susjed]
            heuristika_susjeda = data[susjed][' func']
            heuristika = data[mjesto][' func']
            
            if(heuristika > cijena + heuristika_susjeda):
               consistent = False
        
            print('[CONDITION]: ['
                + ('OK' if heuristika <=  cijena + heuristika_susjeda else 'ERR')
                +'] h('+ mjesto + ') <= h('+ susjed +') + c: '+ str(heuristika)+' <= '+ str(heuristika_susjeda) +' + ' + str(cijena))

    print("[CONCLUSION]: " + 
        ("Heuristic is consistent." if consistent == True else "Heuristic is not consistent.")
    )
from concurrent.futures import ThreadPoolExecutor
from concurrent.futures import as_completed
import multiprocessing
import threading    
import copy


def heuristic_optimistic(data):
    posjeceni = set()
    if(len(data[' end']) != 1):
        heuristic_optimistic_slow(data)
        return
    lista = [
        UCS_item(data[' end'][0], 0,  0.0, [] )
    ]
    heapq.heapify(lista)
    sva_mjesta = {x for x in list(data.keys()) if not x.startswith(' ')}
    
    optimistic = True
    while len(sva_mjesta) != len(posjeceni):
        trenutni  = heapq.heappop(lista)
        if trenutni.mjesto in posjeceni:
            continue
        posjeceni.add(trenutni.mjesto)
        mjesto = trenutni.mjesto
        heuristika = data[trenutni.mjesto][' func']
        prava_cijena = trenutni.cijena
        if(heuristika > prava_cijena):
            optimistic = False
        print('[CONDITION]: ['
            + ('OK' if heuristika <= prava_cijena else 'ERR')
            +'] h('+ mjesto + ') <= h*: '+ str(heuristika) +' <= ' + str(round(prava_cijena,1)))
        
        for i in data[trenutni.mjesto]['prijelazi']:
            if i in posjeceni:
                continue
            heapq.heappush(
                lista, 
                UCS_item(
                    i,
                    trenutni.dubina + 1,
                    trenutni.cijena + data[trenutni.mjesto]['prijelazi'][i],
                    trenutni.put + [trenutni.mjesto]
                )
            )
    

    print("[CONCLUSION]: " +
        ("Heuristic is optimistic." if optimistic == True else "Heuristic is not optimistic.")
    )

def heuristic_optimistic_slow(data):
    sva_mjesta = [x for x in list(data.keys()) if not x.startswith(' ')]
    optimistic = True
    for mjesto in sva_mjesta:
        data[' start'] = mjesto
        prava_cijena = UCS_search(data)['cijena']
        heuristika = data[mjesto][' func']
        if(heuristika > prava_cijena):
            optimistic = False
        
        print('[CONDITION]: ['
            + ('OK' if heuristika <= prava_cijena else 'ERR')
            +'] h('+ mjesto + ') <= h*: '+ str(heuristika) +' <= ' + str(round(prava_cijena,1)))

    print("[CONCLUSION]: " +
        ("Heuristic is optimistic." if optimistic == True else "Heuristic is not optimistic.")
    )

def searchAlghoritm(method,data):
    if(method == 'bfs'):
        return BFS_search(data)
    if(method == 'ucs'):
        return UCS_search(data)
    if(method == 'astar'):
        return ASTAR_search(data)
    raise IOError
    
    