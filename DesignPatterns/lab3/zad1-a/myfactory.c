#include "myfactory.h"
#include <dlfcn.h>
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>



typedef void* (*CONSTRUCOTR)(char const *);
typedef int (*INTFUN)(); 
typedef void* (*INITIALIZATION)(void*, char const *);


void *myfactory(char const *libname, char const *ctorarg) {
    void* handle = dlopen(libname, RTLD_LAZY);
    if(! handle) return NULL;
    CONSTRUCOTR c = (CONSTRUCOTR) dlsym(handle, "create");
    return c(ctorarg);
}

void *myfactory2(char const *libname, char const *ctorarg) {
    void* handle = dlopen(libname, RTLD_LAZY);
    if(! handle) return NULL;
    INTFUN sizeOf = (INTFUN) dlsym(handle, "sizeOf");
    INITIALIZATION init = (INITIALIZATION) dlsym(handle, "construct");
    //void* animal = (void*) malloc(sizeOf());
    char animal1[sizeOf()];
    void* animal = (void*) animal1;
    init(animal,ctorarg);
    return animal;
}
