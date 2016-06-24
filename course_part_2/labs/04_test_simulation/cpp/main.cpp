#include "tree.hpp"
#include <iostream>

static void preorder(node* r) {
	if (r == NULL) return;
	std::cout << r->get() << std::endl;
	for (node::iterator it = r->begin(); it != r->end(); ++it)	
		preorder(*it);
}

static void print(tree& t) {
	std::cout << "tree: " << std::endl;
	preorder(t.root());
}

int main() {
	tree t;
	node* n  = t.add(10);	// add root
	node* v1 = t.add(20,n);  // add child of root
	node* v2 = t.add(30,n);  // add child of root
	node* v3 = t.add(15,n);  // add child of root
	node* v4 = t.add(25,v2); // add child of v2
	print(t);
	t.remove(v2);
	print(t);
	return 0;
}
