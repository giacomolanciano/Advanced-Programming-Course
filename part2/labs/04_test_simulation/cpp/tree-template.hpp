#include <stddef.h>
#include <vector>

/*
 * necessary, because of template usage
 * */
template<typename T>
class tree; 

template<typename T>
class node {
	T info;
	
	/*
	 * Compilator doesn't complain if you write:
	 * std::vector<node*> children;
	 * 
	 * Maybe because when dealing with pointers it doesn't have to know
	 * the real type. Anyway, putting <T> is better, to avoid errors 
	 * for sure
	 * */
	std::vector<node<T>*> children;
	
	node<T>* parent;
	friend class tree<T>;
public:
	/*
	 * instead of define a custom node iterator, simply "box" the one that
	 * vector class provides
	 * */
	typedef typename std::vector<node<T>*>::iterator iterator;
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

template<typename T>
class tree {
	node<T>* r;
	void del(node<T>* v) {
		if (v == NULL) return;
		for (typename node<T>::iterator it = v->begin(); it != v->end(); ++it)	
			del(*it);
		delete v;
	}
public:
	tree() : r(NULL) { }
	~tree() { remove(r); }
	node<T>* add(T info, node<T>* parent) {
		node<T>* v = new node<T>;
		v->info = info;
		v->parent = parent;
		if (parent != NULL) parent->children.push_back(v);
		else {
			if (r != NULL) del(r);
			r = v;
		}
		return v;
	}
	node<T>* add(T info) {
		return add(info, NULL);
	}
	node<T>* root() { return r; }
	void remove(node<T>* v) {
		if (r == v) r = NULL;
		else { 
			/* 
			 * remove v from the list of children of its parent 
			 * */
			int i = 0;
			for(typename node<T>::iterator it = v->parent->begin(); 
				it != v->parent->end() && *it != v; ++it) {
				
				i++;
			}
			v->parent->children.erase(v->parent->children.begin()+i);
		}
		del(v);
	}
};
