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
	 * instead of define custom node iterators, simply "box" the ones that
	 * vector class provides
	 * */
	typedef typename std::vector<node<T>*>::iterator iterator;
	typedef typename std::vector<node<T>*>::reverse_iterator reverse_iterator;
	iterator begin() {
		return children.begin();
	}
	iterator end() {
		return children.end();
	}
	reverse_iterator rbegin() {
		return children.rbegin();
	}
	reverse_iterator rend() {
		return children.rend();
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
	class iterator {
		/*
		 * implements a pre-order visit over tree nodes
		 * */
		
		std::vector<node<T>*> nodes;
		
		/*
		 * defined in order to use empty iterator (see below)
		 * */
		iterator() {}
		
		iterator(node<T>* v) {
			nodes.push_back(v);
		}
		
		friend class tree<T>;
	public:
		bool operator != (const iterator& it) {
			/*
			 * & for efficiency, const is required
			 * 
			 * assuming that operator is only used to check wheter iterator
			 * has been entirely consumed (i.e. it is the result of end())
			 * */
			return nodes.size() != it.nodes.size();   
		}
		
		iterator& operator ++ () {
			/*
			 * this works only for pre-increment
			 * side-effect on nodes
			 * */
			node<T>* top = nodes.back();
			nodes.pop_back();
			
			/*
			 * since the structure we are iterating over will not be
			 * modified, we can apply loop invariant code motion to 
			 * improve performances (rend() is called only once instead
			 * of once per iteration)
			 * */
			typename node<T>::reverse_iterator end = top->rend();
			for(typename node<T>::reverse_iterator it = top->rbegin(); 
					it != end; ++it)
				nodes.push_back(*it);
		}
		
		node<T>*& operator * () {
			return nodes.back();  
		}
	};
private:
	iterator end_iter;
public:
	tree() : r(NULL) { }
	~tree() { 
		remove(r); 
	}
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
	node<T>* root() { 
		return r; 
	}
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
	iterator begin() {
		return iterator(r);
	}
	iterator& end() {		
		return end_iter;
	}
};
