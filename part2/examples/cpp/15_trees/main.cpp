#include "tree.hpp"
#include <iostream>

/*
 * preorder visit
 * */
void preorder(node* r) {
	//std::cout << r << std::endl;
	if(r == NULL) 
		return;
	std::cout << r->get() <<std::endl;
	for(node::iterator it = r->begin(); it != r->end(); ++it) {
		preorder(*it);
	}
}

int main() {
	tree t;
	node* n = t.add(10);
	node* v1 = t.add(20, n);
	node* v2 = t.add(30, n);
	node* v3 = t.add(15, n);
	node* v4 = t.add(25, v2);
	/*for(node::iterator it = n->begin(); it != n->end(); ++it) {
		std::cout << (*it)->get() << std::endl;
	}*/
	preorder(t.root());
	return 0;
}
