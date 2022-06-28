def mymax(iterable, key = lambda k:k):
  # incijaliziraj maksimalni element i maksimalni ključ
  max_x=max_key=None

  # obiđi sve elemente
  for x in iterable:
    if(max_key == None or key(x) > max_key):
        max_key = key(x)
        max_x = x
    # ako je key(x) najveći -> ažuriraj max_x i max_key

  # vrati rezultat
  return max_x

imena = ["Marko", "Filip", "Ana Marija","Hej"]
print(mymax(imena, lambda k: len(k)))
print(mymax([1, 3, 5, 7, 4, 6, 9, 2, 0]))
print(mymax("Suncana strana ulice"))
print(mymax([
  "Gle", "malu", "vocku", "poslije", "kise",
  "Puna", "je", "kapi", "pa", "ih", "njise"]))

D={'burek':8, 'buhtla':5}
print(mymax(D,D.get))

lista = [
    ["Filip","Pavičić"],
    ["Ana Marija","Pavičić"],
    ["Dora", "Doljanin"],
    ["Pero", "Antić"]
]
print(mymax(lista, lambda k: k[1] + k[0]))