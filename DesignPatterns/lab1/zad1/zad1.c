#include "animal.h"
#include <stdio.h>
#include <stdlib.h>

void testAnimals(void){
  struct Animal* p1=createDog("Hamlet");
  
  struct Animal* p3=createDog("Polonije");
  struct Animal* p2=createCat("Ofelija");

  animalPrintGreeting(p1);
  animalPrintGreeting(p2);
  animalPrintGreeting(p3);

  animalPrintMenu(p1);
  animalPrintMenu(p2);
  animalPrintMenu(p3);

  free(p1); free(p2); free(p3);
}

void testNAnimals(int n) {
  struct Animal* p = createNDogs(n);
  for (int i = 0; i < n; i++)
  {
    animalPrintGreeting(&p[i]);
  }
  
}
int main() {
  testAnimals();
  int n = 10;
  testNAnimals(n);
} 