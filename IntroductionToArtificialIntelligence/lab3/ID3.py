import math
import copy


class ID3:
    def __init__(self,deep):
        self.deep = deep

    def fit(self,path: str):
        (headers, data) = ID3._readCSV(path)
        self.differentValues = dict()
        for col in headers[:-1]:
            self.differentValues[col] = ID3._groupBy(data,col)
        entropy = ID3._entropy(ID3._groupByAndCount(data,headers[-1]),len(data))
        self.tree =  self._ID3(data,data,headers[:-1], headers[-1],headers[-1],entropy,0)

    def predict(self,path: str):
        if(self.tree == None):
            raise Exception('Nije kreiran model')
        (self.testHeaders, self.testData) = ID3._readCSV(path)
        return list(map(lambda e:self.predictRow(e, None),self.testData))

    def accuracy(self) -> float:
        if(self.testData == None):
            raise Exception('Testni podaci nisu učitani')
        resultsList =  list(map(lambda e:self.predictRow(e,e[self.testHeaders[-1]]),self.testData))
        return sum(resultsList) / len(resultsList)

    def confusionMatrix(self) -> list:
        if(self.testData == None):
            raise Exception('Testni podaci nisu učitani')
        statesDict = ID3._groupByAndCount(self.testData,self.testHeaders[-1])
        states = sorted(statesDict)
        matrix = {}
        for s in states:
            row = {}
            for s1 in states:
                row[s1] = 0
            matrix[s] = row
        for row in self.testData:
            modelState = self.predictRow(row,None)
            testState = row[self.testHeaders[-1]]
            matrix[testState][modelState] += 1
        return matrix

        


    def _ID3(self, data: list, dataParent: list, X: list, state: str, y: str, entropy: float, indexDeep):
        if len(data) == 0:
            return Leaf(self._groupByAndReturnMaxCount(dataParent,y)[0])
        if(indexDeep == self.deep):
            return Leaf(self._groupByAndReturnMaxCount(data,state)[0])
        if len(X) == 0 or entropy == 0.0:
            return Leaf(self._groupByAndReturnMaxCount(data,state)[0])
        (maxCol,maxdifValue) = self._maxIG(X,data,entropy,state)
        children = dict()
        for child,childEntropy in maxdifValue.items():
            children[child] = self._ID3(
                list(filter(lambda x: x[maxCol] == child ,data)),data,
                list(filter(lambda x : x != maxCol,X)),
                state,
                maxCol,
                childEntropy,
                indexDeep + 1
            )
        default = default = self._groupByAndReturnMaxCount(data,state)[0]
        # if len(maxdifValue) != len(self.differentValues[maxCol]):
        #     default = self._groupByAndReturnMaxCount(data,state)[0]
        return Node(maxCol, default, children)


    def _maxIG(self, columns: list, data:list, entropy: float, state: str):
        maxCol = None
        maxIG = None
        maxdifValue = None

        for col in columns:
            (tmpIG,tmpdifValue) = self._IG(data,col,entropy,state)
            print('IG('+ col + ')=' + str(tmpIG) +" ",end='')
            if(maxCol == None or (tmpIG == maxIG and col < maxCol)):
                maxCol, maxIG, maxdifValue = col, tmpIG, tmpdifValue
                continue
            if(maxCol == None or tmpIG > maxIG):
                maxCol, maxIG, maxdifValue = col, tmpIG, tmpdifValue
        print()
        return (maxCol,maxdifValue)
            

    def _IG(self, data: list,column: str ,entropy: float, state: str):
        difValue = ID3._groupByAndGroupByAndCount(data,column,state)
        igValue = copy.deepcopy(entropy)
        for key,item in difValue.items():
            suma = sum(item.values())
            entropyItem = ID3._entropy(item,suma)
            igValue -= suma / len(data) * entropyItem
            difValue[key] = entropyItem
        return (igValue, difValue)
    
    @staticmethod   
    def _entropy(data: dict, size: int) -> float :
        if(size == None): size = sum(data.values())
        suma = 0.0 
        for el in data.values():
            suma = suma - el/size * math.log2(el/size)
        return suma

    @staticmethod
    def _onlyOneUniqueValueinColumn(data,column) -> bool:
        difValue = set()
        for row in data:
            col = row[column]
            if col not in difValue:
                difValue.add(col)
            if len(difValue) > 1: return False
        return True
    
    @staticmethod
    def _groupByAndReturnMaxCount(data,groupByKey) -> tuple:        
        groupbyValues = ID3._groupByAndCount(data,groupByKey)
        return ID3._maxTupleSecondFirst(groupbyValues)

    @staticmethod
    def _maxTupleSecondFirst(data) -> tuple:
        max = None
        for value in data.items():
            if(max == None or ( value[1] == max[1] and value[0] < max[0])):
                max = value
                continue
            if(max == None or value[1] > max[1]):
                max = value
        return max

    @staticmethod
    def _groupByAndGroupByAndCount(data,groupByKey, countKey ) -> dict:
        def doOnSame(row,el):
            if row[countKey] in el:
                el[row[countKey]] = el[row[countKey]] + 1
            else:
                el[row[countKey]] = 1
            return el
        
        def onNew(row):
            return {row[countKey] : 1}

        return  ID3._groupbyAndDo(data,groupByKey,doOnSame,onNew)


    @staticmethod
    def _groupBy(data,groupByKey) -> dict:
        def doOnSame(row,el):
            return el
        def doOnNew(row):
            return None
        differnetValues = ID3._groupbyAndDo(data,groupByKey,doOnSame,doOnNew)
        return set(differnetValues.keys())

    @staticmethod
    def _groupByAndCount(data,groupByKey) -> dict:
        def count(row,el):
            return el + 1
        def newValue(row):
            return 1
        return ID3._groupbyAndDo(data,groupByKey,count,newValue)


    @staticmethod
    def _groupbyAndDo(data,groupByKey, doOnSame, doOnNew) -> dict:
        differentValues = {}
        for row in data:
            col = row[groupByKey]
            if col in differentValues:
                differentValues[col] = doOnSame(row,differentValues[col])
            else:
                differentValues[col] = doOnNew(row)
        return differentValues
    
    @staticmethod
    def _readCSV(path) -> tuple:
        with open(path, encoding="utf-8") as f:
            content = f.readlines()
        lista = []
        for i,line in enumerate(content):
            if i == 0:
                headers = line[:-1].split(',')
                continue
            mapa = {}
            for j,el in enumerate(line[:-1].split(',')):
                mapa[str(headers[j])] = el
            lista.append(mapa)
        return (headers,lista)
    
    def printTree(self):
        print('[BRANCHES]:')
        self._printTreeInit(self.tree,[],1)



    def _printTreeInit(self, tree, listPrint: list, index: int):
        if isinstance(tree,Leaf):
            print(''.join(listPrint) +  tree.name)
            return
        
        detaultValues = copy.deepcopy(self.differentValues[tree.name])
        for childName,child in tree.children.items():
            listPrint.append(str(index) + ':' + tree.name + '='+ childName +' ')
            ID3._printTreeInit(self, child,listPrint, index +1)
            listPrint.pop()
            detaultValues.remove(childName)
        
        for value in detaultValues:
            listPrint.append(str(index) + ':' + tree.name + '='+ value +' ')
            ID3._printTreeInit(self, Leaf(tree.default),listPrint, index +1)
            listPrint.pop()

    
    def predictRow(self,row: dict, state: str):
        return ID3._predictRowInit(self.tree,row,state)

    @staticmethod
    def _predictRowInit(tree, row: dict, state:str):
        if isinstance(tree,Leaf):
            if(state != None):
                return tree.name == state
            return tree.name

        property = tree.name
        rowValue = row[property]
        if rowValue in tree.children:
            return ID3._predictRowInit(tree.children[rowValue],row, state)
        else:
            if(state != None):
                return tree.default == state
            return tree.default

    @staticmethod
    def _groupByAndCountTree(tree, values = dict()) -> dict:
        if isinstance(tree,Leaf):
            state = tree.name
            if state in values:
                values[state] += 1
            else:
                values[state] = 1
            return values
        
        for child in tree.children.values():
            values = ID3._groupByAndCountTree(child,values)
        return values
            

class Node:
    def __init__(self,name: str, default: str, children: list):
        self.name = name
        self.default = default
        self.children = children

class Leaf:
    def __init__(self,name: str):
        self.name = name
