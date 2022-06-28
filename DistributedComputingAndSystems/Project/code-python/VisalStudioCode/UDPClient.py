from ast import Str
import socket
import threading, queue
#from codes import CODES

localhost = '127.0.0.1'
addresses = [20000,30000,40000,50000]


class UDPClient():
    

    def __init__(self, debug = False):
        self.__queue = queue.Queue()
        self.address = self.__getAddress(addresses)
        self.debug = debug
        if self.debug:
            print("Get Address ", self.address)

        worker = threading.Thread(target=self.__work, daemon=True)
        worker.start()
        listner = threading.Thread(target=self.__listen, daemon=True)
        listner.start()
        self.s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)


    def readMessage(self,message):
        if self.debug:
            print(message)

    def __work(self):
        while True:
            m = self.__queue.get()
            self.readMessage(m)

    def __listen(self):
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            location = (localhost,self.address)
            s.bind(location)
            while True:
                message = s.recv(1024).decode()
                self.queue.put(message)

    def __getAddress(self,addresses):
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            for a in addresses:
                try:
                    location = (localhost,a)
                    s.bind(location)
                    return a
                except OSError:
                    pass
            raise Exception("Can not get adress")

    def broadcast(self,code,addresses,message= ""):
        message = self.__createMessage(code,message)
        with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
            for adr in addresses:
                if adr == self.address:
                    continue
                location = (localhost,adr)
                self.s.sendto(message.encode(),location)

    def __createMessage(self,code, message):
        return str(code) + "::" + str(self.address) + "::" + message

    def getAddress(self):
        return self.address

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        self.s.close()
