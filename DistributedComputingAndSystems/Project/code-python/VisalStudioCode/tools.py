def log(debug,code,*args):
    if debug:
        print("CODE = ", code)
        for arg in args:
            print(str(arg))