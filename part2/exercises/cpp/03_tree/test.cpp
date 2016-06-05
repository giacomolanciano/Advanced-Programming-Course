#include "tree-template.hpp"
#include <iostream>

template <class T>
void print(tree<T>& t) {
    std::cout << "iterating over the tree" << std::endl;
    for (typename tree<T>::iterator it = t.begin(); it != t.end(); ++it)
        std::cout << (*it)->get() << std::endl;
}


int main() {
    tree<int> t;
    node<int>* n  = t.add(10);    // add root
    node<int>* v1 = t.add(20,n);  // add child of root
    node<int>* v2 = t.add(30,n);  // add child of root
    node<int>* v3 = t.add(15,n);  // add child of root
    node<int>* v4 = t.add(25,v2); // add child of v2
    print(t);
    t.remove(v2);
    print(t);
    return 0;
}
