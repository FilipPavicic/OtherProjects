from email import message
from tools import log
from playerClient import PlayerClient
from codes import CODES
from gameStatus import GAME_STATUS

debug = True

class Game():
    def __init__(self) -> None:
        self.play()

    def play(self):
        print("START")
        me = input("enter name: ")

        with PlayerClient(me,debug) as client:

            client.doCommand(CODES.HELLO)

            while True:
                inputMessage = input("\n")
                if inputMessage == "exit":
                    client.doCommand(CODES.EXIT)
                    break
                elif inputMessage.startswith("invite"):
                    client.doCommand(CODES.INVITE)
                
                elif inputMessage.startswith("acc_invite"):
                    client.doCommand(CODES.ACC_INVITE)

                elif inputMessage.startswith("start"):
                    client.doCommand(CODES.START)
                
                elif inputMessage.startswith("play"):
                    out = client.doCommand(CODES.PLAYED_MOVE)
                    if out == True:
                        break
                elif inputMessage.startswith("return"):
                    client.doCommand(CODES.RETURN)
                elif inputMessage.startswith("die"):
                    dic = int(inputMessage.split(" ")[1])
                    if dic != 0: log(debug, CODES.TEST, "DIC FOR TESTING: " + str(dic))
                    client.game.setDicTEST_ONLY(dic)
                else:
                    client.broadcast(CODES.TEXT, client.online_addresses, inputMessage)



Game()
