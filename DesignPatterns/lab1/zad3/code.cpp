#include <stdio.h>

class CoolClass {
public:
  virtual void set(int x) { x_ = x; };
  virtual int get() { return x_; };

private:
  int x_;
};
class PlainOldClass {
public:
  void set(int x) { x_ = x; };
  int get() { return x_; };

private:
  int x_;
};

int main() {
  CoolClass c;
  PlainOldClass p;

  printf("%d\n", sizeof(CoolClass));     // virtual table 8, int 4 i nadopuna 4, zbog x64
  printf("%d\n", sizeof(PlainOldClass)); //ne virtualne metode ne zauzimaju memoriju tako da sizeof je jedino int
}