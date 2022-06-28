from gameState import GameState

players = ["Filip","Dora","Ana","Hari"]
gs = GameState()
gs.players = players
gs.startGame()
dic = { # relative positions for each player
    "Filip" : {"a":0,"b":0,"c":0,"d":0},
    "Dora" : {"a":0,"b":0,"c":0,"d":0},
    "Ana" : {"a":0,"b":0,"c":0,"d":0},
    "Hari" : {"a":0,"b":0,"c":0,"d":0},
}
def test0():
    assert gs.ps == dic, "Test0 Failed"

def test1():
    relative = 0
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Filip"])
    assert absolute == 0, "Test1 Failed"

def test2():
    relative = 1
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Filip"])
    assert absolute == 1, "Test2 Failed Filip"
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Dora"])
    assert absolute == 11, "Test2 Failed Dora"
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Ana"])
    assert absolute == 21, "Test2 Failed Ana"
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Hari"])
    assert absolute == 31, "Test2 Failed Hari"

def test3():
    relative = 15
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Filip"])
    assert absolute == 15, "Test3 Failed Filip"
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Dora"])
    assert absolute == 25, "Test3 Failed Dora"
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Ana"])
    assert absolute == 35, "Test3 Failed Ana"
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Hari"])
    assert absolute == 5, "Test3 Failed Hari"

def test4():
    relative = 41
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Filip"])
    assert absolute == 41, "Test4 Failed Filip"
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Dora"])
    assert absolute == 51, "Test4 Failed Dora"
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Ana"])
    assert absolute == 61, "Test4 Failed Ana"
    absolute = gs.relativeToApsolute(relative, gs.playersIndex["Hari"])
    assert absolute == 71, "Test4 Failed Hari"

def test5():
    nextPlayer = gs.getNextPlayer("Filip")
    assert nextPlayer == "Dora", "Test5 Failed Filip"
    nextPlayer = gs.getNextPlayer("Dora")
    assert nextPlayer == "Ana", "Test5 Failed Dora"
    nextPlayer = gs.getNextPlayer("Ana")
    assert nextPlayer == "Hari", "Test5 Failed Ana"
    nextPlayer = gs.getNextPlayer("Hari")
    assert nextPlayer == "Filip", "Test5 Failed Hari"

def test6():
    dic1 = { # relative positions for each player
    "Filip" : {"a":0,"b":0,"c":0,"d":0},
    "Dora" : {"a":0,"b":0,"c":0,"d":0},
    "Ana" : {"a":5,"b":22,"c":41,"d":43},
    "Hari" : {"a":0,"b":41,"c":42,"d":38},
    }
    gs.ps = dic1
    pieces = gs.getAvailablePieces("Filip", 6)
    assert pieces == ["a","b","c","d"], "Test6 Failed Filip"
    pieces = gs.getAvailablePieces("Dora", 4)
    assert pieces == [], "Test6 Failed Dora"
    pieces = gs.getAvailablePieces("Ana", 3)
    assert pieces == ["a","b","c"], "Test6 Failed Ana"
    pieces = gs.getAvailablePieces("Hari", 6)
    assert pieces == ["a","d"], "Test6 Failed Hari"

def test7():
    played = gs.play("Filip", forTestONLY = 'a', dieforTestONLY = 6)
    assert played == (True ,['a',1]), "Test7 Failed Filip"
    played = gs.play("Dora", forTestONLY = 'exit', dieforTestONLY = 3)
    assert played == (False, None), "Test7 Failed Dora"
    played = gs.play("Ana", forTestONLY = 'exit', dieforTestONLY = 2)
    assert played == 'exit', "Test7 Failed Ana"
    played = gs.play("Hari", forTestONLY = 'd', dieforTestONLY = 5)
    assert played == (False ,['d',43]), "Test7 Failed Hari"
    played = gs.play("Filip", forTestONLY = 'a', dieforTestONLY = 6)
    assert played == (True ,['a',7]), "Test7 Failed Filip2"
    played = gs.play("Dora", forTestONLY = 'exit', dieforTestONLY = 6)
    assert played == 'exit', "Test7 Failed Dora2"
    played = gs.play("Hari", forTestONLY = 'd', dieforTestONLY = 1)
    assert played == (False, ['d', 44]), "Test7 Failed Hari2"
    played = gs.play("Hari", forTestONLY = 'b', dieforTestONLY = 1)
    assert played == (False ,['b',42]), "Test7 Failed Hari3"

