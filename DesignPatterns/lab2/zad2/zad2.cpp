#include <iostream>
#include <string.h>
#include <vector>

template <typename Iterator, typename Predicate>
Iterator mymax(
    Iterator first, Iterator last, Predicate pred) {
  Iterator max;
  int i = 0;
  while (first != last) {
    if (i == 0) {
      max = first;
      i = 1;
    }
    if (pred(first, max) > 1)
      max = first;

    ++first;
  }
  return max;
}

int gt_int(const void *a, const void *b) {
  return (*(int *)a - *(int *)b);
}

int gt_char(const void *a, const void *b) {
  return (*(char *)a - *(char *)b);
}
int gt_str(const void *a, const void *b) {
  int i = strcmp(*(char **)a, *(char **)b);
  return i;
}

int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
int main() {
  const int *maxint = mymax(&arr_int[0], &arr_int[sizeof(arr_int) / sizeof(*arr_int)], gt_int);
  std::cout << *maxint << "\n";
  std::vector<std::string> g1;
  g1.push_back("Marko");
  g1.push_back("Dora");
  g1.push_back("Filip");
  const std::string *maxstr = mymax(g1.cbegin().base(), g1.cend().base(), gt_str);
  std::cout << *maxstr << "\n";
}