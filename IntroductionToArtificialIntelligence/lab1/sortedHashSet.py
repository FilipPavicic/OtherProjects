class Element:
    def __init__(self,data):
        self.data = data
        self.perent =None
        self.left = None
        self.right = None
    
    def __hash__(self):
        return hash(self.data)

    def __eq__(self, other):
        if isinstance(other, self.__class__):
            return self.data == other.data
        else:
            return False

    def __ne__(self, other):
        return not self.__eq__(other)

    def __lt__(self, other):
        return self.data < other.data
    


class sortedHashSet:
    

    first = None
    lastLeft = None
    lastRight = None
    data = {}

    def pop(self):
        if(self.first == None): 
            return None
        ret = self.first
        self.swapFirstLast()
        if(self.first == None):
            self.last = None
            del self.data[hash(ret)]
            return ret.data
        self.removeLast()
        self.promoteDown()

        del self.data[hash(ret)]
        return ret.data

    def add(self,element):
        el = Element(element)
        if hash(el) in self.data:
            self.data[hash(el)].data = min(self.data[hash(el)].data,element)
            return
        if(self.first == None):
            self.first = self.last = el
            self.data[hash(el)] = el
            return
        self.addToEnd(el)
        self.data[hash(el)] = el
        self.promoteUp()
        

    def contain(self,el):
        return hash(el) in self.data

    def __len__(self):
        return len(self.data)

    def addToEnd(self,el):
        if(self.last.left == None):
            self.last.left = el
            el.perent = self.last
            return
        if(self.last.right == None):
            self.last.right = el
            el.perent = self.last
            return
        self.last = self.last.left
        self.addToEnd(el)
        return
    
    def promoteUp(self):
        el = (self.last.left,self.last.right)[self.last.right != None]
        while(el.perent != None):
            if(el.data > el.perent.data):
                break
            el.data, el.perent.data = el.perent.data, el.data
            self.data[hash(el)], self.data[hash(el.perent)] =  self.data[hash(el.perent)], self.data[hash(el)]
            el = el.perent
    def swapFirstLast(self):
        el = (self.last.left,self.last.right)[self.last.right != None]
        el.data, self.first.data = self.first.data, el.data
    
    def removeLast(self):
        if(self.lastLeft.right != None):
            self.lastLeft.right = None
            return
        if(self.lastLeft.left != None):
            self.lastLeft.left = None
            return
         if(self.lastRight.right != None):
            self.lastRight.right = None
            return
        if(self.lastRight.left != None):
            self.lastRight.left = None
            return
        self.last =self.last.perent
        self.removeLast()
    
    def promoteDown(self):
        i = self.first
        while(i.left != None):
            if(i.right != None):
                smaller = min(i.left, i.right)
            else:
                smaller = i.left
            if(i < smaller):
                break
            i.data, smaller.data = smaller.data, i.data
            self.data[hash(i)], self.data[hash(smaller)] =  self.data[hash(smaller)], self.data[hash(i)]
            i = smaller



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

    def __hash__(self):
        return hash(self.mjesto)
    
set = sortedHashSet()
set.add(5)
set.add(7)
set.add(1)
set.add(2)
set.add(3)
set.add(8)
set.add(0)
set.add(6)
# print(set.pop())
# set.add(90)
# set.add(-8)
print()


# print(set.contain(90))
