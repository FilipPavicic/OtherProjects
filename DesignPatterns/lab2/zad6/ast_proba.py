import ast, operator

binOps = {
    ast.Add: operator.add,
    ast.Sub: operator.sub,
    ast.Mult: operator.mul,
    ast.Div: operator.floordiv,
    ast.Mod: operator.mod
}

def arithmeticEval (s):
    node = ast.parse(s, mode='eval')

    def _eval(node):
        if isinstance(node, ast.Expression):
            return _eval(node.body)
        elif isinstance(node, ast.Str):
            return node.s
        elif isinstance(node, ast.Name):
            print(node.id)
            return None
        elif isinstance(node, ast.Num):
            return node.n
        elif isinstance(node, ast.BinOp):
            try:
                return binOps[type(node.op)](_eval(node.left), _eval(node.right))
            except TypeError:
                return None
            
        else:
            raise Exception('Unsupported type {}'.format(node))

    return _eval(node.body)

print(arithmeticEval('A1+2'))