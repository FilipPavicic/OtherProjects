from tools import log
from playerClient import PlayerClient
from codes import CODES
from gameStatus import GAME_STATUS

debug = True

print("START")
me = input("enter name: ")

with PlayerClient(debug) as client:

    client.broadcast(CODES.HELLO, client.address)

    while True:
        inputMessage = input()
        
        if inputMessage == "exit":
            client.doCommand(CODES.EXIT)
        elif inputMessage.startswith("invite"):
            client.doCommand(CODES.INVITE)
        
        elif inputMessage.startswith("acc_invite"):
            client.doCommand(CODES.ACC_INVITE)

        elif inputMessage.startswith("start"):
            client.doCommand(CODES.START)

        else:
            client.broadcast(CODES.TEXT, client.online_addresses, inputMessage)
