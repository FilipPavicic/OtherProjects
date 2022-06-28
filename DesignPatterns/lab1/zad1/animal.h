#include <cstdlib>
#include <stdio.h>

typedef char const *(*PTRFUN)();

char const *dogGreet(void) {
  return "vau!";
}
char const *dogMenu(void) {
  return "kuhanu govedinu";
}
char const *catGreet(void) {
  return "mijau!";
}
char const *catMenu(void) {
  return "konzerviranu tunjevinu";
}

typedef struct {
  PTRFUN greet;
  PTRFUN menu;
} ptrTable;

struct Animal {
  const char *name;
  ptrTable *functions;
};

ptrTable funcs[2] = {
    {dogGreet, dogMenu},
    {catGreet, catMenu}};

void animalPrintGreeting(struct Animal *animal) {
  printf("%s pozdravlja %s!\n", animal->name, animal->functions->greet());
}

void animalPrintMenu(struct Animal *animal) {
  printf("%s voli %s!\n", animal->name, animal->functions->menu());
}

void constructDog(struct Animal *p, char *name) {
  p->name = name;
  p->functions = &funcs[0];
}

void constructCat(struct Animal *p, char *name) {
  p->name = name;
  p->functions = &funcs[1];
}

struct Animal *createDog(char *name) {
  //stog
  struct Animal *dog = (struct Animal *)new char[sizeof(struct Animal)];
  constructDog(dog, name);
  return dog;
}

struct Animal *createCat(char *name) {
  //gomila
  struct Animal *cat = (struct Animal *)malloc(sizeof(struct Animal));
  constructCat(cat, name);
  return cat;
}

struct Animal *createNDogs(int n) {
  struct Animal *animals = (struct Animal *)malloc(sizeof(struct Animal) * n);
  for (int i = 0; i < n; i++) {
    constructDog(&animals[i], "Dog");
  }
  return animals;
}