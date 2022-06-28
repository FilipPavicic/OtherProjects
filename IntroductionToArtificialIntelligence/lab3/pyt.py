e = {
    'b': 3,
    'a' : 4,
    'd' : 2
}
max = None
for value in e.items():
    if(max == None or ( value[1] == max[1] and value[0] < max[0])):
        max = value
        continue
    if(max == None or value[1] > max[1]):
        max = value
print(max)

accuracy = 1.5
print('[ACCURACY]: %.5f' % round(accuracy,5))