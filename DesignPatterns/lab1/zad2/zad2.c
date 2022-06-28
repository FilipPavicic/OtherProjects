#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h> 

struct Unary_Function_Tag;

typedef double (*Value_at)(void*,double);
typedef double (*Negative_value_at)(Unary_Function_Tag*,double);
typedef void (*Tabulate)(Unary_Function_Tag*);
typedef bool (*Same_functions_for_ints)(Unary_Function_Tag*, Unary_Function_Tag*, double);

typedef struct {
    Value_at value_at;
    Negative_value_at negative_value_at;
    Tabulate tabulate;
    Same_functions_for_ints same_functions_for_ints ;
} UnaryFunctionTable;

typedef struct Unary_Function_Tag{
    int lower_bound;
    int upper_bound;
    UnaryFunctionTable *table;
}Unary_Function ;

typedef struct {
    Unary_Function unary_Function;
} Square;

typedef struct {
    Unary_Function unary_Function;
    double a;
    double b;
    
} Linear;

void tablature_Unary_Function(Unary_Function *thiss){
    
    for (int x = thiss->lower_bound; x <= thiss->upper_bound; x++) {
      printf("f(%d)=%lf\n", x, thiss->table->value_at(thiss,x));
    }
}
static bool same_functions_for_ints_Unary_Function(Unary_Function *f1, Unary_Function *f2, double tolerance) {
    if (f1->lower_bound != f2->lower_bound)
      return false;
    if (f1->upper_bound != f2->upper_bound)
      return false;
    for (int x = f1->lower_bound; x <= f1->upper_bound; x++) {
      double delta = f1->table->value_at(f1,x) - f2->table->value_at(f2,x);
      if (delta < 0)
        delta = -delta;
      if (delta > tolerance)
        return false;
    }
    return true;
  };

double negative_value_at_Unary_Function(Unary_Function *thiss,double x) {
    return -thiss->table->value_at(thiss,x);
  }



double value_at_Squere(void *thiss, double x) {
    return x * x;
}
double value_at_Linear(void *thiss, double x) {
    Linear *lin = (Linear*) thiss;
    return lin->a * x + lin->b;
}

UnaryFunctionTable funs[2] = {
    {value_at_Squere,negative_value_at_Unary_Function,tablature_Unary_Function,same_functions_for_ints_Unary_Function},
    {value_at_Linear,negative_value_at_Unary_Function,tablature_Unary_Function,same_functions_for_ints_Unary_Function}
};



void constructUnary_Function(Unary_Function *p, int lower_bound, int upper_bound, int i){
    p->lower_bound = lower_bound;
    p->upper_bound = upper_bound;
    p->table = &funs[i];


}

void constructSquare(Square *p, int lower_bound, int upper_bound){
    constructUnary_Function(&p->unary_Function,lower_bound,upper_bound,0);
}

void constructLinear(Linear *p, int lower_bound, int upper_bound,double a,double b){
    p->a = a;
    p->b = b;
    constructUnary_Function(&p->unary_Function,lower_bound,upper_bound,1);
}

Square * createSquare(int lower_bound, int upper_bound){
    Square *s = (Square*)malloc(sizeof(Square));
    constructSquare(s,lower_bound,upper_bound);
    return s;
}

Linear * createLinear(int lower_bound, int upper_bound,double a, double b){
    Linear *l = (Linear*)malloc(sizeof(Linear));
    constructLinear(l,lower_bound,upper_bound,a,b);
    return l;

}

int main() {
    Unary_Function *f1 = (Unary_Function*) createSquare(-2, 2);
    f1->table->tabulate(f1);
    Unary_Function *f2 = (Unary_Function*) createLinear(-2, 2, 5, -2);
    f2->table->tabulate(f2);
    printf("f1==f2: %s\n", same_functions_for_ints_Unary_Function(f1, f2, 1E-6) ? "DA" : "NE");
    printf("neg_val f2(1) = %lf\n", f2->table->negative_value_at(f2,1.0));
    free(f1);
    free(f2);
    return 0;
}