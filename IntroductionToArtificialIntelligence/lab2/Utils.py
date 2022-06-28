import math
import copy

class Klauzula:
    def __init__(self,value,text,parents):
         self.text = text
         self.value = value
         self.neg_value = Klauzula.getNegValue(value)
         self.parents = parents
         self.isparent = False

    @staticmethod
    def getNegValue(value):
        log = math.floor(math.log2(value))
        minus_1 = 2 **((log if log % 2 !=0 else log + 1 ) + 1) - 1
        xor_value = value ^ minus_1
        start = 2
        half_start = 1
        while( xor_value >= start  + half_start):
            if((xor_value % (start * 2)) >= start + half_start):
                xor_value -= start + half_start
            start = start * 4
            half_start = half_start * 4
        return xor_value
  
    def __hash__(self):
        return hash(self.value)
    
    def __eq__(self, other):
        return other and self.value == other.value 
    
    def __lt__(self, other):
        return bin(self.value).count("1") < bin(other.value).count("1")
    
    @staticmethod
    def parseValue(value,dic):
        lit = []
        for i in range(0, len(dic)):
            last2 = value % 4
            if(last2 == 0): pass
            elif(last2 == 1): lit.append(dic.get(i))
            elif(last2 == 2): lit.append('~' + dic.get(i))
            else: raise ValueError
            value = int(value / 4)
        return ' v '.join(lit)
    
    @staticmethod
    def negStrValue(text):
        if(text.startswith('~')): return text[1:]
        return '~' + text


        
class SetKlauzula:

    def __init__(self):
        self.init_klauzule = dict()
        self.klauzule = dict()
        self.literali = {}
        self.vrijednosti_literala = {}
        self.index = 1
        self.lieral_value = 0
        self.ciljna_klauzula = ''
        self.cilj_tautologija = False

    def getLiteralValue(self):
        i = self.lieral_value
        self.lieral_value += 1
        return i

    def getIndex(self):
        i = self.index
        self.index += 1
        return i
    
    def checkRedundancija(self, kl, removeEl = True):
        remove = set()
        for dic in [self.init_klauzule,self.klauzule]:
            for key, el in dic.items():
                if min(kl,el) == kl:
                    if (kl.value ^ el.value) & kl.value == 0 and el.isparent == False:
                        remove.add(key)
                else:
                    if (kl.value ^ el.value) & el.value == 0:
                        return None
            if removeEl:
                [dic.pop(k, None) for k in remove] 
                remove = set()
        return remove



    def addKlauzulaStr(self,text, parents, init):
        literali_str = text.lower().split(' v ')
        value = 0
        posjeceni = []
        for l in literali_str:
            if l in posjeceni: 
                text = text.replace(' v '+l ,'', 1)
                continue
            if Klauzula.negStrValue(l) in posjeceni: 
                if init == False: self.cilj_tautologija = True
                return
            posjeceni.append(l)
            neg = False
            if l.startswith('~'):
                l = l[1:]
                neg = True
            if l not in self.literali:
                tmp_val = self.getLiteralValue()
                self.literali[l] = tmp_val
                self.vrijednosti_literala[tmp_val] = l

        
            literal_num = 4 ** self.literali[l]
            if neg: literal_num = literal_num * 2
            value += literal_num

        kl = Klauzula(value, text, parents)
        if self.checkRedundancija(kl) == None: return

        if init:
            self.init_klauzule[self.getIndex()] = kl 
            return
        self.klauzule[self.getIndex()] = kl

        
def read_input(path_popis_klauzula,lastNormal = False) :
    try:
        with open(path_popis_klauzula, encoding="utf-8") as f:
            content = f.readlines()
    except Exception:
        print("Datoteka ne postoji na disku ili joj nije moguće pristupiti.")
        return exit()
    
    data = SetKlauzula()

    if lastNormal:
        for line in content:
            if(line[0] == '#'):
                continue
            data.addKlauzulaStr(line.lower().replace('\n',''),[], True)
        return data

    for line in content[:-1]:
        if(line[0] == '#'):
            continue
        data.addKlauzulaStr(line.lower().replace('\n',''),[], True)

    data.ciljna_klauzula = content[-1].lower().replace('\n','')
    for lit in content[-1].lower().split(' v '):
        data.addKlauzulaStr(Klauzula.negStrValue(lit).lower().replace('\n',''), [], False)

    return data



