#include <stdio.h>

class Base {
public:
  Base() {
    metoda();
  }

  virtual void virtualnaMetoda() {
    printf("ja sam bazna implementacija!\n");
  }

  void metoda() {
    printf("Metoda kaze: ");
    virtualnaMetoda();
  }
};

class Derived : public Base {
public:
  Derived() : Base() {
    metoda();
  }
  virtual void virtualnaMetoda() {
    printf("ja sam izvedena implementacija!\n");
  }
};

// poziva se Derived() koji poziva Base() te tu se ispisuuje prva linija onda nakon što se obavi bazni konstruktor obavlja se
// Derived konstruktor koji poziva drugu liniju.
int main() {
  Derived *pd = new Derived();
  pd->metoda(); // treca linija
}