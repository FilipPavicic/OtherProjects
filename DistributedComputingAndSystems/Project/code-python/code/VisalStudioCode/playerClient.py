from UDPClient import UDPClient
from gameState import GameState
from codes import CODES
from gameStatus import GAME_STATUS
from tools import log

class PlayerClient(UDPClient):
    def __init__(self, me, debug=False):
        super().__init__(debug)
        self.game = GameState()
        self.online_addresses = []
        self.me = me
        self.debug = debug
        self.game = GameState()
    
    def readMessage(self, message):
        code, sender, message = message.split("::")
        code = CODES.encode(code)
        sender = int(sender)


        if code == CODES.HELLO:
            self.online_addresses.append(sender)
            self.broadcast(CODES.HELLO_RPL, [sender])

            if self.game.game_state == GAME_STATUS.INVITE and self.me == self.game.leader:
                self.broadcast(CODES.INVITE, [sender], self.me)

            log(self.debug,code, "Change in online addresses", self.online_addresses)


        elif code == CODES.HELLO_RPL:
            self.online_addresses.append(sender)

            log(self.debug,code, "Change in online addresses", self.online_addresses)


        elif code == CODES.EXIT:
            self.online_addresses.remove(sender)
            log(self.debug,code, "Change in online addresses", self.online_addresses)


        elif code == CODES.INVITE:
            self.game.game_state = GAME_STATUS.INVITE
            self.game.leader = message
            log(self.debug,code, "Recieve invite from "+ message)

        
        elif code == CODES.ACC_INVITE:
            if self.me == self.game.leader:
                self.game.players.append(message)
                log(self.debug,code, "Players: "+ str(self.game.players))


        elif code == CODES.START:
            self.game.game_state = GAME_STATUS.YES
            self.game.players = message.split(" ")
            log(self.debug,code, "Players: "+ str(self.game.players))

            
        elif code == CODES.TEXT:
            log(self.debug,code, message)
        
        else:
            log(self.debug, message)

    
    def doCommand(self,code: CODES):
        
        if code == CODES.EXIT:
            self.broadcast(CODES.EXIT, self.online_addresses)

        elif code == CODES.INVITE:
            if self.game.game_state == GAME_STATUS.NO:
                self.game.game_state = GAME_STATUS.INVITE
                self.game.leader = self.me
                self.game.players.append(self.me)
                self.broadcast(CODES.INVITE, self.online_addresses, self.me)
                log(self.debug, CODES.INVITE, "Send Invitation:")
            else:
                print("Can not invite")

        elif code == CODES.ACC_INVITE:
            if self.game.game_state == GAME_STATUS.INVITE:
                self.broadcast(CODES.ACC_INVITE, self.online_addresses, self.me)

        elif code == CODES.START:
            if self.game.leader == self.me:
                self.game.game_state == GAME_STATUS.YES
                self.broadcast(CODES.START, self.online_addresses, " ".join(self.game.players))
                log(self.debug, CODES.START, "Players: "+ str(self.game.players))

        else:
            print("Unkown Command!!")





