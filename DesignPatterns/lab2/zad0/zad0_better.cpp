#include <assert.h>
#include <iostream>
#include <list>
#include <stdlib.h>

struct Point {
  int x;
  int y;
};
std::string printPoint(Point p) {
  return ((std::string) "( ")
      .append(std::to_string(p.x))
      .append(" , ")
      .append(std::to_string(p.y))
      .append(" )\n");
}
class Shape {
public:
  Point center_;
  Shape(Point center) {
    center_ = center;
  }
  virtual void draw() = 0;
  //trans move object relative to (0,0)
  void move(Point trans) {
    this->center_.x += trans.x;
    this->center_.y += trans.y;
  }
};
class Circle : public Shape {
public:
  Circle(Point p, double r) : Shape(p) {
    radius_ = r;
  }
  virtual void draw() {
    std::cerr << "in drawCircle\n";
  }
  double radius_;
};
class Square : public Shape {
public:
  Square(Point p, double d) : Shape(p) {
    side_ = d;
  }
  virtual void draw() {
    std::cerr << "in drawSquare\n";
  }
  double side_;
};
class Rhomb : public Shape {
public:
  Rhomb(Point p, double d) : Shape(p) {
    side_ = d;
  }
  virtual void draw() {
    std::cerr << "in drawRhomb\n";
  }
  double side_;
};
void drawShapes(const std::list<Shape *> &fig) {
  std::list<Shape *>::const_iterator it;
  for (it = fig.begin(); it != fig.end(); ++it) {
    (*it)->draw();
  }
}
void moveShapes(const std::list<Shape *> &fig, Point trans) {
  std::list<Shape *>::const_iterator it;
  for (it = fig.begin(); it != fig.end(); ++it) {
    (*it)->move(trans);
  }
}
void writeCenter(const std::list<Shape *> &fig) {
  std::list<Shape *>::const_iterator it;
  for (it = fig.begin(); it != fig.end(); ++it) {
    std::cout << "Centar je " << printPoint((*it)->center_);
  }
}
int main() {
  std::list<Shape *> list;
  list.push_back((Shape *)new Square((Point){1, 2}, 2));
  list.push_back((Shape *)new Rhomb((Point){5, 5}, 1));
  list.push_back((Shape *)new Circle((Point){1, 2}, 2));
  drawShapes(list);
  moveShapes(list, (Point){2, 2});
  writeCenter(list);
}
