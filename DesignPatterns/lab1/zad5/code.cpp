#include <stdio.h>

class B {
public:
  virtual int prva() = 0;
  virtual int druga(int) = 0;
};

typedef int (*druga)(B *, int);

class D : public B {
public:
  virtual int prva() { return 42; }
  virtual int druga(int x) { return prva() + x; }
};

int func(B *pb) {
  void *tablica = *(void **)pb;
  druga *dr_func = (druga *)tablica;
  return dr_func[1](pb, 5);
}

int main() {
  B *d = new D();
  printf("%d\n", func(d));
}