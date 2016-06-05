#include <stddef.h>
#include <vector>

typedef int T;

/*
 * not necessary, actually
 * */
class tree; 

class node {
	T info;
	std::vector<node*> children;
	node* parent;
	friend class tree;
public:
	/*
	 * instead of define a custom node iterator, simply "box" the one that
	 * vector class provides
	 * */
	typedef std::vector<node*>::iterator iterator;
	iterator begin() {
		return children.begin();
	}
	iterator end() {
		return children.end();
	}
	T get() {
		return info;
	}
};

class tree {
	node* r;
	void del(node* v) {
		if (v == NULL) return;
		for (node::iterator it = v->begin(); it != v->end(); ++it)	
			del(*it);
		delete v;
	}
public:
	tree() : r(NULL) { }
	~tree() { remove(r); }
	node* add(T info, node* parent) {
		node* v = new node;
		v->info = info;
		v->parent = parent;
		if (parent != NULL) parent->children.push_back(v);
		else {
			if (r != NULL) del(r);
			r = v;
		}
		return v;
	}
	node* add(T info) {
		return add(info, NULL);
	}
	node* root() { return r; }
	void remove(node* v) {
		if (r == v) r = NULL;
		else { 
			/* 
			 * remove v from the list of children of its parent 
			 * */
			int i = 0;
			for(node::iterator it = v->parent->begin(); 
				it != v->parent->end() && *it != v; ++it) {
				
				i++;
			}
			v->parent->children.erase(v->parent->children.begin()+i);
		}
		del(v);
	}
};
