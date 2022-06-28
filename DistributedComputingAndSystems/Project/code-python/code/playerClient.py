from email import message
from os import system
from UDPClient import UDPClient
from gameState import GameState
from codes import CODES
from gameStatus import GAME_STATUS
from tools import gamePrint, log

class PlayerClient(UDPClient):
    def __init__(self, me, debug=False):
        super().__init__(debug)
        self.online_addresses = set()
        self.me = me
        self.game = GameState()
        self.returnProcess = set()
        self.broadcastToMe = [CODES.PLAYED_MOVE, CODES.RETURN, CODES.WINNER]
    
    def broadcast(self,code,addresses,message= ""):
        UDPClient.broadcast(self,code, addresses, message)
        if code in self.broadcastToMe and self.address in addresses:
            message_me = self.createMessage(code,message)
            self.readMessage(message_me)
            addresses.remove(self.address)  
    
    def readMessage(self, message):
        code, sender, message = message.split("::")
        code = CODES.encode(code)
        sender = int(sender)


        if code == CODES.HELLO:
            self.online_addresses.add(sender)
            log(self.debug,code, "Change in online addresses", self.online_addresses)
            self.broadcast(CODES.HELLO_RPL, [sender])
            
            if self.game.game_state == GAME_STATUS.INVITE and self.me == self.game.leader:
                self.broadcast(CODES.INVITE, [sender], self.me)
            
            if self.game.game_state == GAME_STATUS.YES and self.me == self.game.leader and message in self.game.ps.keys():
                self.doCommand(CODES.RETURN, extras=message)
                self.returnProcess.add(sender)

        elif code == CODES.HELLO_RPL:
            self.online_addresses.add(sender)

            log(self.debug,code, "Change in online addresses", self.online_addresses)

        elif code == CODES.EXIT:
            if message in self.game.players:
                if message == self.game.leader and self.game.getNextPlayer(message) == self.me:
                    self.doCommand(CODES.LEADER)
            if self.game.leader == self.me and message == self.game.onMovePlayer:
                nextPLayer = self.game.getNextPlayer(message)
                self.doCommand(CODES.ON_MOVE,extras=nextPLayer, addresses=[sender])
            self.game.players.remove(message)
            self.online_addresses.remove(sender)
            log(self.debug,code, "Change in online addresses", self.online_addresses)


        elif code == CODES.INVITE:
            if self.game.game_state != GAME_STATUS.YES:
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
            self.game.startGame()
            log(self.debug,code, "Players: "+ str(self.game.players))
        
        elif code == CODES.LEADER:
            if self.game.game_state == GAME_STATUS.YES:
                self.game.leader = message
                log(self.debug,code, "Changed Leader: "+ self.game.leader)

        elif code == CODES.ON_MOVE:
            if self.game.game_state == GAME_STATUS.YES:
                self.game.onMovePlayer = message
                log(self.debug, code, "Players on move: "+ self.game.onMovePlayer)
                if self.game.onMovePlayer == self.me:
                    gamePrint("Your turn to play, write play to play move")
        
        elif code == CODES.PLAYED_MOVE:
            if self.game.game_state == GAME_STATUS.YES:
                player,cont, move = self.textToMove(message)
                self.game.loadMove(player,move)
                log(self.debug, code, "Recive move: "+ message)
                gamePrint(str(self.game.ps))
                self.sendOnMove(player, cont)

        elif code == CODES.WINNER:
            if self.game.game_state == GAME_STATUS.YES:
                gamePrint("Player: "+ message + " is the winner!!")
                self.game = GameState()
        
        elif code == CODES.UPDATE_STATE:
            self.game.setState(message)
            log(self.debug, code, "Updated state: "+ message)
        
        elif code == CODES.RETURN:
            self.game.players.append(message)
            if message == self.me:
                log(self.debug, code, "I am back to the game")
            else:
                log(self.debug, code, message +" is back to the game")
                                           
        elif code == CODES.TEXT:
            log(self.debug,code, message)
        
        else:
            log(self.debug, message)
        print()

    
    def doCommand(self,code: CODES, extras = None, addresses = None):
        
        if code == CODES.EXIT:
            self.broadcast(CODES.EXIT, self.online_addresses, self.me)
        
        elif code == CODES.HELLO:
            log(self.debug, code, "Sending Hello")
            self.broadcast(CODES.HELLO, self.addresses,self.me)

        elif code == CODES.INVITE:
            if self.game.game_state == GAME_STATUS.NO:
                self.game.game_state = GAME_STATUS.INVITE
                self.game.leader = self.me
                self.game.players.append(self.me)
                self.broadcast(CODES.INVITE, self.online_addresses, self.me)
                log(self.debug, CODES.INVITE, "Send Invitation:")
            else:
                log(self.debug,CODES.INVITE,"Can not invite")

        elif code == CODES.ACC_INVITE:
            if self.game.game_state == GAME_STATUS.INVITE:
                self.broadcast(CODES.ACC_INVITE, self.online_addresses, self.me)
            else:
                log(self.debug, CODES.ACC_INVITE, "You have not been invited")

        elif code == CODES.START:
            if self.game.game_state == GAME_STATUS.INVITE:
                if self.game.leader == self.me:
                    self.game.game_state = GAME_STATUS.YES
                    self.game.startGame()
                    self.broadcast(CODES.START, self.online_addresses, " ".join(self.game.players))
                    log(self.debug, CODES.START, "Send Players: "+ str(self.game.players))
                    self.doCommand(CODES.ON_MOVE, extras=self.me)
                else:
                    log(self.debug, code, "Only the leader can start the game")
            else:
                log(self.debug, code, "Cannot start the game without invite")
        
        elif code == CODES.LEADER:
            if self.game.game_state == GAME_STATUS.YES:
                self.game.leader = self.me
                self.broadcast(CODES.LEADER, self.online_addresses, self.me)
                log(self.debug,code, "Send Changed Leader: "+ self.game.leader)
        
        elif code == CODES.ON_MOVE:
            if self.game.game_state == GAME_STATUS.YES:
                self.game.onMovePlayer = extras
                self.broadcast(CODES.ON_MOVE, self.online_addresses, self.game.onMovePlayer)
                if self.game.onMovePlayer == self.me:
                    gamePrint("Your turn to play, write play to play move")
        
        elif code == CODES.PLAYED_MOVE:
            if self.game.game_state == GAME_STATUS.YES:
                if self.game.onMovePlayer == self.me:
                    play = self.game.play(self.me)
                    if play == "exit":
                        self.doCommand(CODES.EXIT)
                        return True
                    cont, data = play
                    text = self.moveToText(cont, data)
                    self.game.onMovePlayer = None
                    log(self.debug, code, "Sending move: "+ text)
                    self.broadcast(CODES.PLAYED_MOVE, list(self.online_addresses) + [self.address], text)
                else:
                    gamePrint("It is not your move.")

        elif code == CODES.UPDATE_STATE:
            self.broadcast(code,addresses=addresses,message=extras)
            log(self.debug, code, "Send Upadated state to adress: " + str(addresses))
                
        elif code == CODES.WINNER:
            if self.game.game_state == GAME_STATUS.YES:
                self.broadcast(CODES.WINNER,addresses, extras)
        
        elif code == CODES.RETURN:
            self.broadcast(code,list(self.online_addresses) + [self.address], extras)
            log(self.debug, code, "Inform all players about the returning player")

        else:
            print("Unkown Command!!")

    def moveToText(self,cont, move):
        return "<>".join([self.me, str(cont), str(move)])

    def textToMove(self, text):
        text = text.split("<>")
        player = text[0]
        cont = eval(text[1])
        move = eval(text[2])
        return player, cont, move

    def sendOnMove(self, player, cont):
        if self.game.leader == self.me:
            log(self.debug, CODES.ON_MOVE, "Sending on move")
            if len(self.returnProcess) != 0:
                self.doCommand(CODES.UPDATE_STATE, addresses= self.returnProcess, extras=self.game.stateToText())
                self.returnProcess = set()     
            winner = self.game.getWinner()
            if winner != None:
                self.doCommand(CODES.WINNER, extras=winner, addresses= list(self.online_addresses) + [self.address])
                return
            nextPLayer = self.game.getNextPlayer(player) if cont == False else player
            self.doCommand(CODES.ON_MOVE, extras=nextPLayer)
            