def test8():
    dic2 = { # relative positions for each player
    "Filip" : {"a":0,"b":6,"c":13,"d":19},
    "Dora" : {"a":32,"b":0,"c":0,"d":0},
    "Ana" : {"a":10,"b":22,"c":41,"d":43},
    "Hari" : {"a":1,"b":41,"c":42,"d":1},
    }
    gs.ps = dic2
    # Filip's "c" cannot kill his "d"
    gs.play("Filip", forTestONLY = 'c', dieforTestONLY = 6)
    assert gs.ps["Filip"]["c"] == 19, "Test8 Failed Filip"
    assert gs.ps["Filip"]["d"] == 19, "Test8 Failed Filip2"
    # Dora's "a" kills Filip's "b"
    gs.play("Dora", forTestONLY = 'a', dieforTestONLY = 4)
    assert gs.ps["Dora"]["a"] == 36, "Test8 Failed Dora"
    assert gs.ps["Filip"]["b"] == 0, "Test8 Failed Filip3"
    # Ana's "a" kills Hari's "a" and "d"
    gs.play("Ana", forTestONLY = 'a', dieforTestONLY = 1)
    assert gs.ps["Ana"]["a"] == 11, "Test8 Failed Ana"
    assert gs.ps["Hari"]["a"] == 0, "Test8 Failed Hari"
    assert gs.ps["Hari"]["d"] == 0, "Test8 Failed Hari2"
    # Hari's "b" cannot kill his "c"
    gs.play("Hari", forTestONLY = 'b', dieforTestONLY = 1)
    assert gs.ps["Hari"]["b"] == 42, "Test8 Failed Hari3"
    assert gs.ps["Hari"]["c"] == 42, "Test8 Failed Hari4"

def test9():
    # Dora's rel pos 36 is Filip's rel pos 6, so Filip's "a" kills Dora's "a"
    move = ["a", 6]
    gs.loadMove("Filip", move)
    move = ["b", 0]
    gs.loadMove("Filip", move)
    move = ["c", 0]
    gs.loadMove("Filip", move)
    move = ["d", 0]
    gs.loadMove("Filip", move)
    assert gs.ps["Dora"]["a"] == 0, "Test9 Failed Dora"
    assert gs.ps["Filip"]["a"] == 6, "Test9 Failed Filip"
    assert gs.ps["Filip"]["b"] == 0, "Test9 Failed Filip2"
    assert gs.ps["Filip"]["c"] == 0, "Test9 Failed Filip3"
    assert gs.ps["Filip"]["d"] == 0, "Test9 Failed Filip4"
    # turn Dora into a winner
    move = ["a", 41]
    gs.loadMove("Dora", move)
    move = ["b", 43]
    gs.loadMove("Dora", move)
    move = ["c", 44]
    gs.loadMove("Dora", move)
    move = ["d", 42]
    gs.loadMove("Dora", move)
    assert gs.ps["Dora"]["a"] == 41, "Test9 Failed Dora2"
    assert gs.ps["Dora"]["b"] == 43, "Test9 Failed Dora3"
    assert gs.ps["Dora"]["c"] == 44, "Test9 Failed Dora4"
    assert gs.ps["Dora"]["d"] == 42, "Test9 Failed Dora5"

def test10():
    winner = gs.getWinner()
    assert winner == "Dora", "Test10 Failed Dora"

test0()
test1()
test2()
test3()
test4()
test5()
test6()
test7()
test8()
test9()
test10()

print("All tests passed")