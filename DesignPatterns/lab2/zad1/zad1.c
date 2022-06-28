#include<stdio.h>
#include<string.h>
#include <stdlib.h>

void qsort(void *base, size_t nitems, size_t size, int (*compar)(const void *, const void*)){
    void* t = malloc(size);
    for(int i = 0; i < nitems -1; i++){
        for(int j = i +1; j < nitems; j++){
            if(compar( base + (i * size), base + (j * size)) > 0){
                memcpy(t, base + (i * size),size);
                memcpy(base + (i * size), base + (j * size),size);
                memcpy(base + (j * size), t,size);
            }   
        }
    }
}

int gt_int (const void * a, const void * b) {
   return ( *(int*)a - *(int*)b );
}

int gt_char (const void * a, const void * b) {
   return ( *(char*)a - *(char*)b );
}
int gt_str (const void * a, const void * b) {
   int i = strcmp(*(char**)a,*(char**)b);
   return i;
}


int main () {
    int arr_int[] = { 1, 3, 5, 7, 4, 6, 9, 2, 0 };
    char arr_char[]="Suncana strana ulice";
    const char* arr_str[] = {
        "Gle", "malu", "vocku", "poslije", "kise",
        "Puna", "je", "kapi", "pa", "ih", "njise"
    };
    qsort(arr_int,sizeof(arr_int) / sizeof(int), sizeof(int),gt_int);
    qsort(arr_char,sizeof(arr_char) / sizeof(char), sizeof(char),gt_char);
    qsort(arr_str,sizeof(arr_str) / sizeof(char*), sizeof(char*),gt_str);
    printf("Sortirani arr_int: \n");
    for(int i = 0; i < sizeof(arr_int) / sizeof(int); i++){
        printf("%d ",arr_int[i]);
    }
    printf("\n");
    printf("Sortirani arr_char: \n");
    for(int i = 0; i < sizeof(arr_char) / sizeof(char); i++){
        printf("%c",arr_char[i]);
    }
    printf("\n");
    printf("Sortirani arr_str: \n");
    for(int i = 0; i < sizeof(arr_str) / sizeof(char*); i++){
        printf("%s ",arr_str[i]);
    }
    printf("\n");
}