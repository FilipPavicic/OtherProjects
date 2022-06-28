from Utils import *
import bisect
import heapq
import time


c = 0
sumBFS = 0
sumUCS = 0
sumASTAR = 0

data = read_input('3x3_puzzle.txt')
data = read_funcs(data,'3x3_misplaced_heuristic.txt')
for i in range(0,10):
    c += 1
    print("KRUG: " + str(c))
    start = time.time()
    cilj = BFS_search(data)
    end = time.time()
    sumBFS = sumBFS + end - start

    start = time.time()
    cilj = UCS_search(data)
    end = time.time()
    sumUCS= sumUCS + end - start

    start = time.time()
    cilj = ASTAR_search(data)
    end = time.time()
    sumASTAR = sumASTAR + end - start


print("BFS: "+str(sumBFS/ c))
print("UCS: "+str(sumUCS/ c))
print("ASTAR: "+str(sumASTAR/ c))