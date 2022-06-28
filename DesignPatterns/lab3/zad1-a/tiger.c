#include <stdlib.h>

typedef char const* (*PTRFUN)();

struct Tiger
{
    PTRFUN *vtable;
    const char* name;
};

char const* greet(void) {
  return "Roarrr!";
}
char const* menu(void) {
  return "antilope";
}

char const* name(struct Tiger *Tiger){
    return Tiger->name;
}

PTRFUN  table[3] = {
    (PTRFUN) name,
    (PTRFUN) greet,
    (PTRFUN) menu
};

void construct(struct Tiger *p, char *name) {
  p->name = name;
  p->vtable = table;
}

void* create(char *name) {
  //gomila
  struct Tiger *tiger = (struct Tiger *)malloc(sizeof(struct Tiger));
  construct(tiger, name);
  return (void*) tiger;
}

int sizeOf(){
    sizeof(struct Tiger);
}



