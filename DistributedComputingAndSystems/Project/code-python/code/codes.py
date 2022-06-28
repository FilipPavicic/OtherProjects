from enum import Enum, auto

class CODES(Enum):
    # example RED = "red"
    HELLO = "hello"
    HELLO_RPL = "hello_rpl"
    TEXT = "text"
    EXIT = "exit"
    INVITE = "invite"
    ACC_INVITE = "acc_invite"
    START = "start"
    LEADER = "leader"
    ON_MOVE = "on_move"
    PLAYED_MOVE = "played move"
    WINNER = "winner"
    RETURN = "return"
    UPDATE_STATE = "update_state"
    TEST = "test"
    

    def encode(stringName):
        for code in CODES:
            if code.value == stringName:
                return code
        return None

    def decode(self):
        return self.value
    
    def __str__(self) -> str:
        return str(self.value)
