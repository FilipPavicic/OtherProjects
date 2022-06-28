from gameStatus import GAME_STATUS
import random

from tools import gamePrint

test = False
testic = {
    'Dora': {'a': 0, 'b': 0, 'c': 28, 'd': 0}, 
    'Hari': {'a': 11, 'b': 14, 'c': 0, 'd': 0}, 
    'Filip': {'a': 0, 'b': 0, 'c': 0, 'd': 0}
    }

class GameState():
    def __init__(self) -> None:
        self.dic_TEST_ONLY = 0
        self.game_state = GAME_STATUS.NO
        self.leader = None
        self.players = []
        self.playersIndex = None
        self.onMovePlayer = None
        self.ps = None
    
    def setDicTEST_ONLY(self, num):
        self.dic_TEST_ONLY = num

    def setPLayersIndex(self):
        self.playersIndex = {}
        i = 0
        for p in self.players:
            self.playersIndex[p] = i
            i+= 1

    def startGame(self):
        if test:
            self.ps = testic
        else:
            self.ps = self.startPosition()
        self.setPLayersIndex()

    def getNextPlayer(self, player):
        return self.players[(self.players.index(player) +1) % len(self.players)]

    def relativeToApsolute(self, relative, playerIndex):
        if relative == 0:
            apsolute = 0
        elif relative <= 40:
            apsolute = (playerIndex * 10 + relative - 1) % 40 + 1
        else:
            apsolute = relative +  playerIndex * 10 
        return apsolute  

    def startPosition(self):
        dic = dict()
        for i in self.players:
            dic[i] = {'a':0,'b':0,'c':0,'d':0}
        return dic

    def play(self, player, forTestONLY = 0): #return to none
        die = random.randint(1,6) if self.dic_TEST_ONLY == 0 else self.dic_TEST_ONLY
        gamePrint("Your number on die is: "+ str(die))
        
        pieces  = self.getAvailablePieces(player,die)

        if len(pieces) == 0:
            gamePrint("You can not move any of your pieces")
            return (False,None)
        else:
            gamePrint("You can move on of following piceses: "+ str(pieces))
            move_piece = self.getMovePiece(pieces) if forTestONLY == 0 else forTestONLY
            if move_piece == "exit": return "exit"
            if self.ps[player][move_piece] == 0 and die == 6:
                self.ps[player][move_piece] = 1
            else:
                self.ps[player][move_piece] += die
                self.checkOthers(self.ps[player][move_piece], player) # if someone is killed
            cont = die == 6
            return (cont ,[move_piece,self.ps[player][move_piece]])
    
    def loadMove(self, player, move): 
        if move == None:
            return
        piece = move[0]
        position = move[1]
        self.ps[player][piece] = position
        self.checkOthers(self.ps[player][piece], player)

    def getWinner(self):
        for p in self.players:
            positions = list(self.ps[p].values())
            positions.sort()
            if [41,42,43,44] == positions:
                return p
        return None
      
    def getMovePiece(self,pieces):
        inp = input("\n")
        while inp not in pieces and inp != "exit":
            gamePrint("Enter one of available pieces")
            inp = input("\n")
        return inp

    def getAvailablePieces(self, player,die):
        items = self.ps[player].items()
        pieces = [k for k,v in items if v != 0 and v + die <= 40]
        taken_in_hause = [v for k, v in items if v > 40]
        pieces.extend([k for k,v in items if (v + die) > 40 and (v + die) <= 44 and
         (v + die) not in taken_in_hause])
        if die == 6: pieces.extend([k for k,v in items if v == 0])
        return sorted(pieces)

    def checkOthers(self,relative, player):
        absolute = self.relativeToApsolute(relative, self.playersIndex[player])
        for k, v in self.ps.items():
            if k == player:
                continue
            for piece, rel in v.items():
                if rel == 0:
                    continue
                piece_abs = self.relativeToApsolute(rel, self.playersIndex[k])
                if absolute == piece_abs:
                    self.ps[k][piece] = 0

    def stateToText(self):
        info = [self.leader, str(self.players), str(self.playersIndex),str(self.onMovePlayer), str(self.ps)]
        return "##".join(info)

    def setState(self, text):
        info = text.split("##")
        self.leader = info[0]
        self.players = eval(info[1])
        self.playersIndex = eval(info[2])
        self.onMovePlayer = eval(info[3])
        self.ps = eval(info[4])
        self.game_state = GAME_STATUS.YES


