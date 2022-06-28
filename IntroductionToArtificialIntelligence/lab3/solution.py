from ID3 import ID3
import sys, os
   
args = sys.argv[1:]
dubina = None if len(args) < 3 else int(args[2])

id3 = ID3(dubina)
tree = id3.fit(args[0])
id3.printTree()
prections = id3.predict(args[1])
print('[PREDICTIONS]: '+ ' '.join(prections))
accuracy = id3.accuracy()
print('[ACCURACY]: %.5f' % round(accuracy,5))
confusionMatrix = id3.confusionMatrix()
print('[CONFUSION_MATRIX]:')
for row in confusionMatrix.values():
    print(' '.join(list(map(lambda e: str(e),row.values()))))
