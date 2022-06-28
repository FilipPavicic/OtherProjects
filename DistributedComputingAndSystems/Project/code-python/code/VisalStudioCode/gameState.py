from gameStatus import GAME_STATUS

class GameState():
    def __init__(self) -> None:
        self.game_state = GAME_STATUS.NO
        self.leader = None
        self.players = []