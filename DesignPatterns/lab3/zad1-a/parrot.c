#include <stdlib.h>

typedef char const* (*PTRFUN)();

struct Parrot
{
    PTRFUN *vtable;
    const char* name;
};

char const* greet(void) {
  return "pipi!";
}
char const* menu(void) {
  return "jabuku";
}

char const* name(struct Parrot *parrot){
    return parrot->name;
}

PTRFUN  table[3] = {
    (PTRFUN) name,
    (PTRFUN) greet,
    (PTRFUN) menu
};

void construct(struct Parrot *p, char *name) {
  p->name = name;
  p->vtable = table;
}

void* create(char *name) {
  //gomila
  struct Parrot *parrot = (struct Parrot *)malloc(sizeof(struct Parrot));
  construct(parrot, name);
  return (void*) parrot;
}

int sizeOf(){
    sizeof(struct Parrot);
}



