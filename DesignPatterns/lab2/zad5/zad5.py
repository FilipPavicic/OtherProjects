import time
import os
from dataclasses import dataclass, field
import datetime
import statistics

class Izvor:
    def getBroj(self):
        strBroj = self.getLine()
        try:
            broj = int(strBroj)
        except ValueError:
            broj = -1
        return broj if broj >= 0 else -1
    def getLine(self):
        raise NotImplementedError

class TipkovnickiIzvor(Izvor):
    def getLine(self):
        return  input("Unesite novi broj: ")

class DatotecniIzvor(Izvor): 
    def __init__(self,strPath):
        self.index = -1
        with open(strPath) as fp:
            self.lines = fp.readlines()
    def getLine(self):
        self.index +=1 
        return self.lines[self.index]

class Subject:
    list = []
    def addListener(self,observer):
        self.list.append(observer)
    def removeListener(self,observer):
        self.list.remove(observer)
    def notify(self,collection):
        for el in self.list:
            el.update(collection)
class IObserver:
    def update(self,collection):
        raise NotImplementedError
@dataclass
class printNumbersInFile(IObserver):
    path: str
    def update(self,collection):
        now = datetime.datetime.now()
        with open(self.path,"a") as fp:
            fp.write("[" + now.strftime("%d.%m.%Y %H:%M:%S") + "] -> " + str(collection[-1]) +"\n")

class IspisSume(IObserver):
    def update(self,collection):
        print("Ispisujem sumu brojeva: " + str(sum(collection)))
class IspisProsjeka(IObserver):
    def update(self,collection):
        print("Ispisujem prosjek brojeva: " + str(sum(collection)/len(collection)))
class IspisMedijan(IObserver):
    def update(self,collection):
        print("Ispisujem sumu brojeva: " + str(statistics.median(collection)))

@dataclass
class SlijedBrojeva(Subject):
    izvor: Izvor
    lista: list = field(default_factory = list, init= False)

    def kreni(self):
        while True:
            br = self.izvor.getBroj()
            print("Dobio sam broj: " + str(br))
            if(br == -1):
                break
            self.lista.append(br)
            self.notify(self.lista)
            time.sleep(1)
        print(self.lista)


#DatotecniIzvor(os.path.dirname(os.path.realpath(__file__) + "/brojevi.txt")
#TipkovnickiIzvor()
sb = SlijedBrojeva(DatotecniIzvor(os.path.dirname(os.path.realpath(__file__)) + "/brojevi.txt"))

#printNumbersInFile(os.path.dirname(os.path.realpath(__file__)) + "/brojevi_izlaz.txt")
sb.addListener(printNumbersInFile(os.path.dirname(os.path.realpath(__file__)) + "/brojevi_izlaz.txt"))
sb.addListener(IspisSume())
sb.addListener(IspisProsjeka())
sb.addListener(IspisMedijan())
sb.kreni()
                