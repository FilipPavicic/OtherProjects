def log(debug,code,*args):
    if debug:
        print("[CODE] ==> ["+ str(code).upper()+ "] "+ ", ".join([str(a) for a in args]))

def gamePrint(text):
    print("[GAME INTERFACE] ==> "+ text)