def resolution(setKlauzula):
    new_values = []
    blockingSet = set()
    if setKlauzula.cilj_tautologija: return []
    while True:
        for key1,k1 in setKlauzula.klauzule.items():
            if(key1 in blockingSet): continue
            for key2,k2 in setKlauzula.init_klauzule.items():
                if(key2 in blockingSet): continue
                new_value = (k1.value | k2.value) & ((k1.value ^ k2.neg_value) | (k2.value ^ k1.neg_value))
                
                if (k1.value | k2.value) > new_value and bin(k1.value).count("1") + bin(k2.value).count("1") - 2 == bin(new_value).count("1"):
                    k1.isparent = True
                    k2.isparent = True
                    if new_value == 0: 
                        setKlauzula.init_klauzule[key1] = k1
                        return [setKlauzula.getIndex(),[key1, key2]]

                    #new_values.append([new_value,[key1,key2]])
                    new_kl = Klauzula(new_value,Klauzula.parseValue(new_value,setKlauzula.vrijednosti_literala),[key1,key2])
                    tmp_block = setKlauzula.checkRedundancija(new_kl,False)
                    if(tmp_block == None):
                        continue
                    blockingSet.update(tmp_block)
                    new_values.append(new_kl)

            setKlauzula.init_klauzule[key1] = k1

        setKlauzula.klauzule.clear()
        if len(new_values) == 0: return None
        for value in new_values:
            setKlauzula.klauzule[setKlauzula.getIndex()] = value
        new_values = []
        for el in blockingSet:

            setKlauzula.init_klauzule.pop(el,None)
        blockingSet = set()


def ispisKrozRoditelje(sk, first_parents):
    dic = sk.init_klauzule
    cilj = sk.ciljna_klauzula
    ispis_prije = set()
    ispis_poslije = []
    if first_parents == None:
        print('[CONCLUSION]: '+cilj+' is unknown')
        return

    parents = first_parents[1]
    new_elements = []
    ispis_poslije.append(str(first_parents[0]) + '. NIL (' + str(parents[0]) + ', ' + str(parents[1]) + ')')
    while len(parents) != 0:
        for parent in parents:
            if len(dic[parent].parents) == 0:
                ispis_prije.add(str(parent) + '. ' + dic[parent].text)
                continue
            ispis_poslije.append(
                str(parent) + 
                '. ' +
                dic[parent].text +
                ' (' + 
                str(dic[parent].parents[0]) +
                ', ' +
                str(dic[parent].parents[1]) +
                ')')
            new_elements += dic[parent].parents
        parents = new_elements
        new_elements = []
    reverse_ispis = ispis_poslije[::-1]
    ispis_prije = list(ispis_prije)
    ispis_prije.sort(key = lambda x : int(x.split('.')[0]))
    print('\n'.join(ispis_prije))
    print('===============')
    print('\n'.join(reverse_ispis))
    print('===============')
    print('[CONCLUSION]: '+ cilj +' is true')

def ispisPocetnihKlauzula(dic):
    print('Constructed with knowledge:')
    for k,v in sorted(dic.items()):
        print(str(k) + '. ' + v.text)
    print()


def cooking_assistant(file_name_input,file_name_command):
    sk = read_input(file_name_input,True)
    ispisPocetnihKlauzula(sk.init_klauzule)
    try:
        with open(file_name_command, encoding="utf-8") as f:
            content = f.readlines()
    except Exception:
        print("Datoteka ne postoji na disku ili joj nije moguće pristupiti.")
        return exit()
    
    for line in content:
        if(line[0] == '#'):
            continue
        print('User’s command: ' + line)
        command = line[-2:-1]
        izraz = line[:-3].lower()
        if command == '+':
            sk.addKlauzulaStr(izraz,[],True)
            print('added '+ izraz)
            print()
            continue
        if command == '-':
            for k,v in sk.init_klauzule.items():
                if(v.text == izraz):
                    sk.init_klauzule.pop(k)
                    print('removed ' + izraz)
                    print()
                    break
            continue
        if command == '?':
            sk_current = copy.deepcopy(sk)
            for lit in izraz.split(' v '):
                sk_current.addKlauzulaStr(Klauzula.negStrValue(lit).lower().replace('\n',''), [], False)
            sk_current.ciljna_klauzula = izraz
            first_parents = resolution(sk_current)
            ispisKrozRoditelje(sk_current,first_parents)
            print()
            continue
        
        print('Naredba: '+ line + ' nije prepoznata')
        exit()





