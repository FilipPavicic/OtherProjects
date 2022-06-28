class Utils:
    @staticmethod
    def readCSV(path):
        with open(path, encoding="utf-8") as f:
            content = f.readlines()
        lista = []
        for i,line in enumerate(content):
            if i == 0:
                headers = line[:-1].split(',')
                continue
            mapa = {}
            for j,el in enumerate(line[:-1].split(',')):
                mapa[headers[j]] = el
            lista.append(mapa)
        return lista