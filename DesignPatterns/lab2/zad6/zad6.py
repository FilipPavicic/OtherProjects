from dataclasses import dataclass, field
import re
import ast, operator
@dataclass
class Cell:
    exp: str = field(default= "")
    value : int = field(default= None, init=False)
    listeners : list = field(default_factory=list, init=False)

    def __str__(self,expView):
        if expView:
            return self.exp
        return "" if self.exp == ""  else self.getValuePrint() 
    
    def getValuePrint(self):
        return "Not Ready" if self.value == None else str(self.value)

@dataclass
class Sheet:
    def default_factory_list(self):
        lista = []
        for i in range(0,self.max_row):
            lista1 = []
            for j in range(0,self.max_column):
                lista1.append(Cell())
            lista.append(lista1)
        return lista
    
    max_row: int
    max_column: int
    sirina: int = field(default = 11) 
    lista: list = field(default_factory=list, init= False)

    def __post_init__(self):
        if(self.max_column > 30): raise AttributeError("Moguće je unjeti maksimalno 30 stupaca")
        self.lista = self.default_factory_list()


    def p(self,text):
        return text.center(self.sirina, " ") + "|"

    def print(self,expView = False):
        strLine = self.p('-' * self.sirina)
        for i in range(-1,self.max_row):
            for j in range(-1,self.max_row):
                if(j == -1):
                    if(i == -1):
                        print(self.p(' '), end = "")
                    else:   
                        print(self.p(str(i+1)), end = "")
                    continue
                if(i == -1):
                    print(self.p(chr(j + 65)), end = "")
                    strLine += self.p('-' * self.sirina)
                    continue
                print(self.p(str(self.lista[i][j].__str__(expView))), end ="")
            print()
            print(strLine)
    
    def cell(self, ref):
        regex = re.compile("([a-zA-Z]+)([0-9]+)")
        r = regex.match(ref)
        coll = r.group(1)
        if(len(coll) != 1): raise IndexError("Ne postoji stupac: " + coll)
        br_coll = ord(coll) - 65
        br_row = int(r.group(2)) -1
        return self.lista[br_row][br_coll]
    
    def set(self, ref, content):
        ref = ref.upper()
        content = content.upper()
        ovisio_sam_o = self.getrefs(ref)
        for o in ovisio_sam_o:
            self.cell(o).listeners.remove(ref)
        try:
            self.cell(ref).exp = content
        except IndexError as e:
            print("Error: " +e)
            return
        ovisim_o = self.getrefs(ref)
        for o in ovisim_o:
            self.cell(o).listeners.append(ref)
        self.evaluate(ref,ovisim_o)

        

    
    def getrefs(self,cell):
        exp = self.cell(cell).exp
        return re.findall('[a-zA-Z][0-9]+',exp)

    binOps = {
        ast.Add: operator.add,
        ast.Sub: operator.sub,
        ast.Mult: operator.mul,
        ast.Div: operator.floordiv,
        ast.Mod: operator.mod
    }
    
    def evaluate(self,cell,ovisim_o):
        node = ast.parse(self.cell(cell).exp, mode='eval')
        def _eval(node):
            if isinstance(node, ast.Expression):
                return _eval(node.body)
            elif isinstance(node, ast.Str):
                return node.s
            elif isinstance(node, ast.Name):
                return self.cell(node.id).value
            elif isinstance(node, ast.Num):
                return node.n
            elif isinstance(node, ast.BinOp):
                try:
                    return self.binOps[type(node.op)](_eval(node.left), _eval(node.right))
                except TypeError:
                    return None
                
            else:
                raise Exception('Unsupported type {}'.format(node))
    
        prije_vriendost = self.cell(cell).value
        self.cell(cell).value =  _eval(node.body)
        if(prije_vriendost != self.cell(cell).value):
            for el in self.cell(cell).listeners:
                if el in ovisim_o: raise RuntimeError(cell + " ovisi sama o sebi što nije dozvoljeno")
                self.evaluate(el,ovisim_o)
        

s=Sheet(5,5)
print()

s.set('A1','2')
s.set('A2','5')
s.set('A3','A1+A2')
s.print()
print()

s.set('A1','4')
s.set('A4','A2+A3')
s.print()
print()

try:
    s.set('A1','A4')
except RuntimeError as e:
    print("Caught exception:",e)
s.print()
print()

